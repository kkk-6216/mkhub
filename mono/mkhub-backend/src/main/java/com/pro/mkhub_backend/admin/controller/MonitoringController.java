package com.pro.mkhub_backend.admin.controller;

import com.pro.mkhub_backend.admin.dto.EndpointInfo;
import com.pro.mkhub_backend.admin.dto.LogEntry;
import com.pro.mkhub_backend.admin.scanner.EndpointScanner;
import com.pro.mkhub_backend.admin.service.LogFileReaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class MonitoringController {

    private final EndpointScanner endpointScanner;
    private final LogFileReaderService logFileReaderService;

    @GetMapping("/endpoints")
    public List<EndpointInfo> getEndpoints() {
        return endpointScanner.getEndpoints();
    }

    @GetMapping("/logs")
    public List<LogEntry> getLogs(@RequestParam(required = false) String url,
                                  @RequestParam(required = false) String user,
                                  @RequestParam(required = false) String role,
                                  @RequestParam(required = false) String method) {
        return logFileReaderService.getFilteredLogs(url, user, role, method);
    }

    @GetMapping("/stats")
    public Map<String, Integer> getStatsForUrl(@RequestParam String url) {
        return logFileReaderService.getHourlyStats(url);
    }

}

