package com.leon.controller;

import com.leon.service.DisruptorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class DisruptorController
{
    private static final Logger logger = LoggerFactory.getLogger(DisruptorController.class);
    @Autowired
    DisruptorService disruptorService;

    @CrossOrigin
    @RequestMapping("/heartbeat")
    String heartbeat()
    {
        return "Here I am";
    }

    @CrossOrigin
    @RequestMapping(value = "/start", produces=MediaType.APPLICATION_JSON_VALUE )
    void start()
    {
        logger.info("Received request to start disruptor");
        disruptorService.start();
    }

    @CrossOrigin
    @RequestMapping(value = "/stop")
    void stop()
    {
        logger.info("Received request to stop disruptor");
        disruptorService.stop();
    }
}