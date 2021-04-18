package com.github.salilvnair.stockexchange.country.india.nse.model;

import java.util.List;

public class NationalStockInfo {
    private String tradedDate;
    private List<NationalStockInfoData> data;
    private String lastUpdateTime;

    public List<NationalStockInfoData> getData() {
        return data;
    }

    public List<NationalStockInfoData> data() {
        return data;
    }

    public void setData(List<NationalStockInfoData> data) {
        this.data = data;
    }

    public String getTradedDate() {
        return tradedDate;
    }

    public void setTradedDate(String tradedDate) {
        this.tradedDate = tradedDate;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
