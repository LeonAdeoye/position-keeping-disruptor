package com.leon.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leon.model.FxRate;
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
public class FxServiceImpl implements FxService
{
	private static final Logger logger = LoggerFactory.getLogger(FxServiceImpl.class);
	private Map<String, FxRate> fxRatesMap = new HashMap<>();
	public static FxRate defaultUSDRate = new FxRate("USD", 1.0);

	FxServiceImpl() {}

	@Override
	public void put(String currency, double fxRateAgainstOneUSD)
	{
		fxRatesMap.put(currency, new FxRate(currency, fxRateAgainstOneUSD));
	}

	@Override
	public Optional<FxRate> get(String currency)
	{
		return Optional.ofNullable(fxRatesMap.get(currency));
	}

	@Override
	public void upload(String uploadFilePath)
	{
		if(Files.exists(Paths.get(uploadFilePath)))
			logger.info("Uploading FX rates from file: " + uploadFilePath);
		else
			logger.info("FX rates file does NOT exist: " + uploadFilePath);

		try
		{
			fxRatesMap = new ObjectMapper().readValue(new File(uploadFilePath), new TypeReference<List<FxRate>>(){})
					.stream().collect(Collectors.toMap(fxRate -> fxRate.getCurrency(), fxRate -> fxRate));
		}
		catch(IOException ioe)
		{
			logger.error(ioe.getLocalizedMessage());
		}
	}
}
