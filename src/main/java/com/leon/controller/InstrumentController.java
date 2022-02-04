package com.leon.controller;

import com.leon.model.Instrument;
import com.leon.service.InstrumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/instrument")
public class InstrumentController
{
	private static final Logger logger = LoggerFactory.getLogger(InstrumentController.class);
	@Autowired
	InstrumentService instrumentService;

	@CrossOrigin
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@RequestBody Instrument instrument)
	{
		logger.info("Received request to add instrument: " + instrument);
	}

	@CrossOrigin
	@RequestMapping(value = "/getWithBloomberg", method = RequestMethod.GET)
	public Instrument get(@RequestParam String BloombergCode)
	{
		logger.info("Received request get the instrument with Bloomberg code: " + BloombergCode);
		return new Instrument();
	}

	@CrossOrigin
	@RequestMapping(value = "/getWithId", method = RequestMethod.GET)
	public Instrument get(@RequestParam int instrumentId)
	{
		logger.info("Received request get the instrument with instrument Id: " + instrumentId);
		return new Instrument();
	}

	@CrossOrigin
	@RequestMapping(value = "/getAll", method = RequestMethod.GET)
	public List<Instrument> get()
	{
		logger.info("Received request get all instruments.");
		return new ArrayList<>();
	}

	@CrossOrigin
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public void update(@RequestBody Instrument instrument)
	{
		logger.info("Received request to update instrument: " + instrument);
	}

	@CrossOrigin
	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public void uploadInstruments(@RequestParam String uploadFilePath)
	{
		if(uploadFilePath == null || uploadFilePath.isEmpty())
		{
			logger.error("file request parameter cannot be null or empty");
			throw new IllegalArgumentException("file request parameter cannot be null or empty");
		}

		instrumentService.upload(uploadFilePath);

		logger.info("Received request to upload instruments: " + uploadFilePath);
	}
}
