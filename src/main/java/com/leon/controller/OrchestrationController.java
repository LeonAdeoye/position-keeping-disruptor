package com.leon.controller;

import com.leon.service.OrchestrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OrchestrationController
{
    private static final Logger logger = LoggerFactory.getLogger(OrchestrationController.class);
    @Autowired
    OrchestrationService orchestrationService;

    @CrossOrigin
    @RequestMapping("/heartbeat")
    String heartbeat()
    {
        return "Here I am";
    }

    @CrossOrigin
    @RequestMapping(value = "/start")
    void start()
    {
        logger.info("Received request to start orchestration.");
        orchestrationService.start();
    }

    @CrossOrigin
    @RequestMapping(value = "/stop")
    void stop()
    {
        logger.info("Received request to stop orchestration.");
        orchestrationService.stop();
    }

    @CrossOrigin
    @RequestMapping(value = "/uploadSOD")
    void uploadSOD(@RequestParam String file)
    {
        if(file == null || file.isEmpty())
        {
            logger.error("file request parameter cannot be null or empty");
            throw new IllegalArgumentException("file request parameter cannot be null or empty");
        }

        logger.info("Received request to upload SOD file: " + file);
        orchestrationService.upload(file);
    }
}