package com.github.salilvnair.stockexchange.service;

import com.github.salilvnair.stockexchange.country.india.nse.model.PriceToEarningInfo;
import com.github.salilvnair.stockexchange.type.MarketStatus;
import com.github.salilvnair.stockexchange.country.india.nse.type.MarketType;

import java.util.List;
import java.util.Map;

public interface StockExchange {

    MarketStatus marketStatus();

    <T> T stockInfo(String symbol, Class<T> infoClass);

    <T> T preOpenMarketInfo(MarketType marketType, Class<T> infoClass);

    <T> List<T> search(String searchString, Class<T> resultClass);

    <T> T topGainers(Class<T> resultClass);

    <T> T topLosers(Class<T> resultClass);

    <T> T yearHigh(Class<T> resultClass);

    <T> T yearLow(Class<T> resultClass);

    Map<String, PriceToEarningInfo> peDetails();

}
