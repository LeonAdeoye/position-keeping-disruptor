package com.leon.service;

import com.leon.model.Instrument;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class InstrumentServiceImpl implements InstrumentService
{
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
		return instrumentMap.values().stream().
				filter(instrument -> instrument.getBloombergCode().equals(bloombergCode)).
				findFirst();
	}
}
