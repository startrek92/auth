package com.dosepack.auth.controllers;

import com.dosepack.auth.schema.HealthCheck;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class Health {

    @GetMapping("")
    public HealthCheck health() {
        HealthCheck healthCheck = new HealthCheck(
                "ok", "9380388ca7cdceab5a91f820f9fcd8043aa16b49"
        );

        return healthCheck;
    }
}
