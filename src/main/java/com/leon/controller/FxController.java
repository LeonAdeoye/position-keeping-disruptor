package com.leon.controller;

import com.leon.service.FxService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/fx")
public class FxController
{
	private static final Logger logger = LoggerFactory.getLogger(FxController.class);
	@Autowired
	private FxService fxService;

	@CrossOrigin
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add()
	{
		logger.info("Received request to add FX rate: ");
	}

	@CrossOrigin
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public double get(@RequestParam String currency)
	{
		logger.info("Received request get the Fx rate for " + currency);
		return 0.0;
	}

	@CrossOrigin
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public List<String> get()
	{
		logger.info("Received request get all FX rates.");
		return new ArrayList<>();
	}

	@CrossOrigin
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(@RequestParam String currency, @RequestParam double fxRate)
	{
		logger.info("Received request to update FX rate: ");
	}

	@CrossOrigin
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public void uploadFx(@RequestParam String file)
	{
		if(file == null || file.isEmpty())
		{
			logger.error("file request parameter cannot be null or empty");
			throw new IllegalArgumentException("file request parameter cannot be null or empty");
		}

		logger.info("Received request to upload FX file: " + file);
	}
}
