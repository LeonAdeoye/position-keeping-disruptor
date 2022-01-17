package com.leon.service;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class FxServiceImpl implements FxService
{
	private Map<String, Double> fxRatesMap = new HashMap<>();

	FxServiceImpl() {}

	@Override
	public void put(String currency, double fxRateAgainstOneUSD)
	{
		fxRatesMap.put(currency, Double.valueOf(fxRateAgainstOneUSD));
	}

	@Override
	public Optional<Double> get(String currency)
	{
		return Optional.ofNullable(fxRatesMap.get(currency));
	}
}
