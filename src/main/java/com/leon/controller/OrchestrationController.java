package com.leon.controller;

import com.leon.model.Inventory;
import com.leon.service.OrchestrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/orchestrate")
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

    @CrossOrigin
    @RequestMapping(value = "/getAll")
    public List<Inventory> getAll()
    {
        logger.info("Received request to get all inventories.");
        return new ArrayList<>();
    }

    @CrossOrigin
    @RequestMapping(value = "/update")
    public void update(@RequestBody Inventory inventory)
    {
        logger.info("Received request to update inventory: " + inventory);
    }
}