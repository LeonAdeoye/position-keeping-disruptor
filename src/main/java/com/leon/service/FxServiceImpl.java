package com.leon.service;

import com.leon.model.FxRate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FxServiceImpl implements FxService
{
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

	public List<FxRate> getAll()
	{
		return fxRatesMap.values().stream().collect(Collectors.toList());
	}
}
