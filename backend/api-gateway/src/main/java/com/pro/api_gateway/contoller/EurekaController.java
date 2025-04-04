package com.pro.api_gateway.contoller;

import com.pro.api_gateway.dto.EurekaResponse;
import com.pro.api_gateway.service.EurekaWebClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/admin/eureka")
public class EurekaController {

    private final EurekaWebClientService eurekaWebClientService;

    @Autowired
    public EurekaController(EurekaWebClientService eurekaWebClientService) {
        this.eurekaWebClientService = eurekaWebClientService;
    }

    @GetMapping("/services")
    public Mono<ResponseEntity<EurekaResponse>> getServices() {
        return eurekaWebClientService
                .getEurekaApps()
                .map(ResponseEntity::ok);
    }

}
