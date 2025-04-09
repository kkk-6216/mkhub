package com.pro.mkhub_backend.admin.service;

import com.pro.mkhub_backend.admin.dto.LogEntry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Slf4j
@Service
public class LogFileReaderService {

    private static final Path LOG_DIR = Path.of("logs");
    private static final String LOG_FILE_PREFIX = "requests";

    // Формат временной метки в основной строке (с миллисекундами)
    private static final DateTimeFormatter MAIN_TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    // Формат для группировки по часу
    private static final DateTimeFormatter HOUR_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

    /**
     * Регулярное выражение для основной строки.
     * Захватывает:
     *   1: timestamp (yyyy-MM-dd HH:mm:ss.SSS)
     *   2: User – все символы до ", Role="
     *   3: Role – до следующей запятой
     *   4: IP
     *   5: Method
     *   6: URI
     *   7: Status (число, не используется)
     *   8: Duration (число)
     */
    private static final Pattern MAIN_LINE_PATTERN = Pattern.compile(
            "^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\s+\\S+\\s+\\[[^\\]]+\\]\\s+REQUEST_LOGGER\\s+-\\s+" +
                    "User=(.+?),\\s*Role=(.+?),\\s*IP=(.+?),\\s*Method=(.+?),\\s*URI=(.+?),\\s*Status=(\\d+),\\s*Duration=(\\d+)ms$"
    );

    /**
     * Регулярное выражение для строки с заголовками.
     * Ожидается формат:
     * Headers => [Authorization=..., Content-Type=..., User-Agent=...]
     */
    private static final Pattern HEADERS_PATTERN = Pattern.compile(
            "^Headers\\s*=>\\s*\\[\\s*Authorization=(.*?),\\s*Content-Type=(.*?),\\s*User-Agent=(.*)\\]$"
    );

    /**
     * Получает список логов, удовлетворяющих фильтрам.
     * Если параметр фильтра не равен null – он сравнивается после trim(), даже если пустой.
     *
     * @param urlFilter    фильтр по URI (подстрока)
     * @param userFilter   фильтр по пользователю (точное сравнение без учёта регистра)
     * @param roleFilter   фильтр по роли (точное сравнение без учёта регистра)
     * @param methodFilter фильтр по методу (точное сравнение без учёта регистра)
     * @param limit        максимальное число возвращаемых записей
     * @return список LogEntry
     */
    public List<LogEntry> getFilteredLogs(String urlFilter, String userFilter, String roleFilter, String methodFilter, int limit) {

        if(urlFilter == null || urlFilter.trim().isEmpty()) {
            return Collections.emptyList();
        }

        System.out.println("In");
        List<LogEntry> result = new ArrayList<>();

        File[] logFiles = LOG_DIR.toFile().listFiles((dir, name) -> name.startsWith(LOG_FILE_PREFIX));
        if (logFiles == null) {
            return result;
        }
        Arrays.sort(logFiles, Comparator.comparing(File::getName).reversed());

        int count = 0;
        for (File file : logFiles) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
                List<String> lines = reader.lines().toList();
                List<List<String>> logEntries = groupLogEntries(lines);

                // Обрабатываем записи в обратном порядке (новые записи первыми)
                Collections.reverse(logEntries);
                for (List<String> entryLines : logEntries) {
                    LogEntry entry = parseLogEntry(entryLines);
                    if (entry == null) {
                        continue;
                    }

                    if (!urlFilter.trim().isEmpty() && !entry.getUrl().contains(urlFilter.trim())) {
                        continue;
                    }
                    if (userFilter != null && !userFilter.trim().isEmpty() && !entry.getUser().trim().equalsIgnoreCase(userFilter.trim())) {
                        continue;
                    }
                    if (roleFilter != null && !roleFilter.trim().isEmpty() && !entry.getRole().trim().equalsIgnoreCase(roleFilter.trim())) {
                        continue;
                    }
                    if (methodFilter != null && !methodFilter.trim().isEmpty() && !entry.getMethod().trim().equalsIgnoreCase(methodFilter.trim())) {
                        continue;
                    }
                    result.add(entry);
                    count++;
                    if (count >= limit) {
                        break;
                    }
                }
                if (count >= limit) {
                    break;
                }
            } catch (IOException e) {
                log.error("Ошибка при чтении файла логов: {}", file.getName(), e);
            }
        }
        log.info("Возвращено записей: {}", result.size());
        return result;
    }

    /**
     * Формирует статистику – количество записей по часу, для записей, у которых URI содержит urlFilter (если он задан).
     *
     * @param urlFilter подстрока для поиска в URI
     * @return карта, где ключ – час (yyyy-MM-dd HH), значение – число записей за этот час.
     */
    public Map<String, Integer> getHourlyStats(String urlFilter) {
        Map<String, Integer> hourlyCounts = new TreeMap<>();
        log.info("Начало формирования статистики по часам для urlFilter='{}'", urlFilter);
        try (Stream<Path> paths = Files.list(LOG_DIR)) {
            paths.filter(p -> p.getFileName().toString().startsWith(LOG_FILE_PREFIX))
                    .forEach(path -> {
                        log.info("Обработка файла для статистики: {}", path.getFileName());
                        try (Stream<String> linesStream = Files.lines(path)) {
                            List<String> lines = linesStream.toList();
                            log.info("Прочитано строк для статистики: {}", lines.size());
                            List<List<String>> logEntries = groupLogEntries(lines);
                            log.info("Сгруппировано записей для статистики: {}", logEntries.size());
                            for (List<String> entryLines : logEntries) {
                                LogEntry entry = parseLogEntry(entryLines);
                                if (entry == null) {
                                    log.info("Запись для статистики не распознана, пропуск.");
                                    continue;
                                }
                                if (urlFilter != null && !entry.getUrl().contains(urlFilter.trim())) {
                                    continue;
                                }
                                String hourKey = entry.getTimestamp().format(HOUR_FORMAT);
                                int newCount = hourlyCounts.getOrDefault(hourKey, 0) + 1;
                                hourlyCounts.put(hourKey, newCount);
                            }
                        } catch (IOException e) {
                            log.error("Ошибка при чтении строк из файла: {}", path.getFileName(), e);
                        }
                    });
        } catch (IOException e) {
            log.error("Ошибка при перечислении файлов в директории логов", e);
        }
        log.info("Статистика сформирована: {}", hourlyCounts);
        return hourlyCounts;
    }

    /**
     * Группирует исходные строки файла в отдельные логические записи.
     * Новая запись начинается, когда строка начинается с даты (yyyy-MM-dd…).
     *
     * @param lines список строк файла
     * @return список записей (каждая запись – список строк, не пустых)
     */
    private List<List<String>> groupLogEntries(List<String> lines) {
        List<List<String>> entries = new ArrayList<>();
        List<String> currentEntry = new ArrayList<>();
        for (String line : lines) {
            if (line.matches("^\\d{4}-\\d{2}-\\d{2}.*")) {
                if (!currentEntry.isEmpty()) {
                    entries.add(new ArrayList<>(currentEntry));
                    log.info("Новая запись сформирована, строк в записи: {}", currentEntry.size());
                    currentEntry.clear();
                }
            }
            if (!line.isBlank()) {
                currentEntry.add(line);
            }
        }
        if (!currentEntry.isEmpty()) {
            entries.add(new ArrayList<>(currentEntry));
            log.info("Финальная запись сформирована, строк в записи: {}", currentEntry.size());
        }
        log.info("Всего групп записей: {}", entries.size());
        return entries;
    }

    /**
     * Парсит один блок лог-записи.
     * Ожидается следующий формат:
     *   Line 1: основная строка с данными (MAIN_LINE_PATTERN)
     *   Line 2: Headers => [Authorization=…, Content-Type=…, User-Agent=…] (опционально)
     *   Line 3: RequestBody => … (опционально)
     *   Line 4: ResponseBody => … (опционально)
     *   Line 5: Timestamp => <ISO timestamp> (опционально, переопределяет время из Line 1)
     *
     * @param lines блок строк записи
     * @return LogEntry или null, если не удалось распарсить основную строку
     */
    private LogEntry parseLogEntry(List<String> lines) {
        if (lines.isEmpty()) {
            return null;
        }
        String mainLine = lines.get(0);
        Matcher m = MAIN_LINE_PATTERN.matcher(mainLine);
        if (!m.matches()) {
            return null;
        }
        try {
            String timestampStr = m.group(1).trim();
            LocalDateTime timestamp = LocalDateTime.parse(timestampStr, MAIN_TIMESTAMP_FORMAT);
            String user = m.group(2).trim();
            String role = m.group(3).trim();
            String ip = m.group(4).trim();
            String method = m.group(5).trim();
            String uri = m.group(6).trim();

            String statusStr = m.group(7).trim();
            String durationStr = m.group(8).trim();
            long duration = Long.parseLong(durationStr);

            String auth = null;
            String contentType = null;
            String userAgent = null;
            String requestBody = null;
            String responseBody = null;

            // Обработка дополнительных строк
            for (int i = 1; i < lines.size(); i++) {
                String line = lines.get(i).trim();
                if (line.startsWith("Headers =>")) {
                    Matcher hm = HEADERS_PATTERN.matcher(line);
                    if (hm.matches()) {
                        auth = hm.group(1).trim();
                        contentType = hm.group(2).trim();
                        userAgent = hm.group(3).trim();

                    }
                } else if (line.startsWith("RequestBody =>")) {
                    requestBody = line.substring("RequestBody =>".length()).trim();
                    if(requestBody.contains("password")) {
                        requestBody = requestBody.replaceAll("(\"password\"\\s*:\\s*\")[^\"]+\"", "$1*****\"");
                    }
                } else if (line.startsWith("ResponseBody =>")) {
                    responseBody = line.substring("ResponseBody =>".length()).trim();
                } else if (line.startsWith("Timestamp =>")) {
                    String isoTs = line.substring("Timestamp =>".length()).trim();
                    try {
                        LocalDateTime isoTimestamp = LocalDateTime.parse(isoTs);
                        timestamp = isoTimestamp;

                    } catch (Exception e) {
                        log.warn("Не удалось распарсить ISO Timestamp: {}", isoTs, e);
                    }
                }
            }

            LogEntry entry = new LogEntry();
            entry.setTimestamp(timestamp);
            entry.setUser(user);
            entry.setRole(role);
            entry.setIpAddress(ip);
            entry.setMethod(method);
            entry.setUrl(uri);
            entry.setStatus(statusStr);
            entry.setDuration(duration);
            entry.setAuthorization(auth);
            entry.setContentType(contentType);
            entry.setUserAgent(userAgent);
            entry.setRequestBody(requestBody);
            entry.setResponseBody(responseBody);

            return entry;
        } catch (Exception ex) {
            log.error("Ошибка парсинга лог-записи: {}", lines, ex);
            return null;
        }
    }
}
