package com.promptdb.auth.controllers.health;

import com.promptdb.auth.dto.HealthCheckResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheck {

    @GetMapping("")
    public ResponseEntity<HealthCheckResponse> healthCheck() {
        HealthCheckResponse healthCheckResponse = new HealthCheckResponse("ok", "qwerty");
        return new ResponseEntity<HealthCheckResponse>(healthCheckResponse, HttpStatus.OK);
    }
}
