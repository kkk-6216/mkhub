package com.pro.mkhub_backend.admin.controller;

import com.pro.mkhub_backend.admin.dto.EndpointInfo;
import com.pro.mkhub_backend.admin.dto.LogEntry;
import com.pro.mkhub_backend.admin.scanner.EndpointScanner;
import com.pro.mkhub_backend.admin.service.LogFileReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MonitoringController {

    private final EndpointScanner endpointScanner;
    private final LogFileReaderService logFileReaderService;

    @GetMapping("/endpoints")
    public ResponseEntity<List<EndpointInfo>> getEndpoints() {
        return ResponseEntity.ok(endpointScanner.getEndpoints());
    }

    @GetMapping("/logs")
    public List<LogEntry> getLogs(@RequestParam(required = false) String url,
                                  @RequestParam(required = false) String user,
                                  @RequestParam(required = false) String role,
                                  @RequestParam(required = false) String method,
                                  @RequestParam(defaultValue = "100") int limit,
                                  @RequestParam(defaultValue = "desc") String order) {
        int safeLimit = Math.min(Math.max(limit, 10), 1000);
        List<LogEntry> logs = logFileReaderService.getFilteredLogs(url, user, role, method, safeLimit);

        if ("asc".equalsIgnoreCase(order)) {
            Collections.reverse(logs);
        }

        return logs;
    }

    @GetMapping("/stats")
    public Map<String, Integer> getStatsForUrl(@RequestParam String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL параметр не должен быть пустым");
        }

        return logFileReaderService.getHourlyStats(url);
    }

}

