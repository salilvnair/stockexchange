
package com.github.salilvnair.stockexchange.country.india.nse.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "declines",
    "noChange",
    "data",
    "advances"
})
public class PreOpenMarketInfo {

    @JsonProperty("declines")
    private Integer declines;
    @JsonProperty("noChange")
    private Integer noChange;
    @JsonProperty("data")
    private List<MarketData> data = null;
    @JsonProperty("advances")
    private Integer advances;

    @JsonProperty("declines")
    public Integer getDeclines() {
        return declines;
    }

    @JsonProperty("declines")
    public void setDeclines(Integer declines) {
        this.declines = declines;
    }

    @JsonProperty("noChange")
    public Integer getNoChange() {
        return noChange;
    }

    @JsonProperty("noChange")
    public void setNoChange(Integer noChange) {
        this.noChange = noChange;
    }

    @JsonProperty("data")
    public List<MarketData> getData() {
        return data;
    }

    public List<MarketData> data() {
        return data;
    }

    @JsonProperty("data")
    public void setData(List<MarketData> data) {
        this.data = data;
    }

    @JsonProperty("advances")
    public Integer getAdvances() {
        return advances;
    }

    @JsonProperty("advances")
    public void setAdvances(Integer advances) {
        this.advances = advances;
    }

}
