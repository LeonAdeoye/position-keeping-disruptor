package com.leon.service;

import com.leon.model.Instrument;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class InstrumentServiceImpl
{
	Map<Integer, Instrument> instrumentMap = new HashMap<>();

	InstrumentServiceImpl()
	{

	}

	public void put(int instrumentId, Instrument instrument)
	{
		instrumentMap.put(instrumentId, instrument);
	}

	public Optional<Instrument> get(int instrumentId)
	{
		Instrument result = instrumentMap.get(instrumentId);
		if(result == null)
			return Optional.empty();
		else
			return Optional.of(result);
	}

	public Optional<Instrument> get(String bloombergCode)
	{
		Optional<Instrument> matchingInstrument = instrumentMap.values().stream().
				filter(instrument -> instrument.getBloombergCode().equals(bloombergCode)).
				findFirst();

		return matchingInstrument;
	}
}
