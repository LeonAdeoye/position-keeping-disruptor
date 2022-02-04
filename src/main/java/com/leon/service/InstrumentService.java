package com.leon.service;

import com.leon.model.Instrument;
import java.util.Optional;

public interface InstrumentService
{
	void put(int instrumentId, Instrument instrument);
	Optional<Instrument> get(int instrumentId);
	Optional<Instrument> get(String bloombergCode);
	public void upload(String uploadFilePath);
}
