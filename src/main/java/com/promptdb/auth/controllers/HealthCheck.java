package com.promptdb.auth.controllers;

import com.promptdb.auth.dto.HealthCheckResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Slf4j
public class HealthCheck {

    @GetMapping("")
    public ResponseEntity<HealthCheckResponse> healthCheck() {
        log.info("info health check endpoint");
        log.debug("debug health check endpoint");
        log.error("error health check endpoint");
        log.warn("warn health check endpoint");
        log.trace("trace health check endpoint");
        HealthCheckResponse healthCheckResponse = new HealthCheckResponse("ok", "qwerty");
        return new ResponseEntity<HealthCheckResponse>(healthCheckResponse, HttpStatus.OK);
    }
}
