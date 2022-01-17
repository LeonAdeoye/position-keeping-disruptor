package com.leon.service;

import java.util.Optional;

public interface FxService
{
	void put(String currency, double fxRateAgainstOneUSD);
	Optional<Double> get(String currency);
}
