package com.github.salilvnair.stockexchange.test;

import com.github.salilvnair.stockexchange.country.india.nse.NationalStockExchange;
import com.github.salilvnair.stockexchange.country.india.nse.model.*;
import com.github.salilvnair.stockexchange.country.india.nse.type.MarketType;
import com.github.salilvnair.stockexchange.country.india.nse.type.Company;
import com.github.salilvnair.stockexchange.service.StockExchange;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TestStockExchange {
    public static void main(String[] args) {
        StockExchange exchange = new NationalStockExchange();
        NationalStockInfo nationalStockInfo = exchange.stockInfo(Company.TITAN.symbol(), NationalStockInfo.class);
        System.out.println(nationalStockInfo.data());
//
//
//        List<NationalStockSearchInfo> searchInfoList = exchange.search("NIFTY", NationalStockSearchInfo.class);
//        System.out.println(searchInfoList);
        PreOpenMarketInfo marketInfo = exchange.preOpenMarketInfo(MarketType.NIFTY, PreOpenMarketInfo.class);
        System.out.println("Advances:"+marketInfo.getAdvances()+ " Declines:" + marketInfo.getDeclines());

        List<MarketData> watchList = marketInfo.data()
                .stream()
                .filter(marketData -> Company.TITAN.symbol().equals(marketData.getSymbol()))
                .collect(Collectors.toList());

        System.out.println(watchList);


//        TopGainersOrLosers topGainers = exchange.topGainers(TopGainersOrLosers.class);
//        List<String> gainerData = topGainers.data().stream().map(gainer -> gainer.getSymbol() + ":" + gainer.getNetPrice()+"%").collect(Collectors.toList());
//
//        TopGainersOrLosers topLosers = exchange.topLosers(TopGainersOrLosers.class);
//        List<String> losersData = topLosers.data().stream().map(loser -> loser.getSymbol() + ":" + loser.getNetPrice()+"%").collect(Collectors.toList());
//
//        System.out.println("Gainers:"+gainerData+ " Losers:" + losersData);

        Map<String, PriceToEarningInfo> peDetails = exchange.peDetails();
        System.out.println(peDetails.get(Company.TITAN.symbol()));
    }
}
