package com.github.salilvnair.stockexchange.country.india.nse.model;

import java.util.List;

public class TopGainersOrLosers {
    private List<MarketData> data;

    public List<MarketData> getData() {
        return data;
    }

    public void setData(List<MarketData> data) {
        this.data = data;
    }

    public List<MarketData> data() {
        return data;
    }
}
