package com.example.simpleserver.controllers;

import com.example.simpleserver.exceptions.InternalServerError;
import com.example.simpleserver.models.TimeResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class LogController {
    private final Log logger = LogFactory.getLog(LogController.class);

    @GetMapping("/time/info")
    public TimeResponse addInfoLog() {
        final String now = Date.from(Instant.now()).toString();
        logger.info(String.format("Info time requested at %s", now));
        return new TimeResponse(now);
    }

    @GetMapping("/time/error")
    public TimeResponse addErrorLog() {
        final String now = Date.from(Instant.now()).toString();
        logger.error(String.format("Error time requested at %s", now));
        return new TimeResponse(now);
    }

    @GetMapping("/time/warn")
    public TimeResponse addWarnLog() {
        final String now = Date.from(Instant.now()).toString();
        logger.warn(String.format("Warn time requested at %s", now));
        return new TimeResponse(now);
    }

    @GetMapping("/log")
    public String getLogs() {
        try {
            return FileUtils.readFileToString(new File("logs/out.log"), StandardCharsets.UTF_8);
        }
        catch (IOException e) {
            throw new InternalServerError("Fail to read log file, try to restart the server");
        }
    }
}
