package com.leon.controller;

import com.leon.model.FxRate;
import com.leon.service.FxService;
import com.leon.service.FxServiceImpl;
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
	public void add(@RequestBody FxRate fxRate)
	{
		logger.info("Received request to add FX rate: ");
		fxService.put(fxRate.getCurrency(), fxRate.getFxRateAgainstUSD());
	}

	@CrossOrigin
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public FxRate get(@RequestParam String currency)
	{
		logger.info("Received request get the Fx rate for " + currency);
		return fxService.get(currency).orElse(FxServiceImpl.defaultUSDRate);
	}

	@CrossOrigin
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public List<FxRate> get()
	{
		logger.info("Received request get all FX rates.");
		return new ArrayList<>();
	}

	@CrossOrigin
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(@RequestBody FxRate fxRate)
	{
		logger.info("Received request to update FX rate: " + fxRate);
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
