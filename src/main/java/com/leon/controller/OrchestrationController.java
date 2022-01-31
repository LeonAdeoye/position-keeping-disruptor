package com.leon.controller;

import com.leon.model.Inventory;
import com.leon.service.OrchestrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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
    @RequestMapping(value = "/start", method = RequestMethod.GET)
    void start()
    {
        logger.info("Received request to start orchestration.");
        orchestrationService.start();
    }

    @CrossOrigin
    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    void stop()
    {
        logger.info("Received request to stop orchestration.");
        orchestrationService.stop();
    }

    @CrossOrigin
    @RequestMapping(value = "/uploadSOD", method = RequestMethod.GET)
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
    @RequestMapping(value = "/getInventory", method = RequestMethod.GET)
    public List<Inventory> getInventory()
    {
        logger.info("Received request to get all inventory items.");
        return orchestrationService.getInventory();
    }

    @CrossOrigin
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public void updateInventory(@RequestBody Inventory inventory)
    {
        logger.info("Received request to update inventory: " + inventory);
        orchestrationService.updateInventory(inventory);
    }

    @CrossOrigin
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteInventory(@RequestBody Inventory inventory)
    {
        logger.info("Received request to delete inventory: " + inventory);
        orchestrationService.deleteInventory(inventory);
    }

    @CrossOrigin
    @RequestMapping(value = "/clear", method = RequestMethod.DELETE)
    public void clear()
    {
        logger.info("Received request to clear inventory.");
        orchestrationService.clearInventory();
    }

    @CrossOrigin
    @RequestMapping(value = "/togglePrimary", method = RequestMethod.GET)
    public boolean togglePrimary()
    {
        logger.info("Received request to toggle isPrimary mode.");
        return orchestrationService.togglePrimary();
    }
}