package com.leon.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.model.Instrument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InstrumentServiceImpl implements InstrumentService
{
	private static final Logger logger = LoggerFactory.getLogger(InstrumentServiceImpl.class);
	Map<Integer, Instrument> instrumentMap = new HashMap<>();

	InstrumentServiceImpl()
	{

	}

	@Override
	public void put(int instrumentId, Instrument instrument)
	{
		instrumentMap.put(instrumentId, instrument);
	}

	@Override
	public Optional<Instrument> get(int instrumentId)
	{
		return Optional.ofNullable(instrumentMap.get(instrumentId));
	}

	@Override
	public Optional<Instrument> get(String bloombergCode)
	{
		return instrumentMap.values().stream().filter(instrument -> instrument.getBloombergCode().equals(bloombergCode)).findFirst();
	}

	@Override
	public void upload(String uploadFilePath)
	{
		if(Files.exists(Paths.get(uploadFilePath)))
			logger.info("Uploading instruments from file: " + uploadFilePath);
		else
			logger.info("Instruments file does NOT exist: " + uploadFilePath);

		try
		{
			instrumentMap = new ObjectMapper().readValue(new File(uploadFilePath), new TypeReference<List<Instrument>>(){})
					.stream().collect(Collectors.toMap(instrument -> instrument.getInstrumentId(), instrument -> instrument));
		}
		catch(IOException ioe)
		{
			logger.error(ioe.getLocalizedMessage());
		}
	}
}
