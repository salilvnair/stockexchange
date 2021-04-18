package com.github.salilvnair.stockexchange.country.india.nse.model;

public class PriceToEarningInfo {
    private String date;
    private String symbol;
    private String PE;
    private String sectorPE;
    private String sector;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPE() {
        return PE;
    }

    public void setPE(String PE) {
        this.PE = PE;
    }

    public String getSectorPE() {
        return sectorPE;
    }

    public void setSectorPE(String sectorPE) {
        this.sectorPE = sectorPE;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    @Override
    public String toString() {
        return "PriceToEarning{" +
                "date='" + date + '\'' +
                ", symbol='" + symbol + '\'' +
                ", PE='" + PE + '\'' +
                ", sectorPE='" + sectorPE + '\'' +
                ", sector='" + sector + '\'' +
                '}';
    }
}
