package com.github.salilvnair.stockexchange.country.india.nse;

import com.github.salilvnair.stockexchange.country.india.nse.model.PriceToEarningInfo;
import com.github.salilvnair.stockexchange.service.StockExchange;
import com.github.salilvnair.stockexchange.type.MarketStatus;
import com.github.salilvnair.stockexchange.country.india.nse.type.MarketType;
import com.github.salilvnair.utils.common.UrlEncoder;
import com.github.salilvnair.utils.rest.RestWsClientUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NationalStockExchange implements StockExchange {

    private RestWsClientUtil restWsClientUtil;

    public NationalStockExchange() {
        init();
    }

    @Override
    public MarketStatus marketStatus() {
        restWsClientUtil.setEndPointURL("https://www1.nseindia.com/live_market/dynaContent/live_watch/get_quote/ajaxGetQuoteJSON.jsp?series=EQ&symbol=TCS");
        String response = restWsClientUtil.sendRequest(null, true, RestWsClientUtil.GET_REQUEST);
        if(response!=null && response.contains("closed")){
            return MarketStatus.CLOSED;
        }
        return MarketStatus.OPEN;
    }

    @Override
    public <T> T stockInfo(String symbol, Class<T> infoClass) {
        restWsClientUtil.setEndPointURL("https://www1.nseindia.com/live_market/dynaContent/live_watch/get_quote/ajaxGetQuoteJSON.jsp?series=EQ&symbol="+symbol);
        String responseString = restWsClientUtil.sendRequest(null, true,  RestWsClientUtil.GET_REQUEST);
        T nationalStockInfo = new Gson().fromJson(responseString, infoClass);
        return nationalStockInfo;
    }

    @Override
    public <T> T preOpenMarketInfo(MarketType marketType, Class<T> infoClass) {
        restWsClientUtil.setEndPointURL("https://www1.nseindia.com/live_market/dynaContent/live_analysis/pre_open/"+marketType.name().toLowerCase()+".json");
        String responseString = restWsClientUtil.sendRequest(null, true,  RestWsClientUtil.GET_REQUEST);
        T nationalStockInfo = new Gson().fromJson(responseString, infoClass);
        return nationalStockInfo;
    }

    @Override
    public <T> List<T> search(String searchString, Class<T> resultClass) {
        searchString = UrlEncoder.encodeURIComponent(searchString);
        restWsClientUtil.setEndPointURL("https://www1.nseindia.com/live_market/dynaContent/live_watch/get_quote/ajaxCompanySearch.jsp?search="+searchString);
        String responseString = restWsClientUtil.sendRequest(null, true,  RestWsClientUtil.GET_REQUEST);
        responseString = searchTransformer(responseString);
        Type type = TypeToken.getParameterized(ArrayList.class, resultClass).getType();
        List<T>  searchList = new Gson().fromJson(responseString, type);
        return searchList;
    }

    @Override
    public <T> T topGainers(Class<T> resultClass) {
        restWsClientUtil.setEndPointURL("https://www1.nseindia.com/live_market/dynaContent/live_analysis/gainers/niftyGainers1.json");
        String responseString = restWsClientUtil.sendRequest(null, true,  RestWsClientUtil.GET_REQUEST);
        T nationalStockInfo = new Gson().fromJson(responseString, resultClass);
        return nationalStockInfo;
    }

    @Override
    public <T> T topLosers(Class<T> resultClass) {
        restWsClientUtil.setEndPointURL("https://www1.nseindia.com/live_market/dynaContent/live_analysis/losers/niftyLosers1.json");
        String responseString = restWsClientUtil.sendRequest(null, true,  RestWsClientUtil.GET_REQUEST);
        T nationalStockInfo = new Gson().fromJson(responseString, resultClass);
        return nationalStockInfo;
    }

    @Override
    public <T> T yearHigh(Class<T> resultClass) {
        return null;
    }

    @Override
    public <T> T yearLow(Class<T> resultClass) {
        return null;
    }

    @Override
    public Map<String, PriceToEarningInfo> peDetails() {
        restWsClientUtil.setEndPointURL("https://www1.nseindia.com/homepage/peDetails.json");
        String responseString = restWsClientUtil.sendRequest(null, true,  RestWsClientUtil.GET_REQUEST);
        Type type = new TypeToken<Map<String, PriceToEarningInfo>>(){}.getType();
        return new Gson().fromJson(responseString, type);
    }

    //private helpers

    private void init() {
        initRestWsClient();
    }

    private void initRestWsClient() {
        restWsClientUtil = new RestWsClientUtil();
        restWsClientUtil.setLogRequestResponse(false);
        MultivaluedMap<String, Object> requestHeaderMap = new MultivaluedHashMap<>();
        requestHeaderMap.putSingle("Referer","https://www1.nseindia.com");
        requestHeaderMap.putSingle("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.2 Safari/605.1.15");
        requestHeaderMap.putSingle("Accept-Encoding", "gzip,deflate");
        requestHeaderMap.putSingle("Accept", "*/*");
        requestHeaderMap.putSingle("Host", "www1.nseindia.com");
        requestHeaderMap.putSingle("User-Agent", "Mozilla/5.0");
        restWsClientUtil.setRequestHeaderMap(requestHeaderMap);
        restWsClientUtil.responseHasGzipEncoding();
    }

    private String searchTransformer(String data) {
        Pattern dPattern = Pattern.compile("<li>(.*?)<\\/li>");
        Matcher matcher = dPattern.matcher(data);
        String[] arr = {"[","","]"};
        matcher.results().forEach(result -> {
            Pattern pattern = Pattern.compile("symbol=(.*?)&");
            String listItem = result.group(1);
            Matcher newMatcher  = pattern.matcher(listItem);
            String symbol = "";
            while(newMatcher.find()) {
                symbol = newMatcher.group(1);
                listItem = stripTags(listItem).replace(symbol, "");
            }
            arr[1] = arr[1]+ "{\"name\":\""+listItem+"\",\"symbol\":\""+symbol+"\"},";
        });
        arr[1] = arr[1].replaceAll(",$","");
        return arr[0]+arr[1]+arr[2];
    }

    private String stripTags(String data) {
        return data.replaceAll("<(.|\n)*?>", "").trim();
    }
}
