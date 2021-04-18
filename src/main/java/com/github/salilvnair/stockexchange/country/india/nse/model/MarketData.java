
package com.github.salilvnair.stockexchange.country.india.nse.model;

import com.google.gson.annotations.SerializedName;


public class MarketData {

    @SerializedName("symbol")
    private String symbol;
    @SerializedName("series")
    private String series;
    @SerializedName("xDt")
    private String xDt;
    @SerializedName("caAct")
    private String caAct;
    @SerializedName("iep")
    private String iep;
    @SerializedName("chn")
    private String change;
    @SerializedName("perChn")
    private String changePercentage;
    @SerializedName("pCls")
    private String previousClose;
    @SerializedName("trdQnty")
    private String trdQnty;
    @SerializedName("iVal")
    private String iVal;
    @SerializedName("mktCap")
    private String mktCap;
    @SerializedName("yHigh")
    private String high52Week;
    @SerializedName("yLow")
    private String low52Week;
    @SerializedName("sumVal")
    private String sumVal;
    @SerializedName("sumQnty")
    private String sumQnty;
    @SerializedName("finQnty")
    private String finQnty;
    @SerializedName("sumfinQnty")
    private String sumfinQnty;
    @SerializedName("openPrice")
    private String openPrice;
    @SerializedName("highPrice")
    private String highPrice;
    @SerializedName("lowPrice")
    private String lowPrice;
    @SerializedName("ltp")
    private String ltp;
    @SerializedName("previousPrice")
    private String previousPrice;
    @SerializedName("netPrice")
    private String netPrice;
    @SerializedName("tradedQuantity")
    private String tradedQuantity;
    @SerializedName("turnoverInLakhs")
    private String turnoverInLakhs;
    @SerializedName("lastCorpAnnouncementDate")
    private String lastCorpAnnouncementDate;
    @SerializedName("lastCorpAnnouncement")
    private String lastCorpAnnouncement;

    @SerializedName("symbol")
    public String getSymbol() {
        return symbol;
    }

    @SerializedName("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @SerializedName("series")
    public String getSeries() {
        return series;
    }

    @SerializedName("series")
    public void setSeries(String series) {
        this.series = series;
    }

    @SerializedName("xDt")
    public String getXDt() {
        return xDt;
    }

    @SerializedName("xDt")
    public void setXDt(String xDt) {
        this.xDt = xDt;
    }

    @SerializedName("caAct")
    public String getCaAct() {
        return caAct;
    }

    @SerializedName("caAct")
    public void setCaAct(String caAct) {
        this.caAct = caAct;
    }

    @SerializedName("iep")
    public String getIep() {
        return iep;
    }

    @SerializedName("iep")
    public void setIep(String iep) {
        this.iep = iep;
    }

    @SerializedName("chn")
    public String getChange() {
        return change;
    }

    @SerializedName("chn")
    public void setChange(String change) {
        this.change = change;
    }

    @SerializedName("perChn")
    public String getChangePercentage() {
        return changePercentage;
    }

    @SerializedName("perChn")
    public void setChangePercentage(String changePercentage) {
        this.changePercentage = changePercentage;
    }

    @SerializedName("pCls")
    public String getPreviousClose() {
        return previousClose;
    }

    @SerializedName("pCls")
    public void setPreviousClose(String pCls) {
        this.previousClose = pCls;
    }

    @SerializedName("trdQnty")
    public String getTrdQnty() {
        return trdQnty;
    }

    @SerializedName("trdQnty")
    public void setTrdQnty(String trdQnty) {
        this.trdQnty = trdQnty;
    }

    @SerializedName("iVal")
    public String getIVal() {
        return iVal;
    }

    @SerializedName("iVal")
    public void setIVal(String iVal) {
        this.iVal = iVal;
    }

    @SerializedName("mktCap")
    public String getMktCap() {
        return mktCap;
    }

    @SerializedName("mktCap")
    public void setMktCap(String mktCap) {
        this.mktCap = mktCap;
    }

    @SerializedName("yHigh")
    public String getHigh52Week() {
        return high52Week;
    }

    @SerializedName("yHigh")
    public void setHigh52Week(String yHigh) {
        this.high52Week = yHigh;
    }

    @SerializedName("yLow")
    public String getLow52Week() {
        return low52Week;
    }

    @SerializedName("yLow")
    public void getLow52Week(String yLow) {
        this.low52Week = yLow;
    }

    @SerializedName("sumVal")
    public String getSumVal() {
        return sumVal;
    }

    @SerializedName("sumVal")
    public void setSumVal(String sumVal) {
        this.sumVal = sumVal;
    }

    @SerializedName("sumQnty")
    public String getSumQnty() {
        return sumQnty;
    }

    @SerializedName("sumQnty")
    public void setSumQnty(String sumQnty) {
        this.sumQnty = sumQnty;
    }

    @SerializedName("finQnty")
    public String getFinQnty() {
        return finQnty;
    }

    @SerializedName("finQnty")
    public void setFinQnty(String finQnty) {
        this.finQnty = finQnty;
    }

    @SerializedName("sumfinQnty")
    public String getSumfinQnty() {
        return sumfinQnty;
    }

    @SerializedName("sumfinQnty")
    public void setSumfinQnty(String sumfinQnty) {
        this.sumfinQnty = sumfinQnty;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getLtp() {
        return ltp;
    }

    public void setLtp(String ltp) {
        this.ltp = ltp;
    }

    public String getPreviousPrice() {
        return previousPrice;
    }

    public void setPreviousPrice(String previousPrice) {
        this.previousPrice = previousPrice;
    }

    public String getNetPrice() {
        return netPrice;
    }

    public void setNetPrice(String netPrice) {
        this.netPrice = netPrice;
    }

    public String getTradedQuantity() {
        return tradedQuantity;
    }

    public void setTradedQuantity(String tradedQuantity) {
        this.tradedQuantity = tradedQuantity;
    }

    public String getTurnoverInLakhs() {
        return turnoverInLakhs;
    }

    public void setTurnoverInLakhs(String turnoverInLakhs) {
        this.turnoverInLakhs = turnoverInLakhs;
    }

    public String getLastCorpAnnouncementDate() {
        return lastCorpAnnouncementDate;
    }

    public void setLastCorpAnnouncementDate(String lastCorpAnnouncementDate) {
        this.lastCorpAnnouncementDate = lastCorpAnnouncementDate;
    }

    public String getLastCorpAnnouncement() {
        return lastCorpAnnouncement;
    }

    public void setLastCorpAnnouncement(String lastCorpAnnouncement) {
        this.lastCorpAnnouncement = lastCorpAnnouncement;
    }

    @Override
    public String toString() {
        return "MarketData{" +
                "symbol='" + symbol + '\'' +
                ", iep='" + iep + '\'' +
                ", change='" + change + '\'' +
                ", changePercentage='" + changePercentage + '\'' +
                ", previousClose='" + previousClose + '\'' +
                ", iVal='" + iVal + '\'' +
                ", high52Week='" + high52Week + '\'' +
                ", low52Week='" + low52Week + '\'' +
                ", sumVal='" + sumVal + '\'' +
                ", openPrice='" + openPrice + '\'' +
                ", highPrice='" + highPrice + '\'' +
                ", lowPrice='" + lowPrice + '\'' +
                ", ltp='" + ltp + '\'' +
                ", previousPrice='" + previousPrice + '\'' +
                ", netPrice='" + netPrice + '\'' +
                '}';
    }
}
