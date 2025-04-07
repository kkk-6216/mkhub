package com.pro.mkhub_backend.admin.service;

import com.pro.mkhub_backend.admin.dto.LogEntry;
import org.apache.commons.io.input.ReversedLinesFileReader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class LogFileReaderService {

    private static final Path LOG_PATH = Path.of("logs/requests.log");
    private static final Pattern LOG_PATTERN = Pattern.compile(
            "(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}) .* User=(.*?), Role=(.*?), Method=(.*?), URI=(.*?), Duration=(.*?)ms"
    );

    public List<LogEntry> getFilteredLogs(String url, String user, String role, String method) {
        List<LogEntry> result = new ArrayList<>();

        try (ReversedLinesFileReader reader = new ReversedLinesFileReader(LOG_PATH.toFile(), StandardCharsets.UTF_8)) {
            String line;
            int count = 0;

            while ((line = reader.readLine()) != null && count < 100) {
                Matcher matcher = LOG_PATTERN.matcher(line);
                if (matcher.find()) {
                    LogEntry entry = new LogEntry(
                            matcher.group(1),
                            matcher.group(2),
                            matcher.group(3),
                            matcher.group(4),
                            matcher.group(5),
                            matcher.group(6)
                    );

                    if ((url == null || entry.getUrl().contains(url)) &&
                            (user == null || entry.getUser().equalsIgnoreCase(user)) &&
                            (role == null || entry.getRole().equalsIgnoreCase(role)) &&
                            (method == null || entry.getMethod().equalsIgnoreCase(method))) {
                        result.add(entry);
                        count++;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public Map<String, Integer> getHourlyStats(String url) {
        Map<String, Integer> hourlyCounts = new TreeMap<>();
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");

        try (Stream<String> lines = Files.lines(LOG_PATH)) {
            lines
                    .filter(line -> line.contains(url))
                    .forEach(line -> {
                        Matcher matcher = LOG_PATTERN.matcher(line);
                        if (matcher.find()) {
                            String timestamp = matcher.group(1);
                            LocalDateTime dt = LocalDateTime.parse(timestamp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            String hour = dt.format(hourFormatter);
                            hourlyCounts.put(hour, hourlyCounts.getOrDefault(hour, 0) + 1);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return hourlyCounts;
    }


}


