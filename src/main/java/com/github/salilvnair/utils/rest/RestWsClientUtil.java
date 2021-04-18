package com.github.salilvnair.utils.rest;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.message.GZipEncoder;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.net.ssl.*;
import javax.ws.rs.client.*;
import javax.ws.rs.core.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.security.KeyStore;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestWsClientUtil {
    protected final Log logger = LogFactory.getLog(RestWsClientUtil.class);
    private String mediaType=MediaType.APPLICATION_JSON;
    private String endPointURL="";
    private MultivaluedMap<String, Object> requestHeaderMap;
    private MultivaluedMap<String, Object> responseHeaderMap;
    private List<String> errorTypeList;
    private Map<String,List<Object>> responseErrorMap;
    private Response currentResponse;
    private String gsonDateFormat="E MMM dd hh:mm:ss Z yyyy";
    private Boolean prettyPrintOn=false;
    private Boolean serializeNulls=false;
    private Boolean disableHTMLEscaping=false;
    private String webServiceName="";
    private Boolean excludeEmpty=false;
    public static final String POST_REQUEST="POST";
    public static final String GET_REQUEST="GET";
    public static final String PUT_REQUEST="PUT";
    public static final String PATCH_REQUEST="PATCH";
    public static final String DELETE_REQUEST="DELETE";
    public static final String HTTP_REQUEST="REQUEST";
    public static final String HTTP_RESPONSE="RESPONSE";
    private boolean errorInResponse=false;
    private List<Object> errorCode;
    private Map<String, String> queryParams;
    private List<String> pathParams;
    private boolean hasQueryParams;
    private boolean hasPathParams;
    public static final String HTTP_AUTHENTICATION_BASIC_USERNAME="HTTP_AUTH_BASIC_USR";
    public static final String HTTP_AUTHENTICATION_BASIC_PWD="HTTP_AUTH_BASIC_PWD";
    public static final String CONNECTION_TIME_OUT="connectionTimeOut";
    public static final String RESPONSE_TIME_OUT="responseTimeOut";
    private long responseTime=0;
    private boolean httpsDefaultTLSv2=false;
    private boolean hasCustomCofiguration=false;
    private boolean hasDefaultCofiguration=false;
    private ClientConfig clientConfiguration;
    private boolean logRequestResponse=true;
    private boolean logResponseErrors=true;
    private boolean securedClientPolicy=false;
    private boolean userDefinedKeyStore=false;
    private KeyStore keyStore;
    private boolean useJackson=false;
    private boolean responseHasGzipEncoding;


    public MultivaluedMap<String, Object> getRequestHeaderMap() {
        return requestHeaderMap;
    }

    public void setRequestHeaderMap(MultivaluedMap<String, Object> requestHeaderMap) {
        this.requestHeaderMap = requestHeaderMap;
    }

    public <T> Response sendRequest(Object entityRequestBean, String requestType){
        Client client = registerClient();
        WebTarget webTarget = client.target(getEndPointURL());
        webTarget=setURLParams(webTarget);
        if(responseHasGzipEncoding) {
            webTarget.register(GZipEncoder.class);
        }
        Invocation.Builder invocationBuilder =  webTarget.request(getMediaType());
        if(requestHeaderMap!=null){
            invocationBuilder.headers(getRequestHeaderMap());
        }
        if(excludeEmpty){
            entityRequestBean=serialiazeWithGson(entityRequestBean);
        }
        printLoggers(entityRequestBean,requestType+" "+HTTP_REQUEST);
        Response response = null;
        long startTime = System.currentTimeMillis();
        if(POST_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.post(Entity.entity(entityRequestBean, getMediaType()));
        }
        else if(PUT_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.put(Entity.entity(entityRequestBean, getMediaType()));
        }
        else if(GET_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.get();
        }
        else if(DELETE_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.delete();
        }
        else if (PATCH_REQUEST.equalsIgnoreCase(requestType)) {
            if(requestHeaderMap!=null){
                response = webTarget.request().headers(getRequestHeaderMap()).method(PATCH_REQUEST,Entity.entity(entityRequestBean, getMediaType()));
            }
            else {
                response = webTarget.request().method(PATCH_REQUEST, Entity.entity(entityRequestBean, getMediaType()));
            }
        }
        setCurrentResponse(response);
        if(response!=null){
            populateResponseHeaders(response);
            prepareResponseErrorMap(response);
            printErrors(response);
        }
        long elapsedTime = System.currentTimeMillis() - startTime;
        responseTime=elapsedTime;
        return response;
    }

    public <T> String sendRequest(Object entityRequestBean, boolean responseAsString, String requestType){
        String responseString = new String();
        Client client = registerClient();
        WebTarget webTarget = client.target(getEndPointURL());
        webTarget=setURLParams(webTarget);
        if(responseHasGzipEncoding) {
            webTarget.register(GZipEncoder.class);
        }
        Invocation.Builder invocationBuilder =  webTarget.request(getMediaType());
        if(requestHeaderMap!=null){
            invocationBuilder.headers(getRequestHeaderMap());
            if(logRequestResponse){
                logger.info("Request Headers["+getRequestHeaderMap().toString()+"]");
            }
        }
        if(excludeEmpty){
            entityRequestBean=serialiazeWithGson(entityRequestBean);
        }
        printLoggers(entityRequestBean,requestType+" "+HTTP_REQUEST);
        Response response = null;
        long startTime = System.currentTimeMillis();
        if(POST_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.post(Entity.entity(entityRequestBean, getMediaType()));
        }
        else if(PUT_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.put(Entity.entity(entityRequestBean, getMediaType()));
        }
        else if(GET_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.get();
        }
        else if(DELETE_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.delete();
        }
        else if (PATCH_REQUEST.equalsIgnoreCase(requestType)) {
            if(requestHeaderMap!=null){
                response = webTarget.request().headers(getRequestHeaderMap()).method(PATCH_REQUEST,Entity.entity(entityRequestBean, getMediaType()));
            }
            else {
                response = webTarget.request().method(PATCH_REQUEST, Entity.entity(entityRequestBean, getMediaType()));
            }
        }
        setCurrentResponse(response);
        long elapsedTime = System.currentTimeMillis() - startTime;
        responseTime=elapsedTime;
        if(response!=null){
            printResponseHeaders(response);
            printErrors(response);
            populateResponseHeaders(response);
            prepareResponseErrorMap(response);
            if(response.hasEntity()){
                responseString=(String) response.readEntity(responseString.getClass());
            }
            if(logRequestResponse){
                logger.info("===================================================="+getWebServiceName()+" "+requestType+" "+HTTP_RESPONSE+" BEGINS=================================================");
                if(getPrettyPrintOn()){
                    logger.info(jsonStringPrettyPrint(responseString));
                }
                else{
                    logger.info(responseString);
                }

                logger.info("===================================================="+getWebServiceName()+" "+requestType+" "+HTTP_RESPONSE+" ENDS====================================================");
            }
        }
        return responseString;
    }

    @SuppressWarnings("unchecked")
    public <T> T sendRequest(Object entityRequestBean,T entityResponseBean,String requestType){
        Client client = registerClient();
        WebTarget webTarget = client.target(getEndPointURL());
        webTarget=setURLParams(webTarget);
        if(responseHasGzipEncoding) {
            webTarget.register(GZipEncoder.class);
        }
        Invocation.Builder invocationBuilder =  webTarget.request(getMediaType());
        if(requestHeaderMap!=null){
            invocationBuilder.headers(getRequestHeaderMap());
            if(logRequestResponse){
                logger.info("Request Headers["+getRequestHeaderMap().toString()+"]");
            }
        }
        if(excludeEmpty){
            entityRequestBean=serialiazeWithGson(entityRequestBean);
        }
        printLoggers(entityRequestBean,requestType+" "+HTTP_REQUEST);
        Response response = null;
        long startTime = System.currentTimeMillis();
        if(POST_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.post(Entity.entity(entityRequestBean, getMediaType()));
        }
        else if(PUT_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.put(Entity.entity(entityRequestBean, getMediaType()));
        }
        else if(GET_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.get();
        }
        else if(DELETE_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.delete();
        }
        else if (PATCH_REQUEST.equalsIgnoreCase(requestType)) {
            if(requestHeaderMap!=null){
                response = webTarget.request().headers(getRequestHeaderMap()).method(PATCH_REQUEST,Entity.entity(entityRequestBean, getMediaType()));
            }
            else {
                response = webTarget.request().method(PATCH_REQUEST, Entity.entity(entityRequestBean, getMediaType()));
            }
        }
        setCurrentResponse(response);
        long elapsedTime = System.currentTimeMillis() - startTime;
        responseTime=elapsedTime;
        if(response!=null){
            printResponseHeaders(response);
            printErrors(response);
            populateResponseHeaders(response);
            prepareResponseErrorMap(response);
            if(response.hasEntity()){
                try{
                    entityResponseBean=(T) response.readEntity(entityResponseBean.getClass());
                }
                catch(Exception ex){
                    logger.info("RestWSClientUtil>>Got Exception["+ex+"]");
                }
            }
            printLoggers(entityResponseBean,requestType+" "+HTTP_RESPONSE);
        }
        return entityResponseBean;
    }

    @SuppressWarnings("unchecked")
    public <T> T sendRequest(Object entityRequestBean, T entityResponseBean, String requestType, GenericType<?> genericType){
        Client client = registerClient();
        WebTarget webTarget = client.target(getEndPointURL());
        webTarget=setURLParams(webTarget);
        if(responseHasGzipEncoding) {
            webTarget.register(GZipEncoder.class);
        }
        Invocation.Builder invocationBuilder =  webTarget.request(getMediaType());
        if(requestHeaderMap!=null){
            invocationBuilder.headers(getRequestHeaderMap());
            if(logRequestResponse){
                logger.info("Request Headers["+getRequestHeaderMap().toString()+"]");
            }
        }
        if(excludeEmpty){
            entityRequestBean=serialiazeWithGson(entityRequestBean);
        }
        printLoggers(entityRequestBean,requestType+" "+HTTP_REQUEST);
        Response response = null;
        if(POST_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.post(Entity.entity(entityRequestBean, getMediaType()));
        }
        else if(PUT_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.put(Entity.entity(entityRequestBean, getMediaType()));
        }
        else if(GET_REQUEST.equalsIgnoreCase(requestType)){
            response=invocationBuilder.get(Response.class);
        }
        else if(DELETE_REQUEST.equalsIgnoreCase(requestType)){
            response = invocationBuilder.delete(Response.class);
        }
        else if (PATCH_REQUEST.equalsIgnoreCase(requestType)) {
            if(requestHeaderMap!=null){
                response = webTarget.request().headers(getRequestHeaderMap()).method(PATCH_REQUEST,Entity.entity(entityRequestBean, getMediaType()));
            }
            else {
                response = webTarget.request().method(PATCH_REQUEST, Entity.entity(entityRequestBean, getMediaType()));
            }
        }
        setCurrentResponse(response);
        if(response!=null){
            printResponseHeaders(response);
            printErrors(response);
            populateResponseHeaders(response);
            prepareResponseErrorMap(response);
            if(response.hasEntity()){
                try{
                    entityResponseBean=(T) response.readEntity(genericType);
                }
                catch(Exception ex){
                    logger.info("RestWSClientUtil>>Got Exception["+ex+"]");
                }
            }
            printLoggers(entityResponseBean,requestType+" "+HTTP_RESPONSE);
        }
        return entityResponseBean;
    }

    private void populateResponseHeaders(Response response) {
        responseHeaderMap = new MultivaluedHashMap<>();
        responseHeaderMap.putAll(response.getHeaders());
    }

    private void prepareResponseErrorMap(Response response) {
        if(getErrorTypeList()!=null && !getErrorTypeList().isEmpty()){
            for(String errorKey:getErrorTypeList()){
                if(response.getHeaders().containsKey(errorKey.trim())){
                    if(responseErrorMap==null) {
                        responseErrorMap = new HashMap<>();
                    }
                    setErrorInResponse(true);
                    responseErrorMap.put(errorKey,response.getHeaders().get(errorKey.trim()));
                }
            }
        }
    }

    private Client registerClient(){
        Client client=ClientBuilder.newClient();
        ClientConfig clientConfig = new ClientConfig();

        //append if any other ClientConfiguration is needed.
        // and make the hasDefaultCofiguration=true in the block

        if(requestHeaderMap!=null && requestHeaderMap.containsKey(HTTP_AUTHENTICATION_BASIC_USERNAME)){
            clientConfig=registerHttpAuthenticationFeature(clientConfig);
            hasDefaultCofiguration=true;
        }

        if(requestHeaderMap!=null && (requestHeaderMap.containsKey(CONNECTION_TIME_OUT)||requestHeaderMap.containsKey(RESPONSE_TIME_OUT))){
            clientConfig=registerTimeout(clientConfig);
            hasDefaultCofiguration=true;
        }
        if(httpsDefaultTLSv2 || securedClientPolicy){
            hasDefaultCofiguration=true;
        }


        //DONOT change anything below
        if(hasCustomCofiguration){
            client = registerClientConfiguration(clientConfiguration);
        }
        else if(hasDefaultCofiguration){
            if(httpsDefaultTLSv2 && securedClientPolicy){
                client=setSecuredHttpClientPolicy(clientConfig);
            }
            else if(httpsDefaultTLSv2){
                client=setHttpsDefaultTLSv2(clientConfig);
            }
            else if(securedClientPolicy){
                client=setSecuredHttpClientPolicy(clientConfig);
            }
            else{
                client = ClientBuilder.newClient(clientConfig);
            }
        }

        return client;
    }

    private Client registerClientConfiguration(ClientConfig clientConfig){
        Client client = null;
        if(requestHeaderMap!=null && requestHeaderMap.containsKey(HTTP_AUTHENTICATION_BASIC_USERNAME)){
            clientConfig=registerHttpAuthenticationFeature(clientConfig);
        }
        if(requestHeaderMap!=null && (requestHeaderMap.containsKey(CONNECTION_TIME_OUT)||requestHeaderMap.containsKey(RESPONSE_TIME_OUT))){
            clientConfig=registerTimeout(clientConfig);
        }

        if(httpsDefaultTLSv2 && securedClientPolicy){
            client=setSecuredHttpClientPolicy(clientConfig);
        }
        else if(httpsDefaultTLSv2){
            client=setHttpsDefaultTLSv2(clientConfig);
        }
        else if(securedClientPolicy){
            client=setSecuredHttpClientPolicy(clientConfig);
        }
        else{
            client = ClientBuilder.newClient(clientConfig);
        }
        return client;
    }

    public void ignoreUnknownProperties() {
        final JacksonJsonProvider jacksonJsonProvider = new JacksonJaxbJsonProvider().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ClientConfig clientConfig = new ClientConfig(jacksonJsonProvider);
        setClientConfiguration(clientConfig);
    }

    public void setClientConfiguration(ClientConfig clientConfig) {
        hasCustomCofiguration=true;
        this.clientConfiguration = clientConfig;
    }

    public String generateBasicAuthorization(String userName, String pwd) {
        String basicAuthorizationValue = null;
        if (userName != null && pwd != null) {
            String authenticationString = userName + ":" + pwd;
            basicAuthorizationValue = "Basic " + new String(Base64.encodeBase64(authenticationString.getBytes()));
        }
        return basicAuthorizationValue;
    }

    private ClientConfig registerHttpAuthenticationFeature(ClientConfig clientConfig){
        String httpAuthenticationUserName=(String) requestHeaderMap.getFirst(HTTP_AUTHENTICATION_BASIC_USERNAME);
        String httpAuthenticationPwd=(String) requestHeaderMap.getFirst(HTTP_AUTHENTICATION_BASIC_PWD);
        //setting preemptive authentication way i.e. information is send always with each HTTP request.
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(httpAuthenticationUserName, httpAuthenticationPwd);
        return clientConfig.register(feature);
    }

    private ClientConfig registerTimeout(ClientConfig clientConfig){

        if(requestHeaderMap.containsKey(CONNECTION_TIME_OUT)){
            Object connectionTimeOut=(Object) requestHeaderMap.getFirst(CONNECTION_TIME_OUT);
            clientConfig.property(ClientProperties.CONNECT_TIMEOUT, connectionTimeOut);
        }

        if(requestHeaderMap.containsKey(RESPONSE_TIME_OUT)){
            Object requestTimeOut=(Object) requestHeaderMap.getFirst(RESPONSE_TIME_OUT);
            clientConfig.property(ClientProperties.READ_TIMEOUT, requestTimeOut);
        }
        return clientConfig;
    }

    private Client setHttpsDefaultTLSv2(ClientConfig clientConfig){
        SSLContext sslContext;
        try {
            ClientBuilder clientBuilder = ClientBuilder.newBuilder();
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            return clientBuilder.withConfig(clientConfig).sslContext(sslContext).build();
        }
        catch (Exception ex) {
            logger.error("RestWSClientUtil>>sendRequest>>httpsDefaultTLSv2>>caught exception:"+ex);
        }
        return ClientBuilder.newClient();
    }

    private Client setSecuredHttpClientPolicy(ClientConfig clientConfig){
        try{
            KeyStore trustStore=null;
            if(hasUserDefinedKeyStore()){
                trustStore = getUserDefinedKeyStore();
            }
            else{
                trustStore = getCsiCommonKeyStore();
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            if(httpsDefaultTLSv2){
                sslContext = SSLContext.getInstance("TLSv1.2");
            }
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(trustStore);
            TrustManager[] trustManagers=tmf.getTrustManagers();
            sslContext.init(null, trustManagers, null);
            return ClientBuilder.newBuilder().withConfig(clientConfig).hostnameVerifier(new TrustAllHostNameVerifier()).sslContext(sslContext).build();
        }
        catch(Exception ex){
            logger.error("RestWSClientUtil>>sendRequest>>setSecuredHttpClientPolicy>>caught exception:"+ex);
        }
        return ClientBuilder.newClient();
    }

    private KeyStore getCsiCommonKeyStore(){
        KeyStore trustStore = null;
        try{
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            String keyFile = System.getProperty("csi.common.keystore").trim();
            String jksValue=System.getProperty("keystore.pwd").trim();
            trustStore.load(new FileInputStream(keyFile),jksValue.toCharArray());
        }
        catch(Exception ex){
            logger.error("RestWSClientUtil>>sendRequest>>getCsiCommonKeyStore>>caught exception:"+ex);
        }

        return trustStore;
    }

    private KeyStore getUserDefinedKeyStore(){
        return getKeyStore();
    }

    public <T> void printLoggers(T entityBean,String wsType){
        if(logRequestResponse){
            if(MediaType.APPLICATION_JSON.equals(mediaType)){
                printJsonLoggers(entityBean,wsType);
            }
            else if(MediaType.APPLICATION_XML.equals(mediaType)){
                printXMLLoggers(entityBean,wsType);
            }
        }
    }

    public WebTarget applyQueryParams(WebTarget webTarget){
        if (queryParams != null && !queryParams.isEmpty()) {
            for (Map.Entry<String, String> entry : queryParams.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    if(logRequestResponse){
                        logger.info("queryParams: {"+entry.getKey()+"} = {"+ entry.getValue()+"}");
                    }
                    webTarget = webTarget.queryParam(entry.getKey(), entry.getValue());
                }
            }
        }
        return webTarget;
    }

    public WebTarget applyPathParams(WebTarget webTarget){
        if (pathParams != null && !pathParams.isEmpty()) {
            for (String path:pathParams) {
                if (!"".equals(path) && path!=null) {
                    if(logRequestResponse){
                        logger.info("pathParams: {"+path+"}");
                    }
                    webTarget = webTarget.path(path);
                }
            }
        }
        return webTarget;
    }

    private WebTarget setURLParams(WebTarget webTarget){
        if(hasPathParams){
            webTarget=applyPathParams(webTarget);
        }
        if(hasQueryParams){
            webTarget=applyQueryParams(webTarget);
        }
        return webTarget;
    }

    private void printErrors(Response response) {
        if(logResponseErrors){
            if(getErrorTypeList()!=null && getErrorTypeList().size()>0){
                for(String errorKey:getErrorTypeList()){
                    if(response.getHeaders().containsKey(errorKey.trim())){
                        setErrorInResponse(true);
                        logger.info(errorKey+" "+response.getHeaders().get(errorKey.trim()));
                        if(errorKey != null && errorKey == "errorCode"){
                            setErrorCode(response.getHeaders().get(errorKey.trim()));
                        }
                    }
                }
            }
        }
    }

    public void printResponseHeaders(Response response){
        MultivaluedMap<String,String> responseMap=getResponseHeaderMap(response);
        if(logRequestResponse){
            logger.info("Response Headers["+responseMap.toString()+"]");
        }
    }

    public MultivaluedMap<String,String> getResponseHeaderMap(Response response){
        MultivaluedHashMap<String,String> metaDataMap= new MultivaluedHashMap<String,String>();
        metaDataMap.putSingle("status", response.getStatus()+"");
        if(response.getStatusInfo()!=null){
            metaDataMap.putSingle("reason", response.getStatusInfo().getReasonPhrase()+"");
        }
        metaDataMap.putAll(response.getStringHeaders());
        return metaDataMap;
    }

    public <T> void printJsonLoggers(T entityBean,String wsType){
        String jsonString="";
        if(entityBean==null){
            jsonString="{}";
        }
        else{
            if(useJackson) {
                ObjectMapper mapper = new ObjectMapper();
                try {
                    jsonString=mapper.writeValueAsString(entityBean);
                } catch (Exception ex) {
                    logger.error("RestWSClientUtil>>printJsonLoggers>>useJackson>>caught exception:"+ex);
                }
            }
            else{
                if(getPrettyPrintOn()){
                    jsonString=jsonPrettyPrint(entityBean);
                }
                else{
                    jsonString=jsonPrint(entityBean);
                }
            }
        }
        logger.info("===================================================="+getWebServiceName()+" "+wsType+" BEGINS=================================================");
        logger.info(jsonString);
        logger.info("===================================================="+getWebServiceName()+" "+wsType+" ENDS====================================================");
    }

    public <T> void printXMLLoggers( T entityBean,String wsType){
        String xmlString = "";
        if(entityBean != null){
            try {
                JAXBContext jc = null;

                jc = JAXBContext.newInstance(entityBean.getClass());

                Marshaller reqMarshal = jc.createMarshaller();
                StringWriter sw = new StringWriter();
                reqMarshal.marshal(entityBean, sw);
                xmlString = sw.toString();
                if(getPrettyPrintOn()){
                    xmlString=xmlPrettyPrint(xmlString);
                }
                logger.info("===================================================="+getWebServiceName()+" "+wsType+" BEGINS=================================================");
                logger.info(xmlString);
                logger.info("===================================================="+getWebServiceName()+" "+wsType+"  ENDS===================================================");
                sw.close();

            } catch (JAXBException e) {
                logger.error("RestWSClientUtil>>printXMLLoggers>>JAXBException in marshelling as an XML string", e);
            } catch (IOException e) {
                logger.error("RestWSClientUtil>>printXMLLoggers>>IOException in marshelling as an XML string", e);
            }
        }

    }

    private <T> String jsonPrettyPrint(T entityBean){
        Gson gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).setPrettyPrinting().create();
        if(serializeNulls){
            gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).setPrettyPrinting().serializeNulls().create();
        }
        if(getDisableHTMLEscaping()){
            gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).setPrettyPrinting().disableHtmlEscaping().create();
        }
        if(getDisableHTMLEscaping() && serializeNulls){
            gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
        }
        String jsonPrettyString =gson.toJson(entityBean);
        return jsonPrettyString;
    }

    private <T> String jsonPrint(T entityBean){
        Gson gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).create();
        if(serializeNulls){
            gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).serializeNulls().create();
        }
        if(getDisableHTMLEscaping()){
            gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).disableHtmlEscaping().create();
        }
        if(getDisableHTMLEscaping() && serializeNulls){
            gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).serializeNulls().disableHtmlEscaping().create();
        }
        String jsonString =gson.toJson(entityBean);
        return jsonString;
    }

    private  String jsonStringPrettyPrint(String jsonString){
        JsonParser parser = new JsonParser();
        JsonObject json = parser.parse(jsonString).getAsJsonObject();

        Gson gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).setPrettyPrinting().create();
        if(serializeNulls){
            gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).setPrettyPrinting().serializeNulls().create();
        }
        if(getDisableHTMLEscaping()){
            gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).setPrettyPrinting().disableHtmlEscaping().create();
        }
        if(getDisableHTMLEscaping() && serializeNulls){
            gson = new GsonBuilder().setDateFormat(getGsonDateFormat()).setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
        }
        String prettyJson = gson.toJson(json);

        return prettyJson;
    }

    private String xmlPrettyPrint(String unformattedXml){
        try {
            Document document = parseXmlFile(unformattedXml);
            OutputFormat format = new OutputFormat(document);
            format.setIndenting(true);
            format.setIndent(3);
            format.setOmitXMLDeclaration(true);
            Writer out = new StringWriter();
            XMLSerializer serializer = new XMLSerializer(out, format);
            serializer.serialize(document);
            return out.toString();
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    private Document parseXmlFile(String in){
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        }catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }catch (SAXException e) {
            throw new RuntimeException(e);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> String prepareJSONRequest(T entityRequestBean){
        String jsonRequestString=serialiazeWithGson(entityRequestBean,true);
        return jsonRequestString;
    }

    @SuppressWarnings("unchecked")
    private <T> T serialiazeWithGson(T enitityRequestBean){
        Gson gson = new GsonBuilder().setDateFormat(getGsonDateFormat())
                .registerTypeHierarchyAdapter(Collection.class, new GsonCollectionAdapter())
                .create();
        String jsonString=gson.toJson(enitityRequestBean);
        String excludedEmptyObjectString=jsonString.replace("{}", "null");
        enitityRequestBean=(T) gson.fromJson(excludedEmptyObjectString, enitityRequestBean.getClass());
        return enitityRequestBean;
    }

    private <T> String serialiazeWithGson(T enitityRequestBean,boolean returnJsonString){
        Gson gson = new GsonBuilder().setDateFormat(getGsonDateFormat())
                .registerTypeHierarchyAdapter(Collection.class, new GsonCollectionAdapter())
                .create();
        String jsonString=gson.toJson(enitityRequestBean);
        String excludedEmptyObjectString=jsonString.replace("{}", "null");
        return excludedEmptyObjectString;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getEndPointURL() {
        return endPointURL;
    }

    public void setEndPointURL(String endPointURL) {
        this.endPointURL = endPointURL;
    }

    public List<String> getErrorTypeList() {
        return errorTypeList;
    }

    public void setErrorTypeList(List<String> errorTypeList) {
        this.errorTypeList = errorTypeList;
    }

    public String getGsonDateFormat() {
        return gsonDateFormat;
    }

    public void setGsonDateFormat(String gsonDateFormat) {
        this.gsonDateFormat = gsonDateFormat;
    }

    public Boolean getPrettyPrintOn() {
        return prettyPrintOn;
    }

    public void setPrettyPrintOn(Boolean prettyPrintOn) {
        this.prettyPrintOn = prettyPrintOn;
    }

    public Boolean getSerializeNulls() {
        return serializeNulls;
    }

    public void setSerializeNulls(Boolean serializeNulls) {
        this.serializeNulls = serializeNulls;
    }

    public Boolean getExcludeEmpty() {
        return excludeEmpty;
    }

    public void setExcludeEmpty(Boolean excludeEmpty) {
        this.excludeEmpty = excludeEmpty;
    }

    public Boolean getDisableHTMLEscaping() {
        return disableHTMLEscaping;
    }

    public void setDisableHTMLEscaping(Boolean disableHTMLEscaping) {
        this.disableHTMLEscaping = disableHTMLEscaping;
    }

    public boolean isErrorInResponse() {
        return errorInResponse;
    }

    public void setErrorInResponse(boolean errorInResponse) {
        this.errorInResponse = errorInResponse;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public List<String> getPathParams() {
        return pathParams;
    }

    public void setPathParams(List<String> pathParams) {
        this.pathParams = pathParams;
    }

    public boolean ifHasPathParams() {
        return hasPathParams;
    }

    public void setHasPathParams(boolean hasPathParams) {
        this.hasPathParams = hasPathParams;
    }

    public boolean ifHasQueryParams() {
        return hasQueryParams;
    }

    public void setHasQueryParams(boolean hasQueryParams) {
        this.hasQueryParams = hasQueryParams;
    }

    public String getWebServiceName() {
        return webServiceName;
    }

    public void setWebServiceName(String webServiceName) {
        this.webServiceName = webServiceName;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setHttpsDefaultTLSv2(boolean httpsDefaultTLSv2) {
        this.httpsDefaultTLSv2 = httpsDefaultTLSv2;
    }

    public Response getCurrentResponse() {
        return currentResponse;
    }

    public void setCurrentResponse(Response currentResponse) {
        this.currentResponse = currentResponse;
    }

    public boolean isLogRequestResponse() {
        return logRequestResponse;
    }

    public void setLogRequestResponse(boolean logRequestResponse) {
        this.logRequestResponse = logRequestResponse;
    }

    public boolean isLogResponseErrors() {
        return logResponseErrors;
    }

    public void setLogResponseErrors(boolean logResponseErrors) {
        this.logResponseErrors = logResponseErrors;
    }

    public boolean hasSecuredClientPolicy() {
        return securedClientPolicy;
    }

    public void setSecuredClientPolicy(boolean securedClientPolicy) {
        this.securedClientPolicy = securedClientPolicy;
    }

    public KeyStore getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(KeyStore keyStore) {
        this.keyStore = keyStore;
    }

    public boolean hasUserDefinedKeyStore() {
        return userDefinedKeyStore;
    }

    public void setUserDefinedKeyStore(boolean userDefinedKeyStore) {
        this.userDefinedKeyStore = userDefinedKeyStore;
    }

    public static class TrustAllHostNameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
    public boolean isUseJackson() {
        return useJackson;
    }

    public void setUseJackson(boolean useJackson) {
        this.useJackson = useJackson;
    }

    public ClientConfig getClientConfiguration() {
        return clientConfiguration;
    }
    public List<Object> getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(List<Object> errorCode) {
        this.errorCode = errorCode;
    }

    public MultivaluedMap<String, Object> getResponseHeaderMap() {
        return responseHeaderMap;
    }

    public void setResponseHeaderMap(MultivaluedMap<String, Object> responseHeaderMap) {
        this.responseHeaderMap = responseHeaderMap;
    }

    public Map<String,List<Object>> getResponseErrorMap() {
        return responseErrorMap;
    }

    public void setResponseErrorMap(Map<String,List<Object>> responseErrorMap) {
        this.responseErrorMap = responseErrorMap;
    }
    public void responseHasGzipEncoding() {
        this.responseHasGzipEncoding = true;
    }
}
