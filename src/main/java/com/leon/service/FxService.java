package com.leon.service;

import com.leon.model.FxRate;
import java.util.Optional;

public interface FxService
{
	void put(String currency, double fxRateAgainstOneUSD);
	Optional<FxRate> get(String currency);
	void upload(String uploadFilePath);
}
