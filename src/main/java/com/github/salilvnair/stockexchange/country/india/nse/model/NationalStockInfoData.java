package com.github.salilvnair.stockexchange.country.india.nse.model;

public class NationalStockInfoData {
    private String symbol;
    private String companyName;
    private String open;
    private String dayHigh;
    private String dayLow;
    private String closePrice;
    private String previousClose;
    private String high52;
    private String low52;

    public String getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = closePrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getDayHigh() {
        return dayHigh;
    }

    public void setDayHigh(String dayHigh) {
        this.dayHigh = dayHigh;
    }

    public String getDayLow() {
        return dayLow;
    }

    public void setDayLow(String dayLow) {
        this.dayLow = dayLow;
    }

    public String getPreviousClose() {
        return previousClose;
    }

    public void setPreviousClose(String previousClose) {
        this.previousClose = previousClose;
    }

    public String getLow52() {
        return low52;
    }

    public void setLow52(String low52) {
        this.low52 = low52;
    }

    public String getHigh52() {
        return high52;
    }

    public void setHigh52(String high52) {
        this.high52 = high52;
    }

    @Override
    public String toString() {
        return "NationalStockInfoData{" +
                "symbol='" + symbol + '\'' +
                ", companyName='" + companyName + '\'' +
                ", open='" + open + '\'' +
                ", dayHigh='" + dayHigh + '\'' +
                ", dayLow='" + dayLow + '\'' +
                ", closePrice='" + closePrice + '\'' +
                ", previousClose='" + previousClose + '\'' +
                ", high52='" + high52 + '\'' +
                ", low52='" + low52 + '\'' +
                '}';
    }
}
