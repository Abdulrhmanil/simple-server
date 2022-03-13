package com.example.simpleserver.controllers;

import com.example.simpleserver.exceptions.InternalServerError;
import com.example.simpleserver.exceptions.UnauthorizedException;
import com.example.simpleserver.models.TimeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Tag(name = "Log", description = "Managing the logs")
@RestController
@RequestMapping("/api")
public class LogController {
    private final Log logger = LogFactory.getLog(LogController.class);

    @Operation(summary = "Get and log current time, no authorization require")
    @GetMapping("/time")
    public TimeResponse addInfoLog() {
        final String now = Date.from(Instant.now()).toString();
        logger.info(String.format("Info time requested at %s", now));
        return new TimeResponse(now);
    }

    @Operation(summary = "Get All logs as a text, authorization is required")
    @GetMapping("/log")
    public String getLogs(@RequestHeader(value = "Authorization", required = false) String authorization) {
        final String accessKey = System.getenv("ACCESS_KEY");
        if (authorization == null || accessKey == null || !authorization.equals("Bearer " + accessKey)) {
            throw new UnauthorizedException("Unauthorized access");
        }
        try {
            return FileUtils.readFileToString(new File("logs/out.log"), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new InternalServerError("Fail to read log file, try to restart the server");
        }
    }
}
