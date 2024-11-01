package com.promptdb.auth.controller.error;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {

    record DefaultResponse(String message) {
    }

    @RequestMapping(path = "/**")
    public ResponseEntity<DefaultResponse> defaultController() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(new DefaultResponse("not found"), headers, HttpStatus.NOT_FOUND);
    }
}
