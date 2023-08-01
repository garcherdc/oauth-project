package com.example.demo.manager;


import com.example.demo.manager.kafka.SystemConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@Slf4j
public class HttpClientManager {

    @Autowired
    Environment environment;

    @Autowired
    private SystemConfig systemConfig;

    private RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        PoolingHttpClientConnectionManager conMgr = new PoolingHttpClientConnectionManager();
        conMgr.setMaxTotal(systemConfig.getMaxTotal());//设置整个连接池最大连接数 根据自己的场景决定
        conMgr.setDefaultMaxPerRoute(systemConfig.getMaxPerRoute());//设置默认的最大路由
        RequestConfig requestConfig = null;
        log.info("env is:" + environment.getProperty("ENV", ""));
        requestConfig = RequestConfig.custom().
                setSocketTimeout(systemConfig.getHttpSocketTimeout()).
                setConnectTimeout(5000).
                setConnectionRequestTimeout(5000).build();
        HttpClient httpClient = HttpClients.custom().
                evictIdleConnections(60, TimeUnit.SECONDS).// 删除空闲连接时间
                setConnectionManager(conMgr).
                setDefaultRequestConfig(requestConfig).
                build();
        HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        restTemplate = new RestTemplate(httpComponentsClientHttpRequestFactory);
        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
        StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        httpMessageConverters.add(stringHttpMessageConverter);
        httpMessageConverters.add(new ByteArrayHttpMessageConverter());
        httpMessageConverters.add(new MappingJackson2HttpMessageConverter());
        httpMessageConverters.add(new AllEncompassingFormHttpMessageConverter());
        restTemplate.setMessageConverters(httpMessageConverters);
    }

    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForObject(url, responseType, uriVariables);
    }

    public <T> ResponseEntity<T> getForEntityWithHeader(String url, Map<String, Object> headers, Class<T> responseType, Object... uriVariables) {
        HttpEntity httpEntity = getHttpEntityHeader(headers, null, null);
        ResponseEntity<T> result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, responseType);
        return result;
    }

    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    public <T> ResponseEntity<T> postForEntity(String url, @Nullable Object request, Class<T> responseType,
                                               Object... uriVariables) {
        ResponseEntity<T> responseEntity = restTemplate.postForEntity(url, request, responseType, uriVariables);
        return responseEntity;
    }

    public <T> ResponseEntity<T> postForEntityWithUrlencoded(String url, Map<String, Object> header, Map<String, Object> params, Class<T> responseType,
                                                             Object... uriVariables) {
        HttpEntity httpEntity = getHttpEntity(header, params, MediaType.APPLICATION_FORM_URLENCODED);
        return postForEntity(url, httpEntity, responseType, uriVariables);
    }

    public <T> ResponseEntity<T> postForEntityWithUrlHeader(String url, Map<String, Object> header, @Nullable Object request, Class<T> responseType,
                                                            Object... uriVariables) {
        HttpEntity httpEntity = getHttpEntityHeader(header, request, MediaType.APPLICATION_JSON_UTF8);
        return postForEntity(url, httpEntity, responseType, uriVariables);
    }

    public <T> ResponseEntity<T> postForEntityWithJson(String url, @Nullable Object request, Class<T> responseType,
                                                       Object... uriVariables) {
        HttpEntity httpEntity = getHttpEntity(request, MediaType.APPLICATION_JSON_UTF8);
        return postForEntity(url, httpEntity, responseType, uriVariables);
    }

    public ResponseEntity postWithFileStream(String url, Map<String, Object> params, Map<String, InputStream> fileMap) throws IOException {
        //设置请求头
        HttpHeaders headers = new HttpHeaders();
        //请求体为入参map
        LinkedMultiValueMap<String, Object> request = new LinkedMultiValueMap<>();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            request.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        //文件流
        MediaType mediaType = null;
        if(MapUtils.isNotEmpty(fileMap)) {
            //multipart/form-data
            mediaType = MediaType.parseMediaType(MediaType.MULTIPART_FORM_DATA_VALUE);
            for (String fileName : fileMap.keySet()) {
                InputStream fileStream = fileMap.get(fileName);
                ByteArrayResource resource = new ByteArrayResource(IOUtils.toByteArray(fileStream)) {
                    @Override
                    public String getFilename() {
                        return fileName;
                    }
                };
                request.add(fileName, resource);
            }
        }else{
            //multipart/form-data
            mediaType = MediaType.parseMediaType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        }
        headers.setContentType(mediaType);
        //用httpEntity封装整个请求报文
        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity(request, headers);
        return restTemplate.postForEntity(url, entity, String.class);
    }

    private String createRedirectUrl(HttpServletRequest request, String routeUrl, String prefix) {
        String queryString = request.getQueryString();
        return routeUrl + request.getRequestURI().replace(prefix, "") +
                (queryString != null ? "?" + queryString : "");
    }

    private RequestEntity createRequestEntity(HttpServletRequest request, String url) throws URISyntaxException, IOException {
        String method = request.getMethod();
        HttpMethod httpMethod = HttpMethod.resolve(method);
        MultiValueMap<String, String> headers = parseRequestHeader(request);
        byte[] body = parseRequestBody(request);
        return new RequestEntity<>(body, headers, httpMethod, new URI(url));
    }

    public ResponseEntity<byte[]> exchange(RequestEntity requestEntity, Class<byte[]> responseType) {
        return restTemplate.exchange(requestEntity, responseType);
    }

    public ResponseEntity<byte[]> exchange(String url, HttpMethod method,
                                           @Nullable HttpEntity<?> requestEntity, Class<byte[]> responseType, Map<String, ?> uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    private byte[] parseRequestBody(HttpServletRequest request) throws IOException {
        InputStream inputStream = request.getInputStream();
        return StreamUtils.copyToByteArray(inputStream);
    }

    private MultiValueMap<String, String> parseRequestHeader(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        List<String> headerNames = Collections.list(request.getHeaderNames());
        for (String headerName : headerNames) {
            List<String> headerValues = Collections.list(request.getHeaders(headerName));
            for (String headerValue : headerValues) {
                headers.add(headerName, headerValue);
            }
        }
        return headers;
    }

    private HttpEntity getHttpEntity(Map<String, Object> header, Map<String, Object> param, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        if (header != null) {
            for (String name : header.keySet()) {
                headers.add(name, (String) header.get(name));
            }
        }
        if(mediaType!=null) {
            headers.setContentType(mediaType);
        }
        MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            body.add(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return new HttpEntity<>(body, headers);
    }

    private HttpEntity getHttpEntityHeader(Map<String, Object> header, Object object, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        if (header != null) {
            for (String name : header.keySet()) {
                headers.add(name, (String) header.get(name));
            }
        }
        if(mediaType!=null) {
            headers.setContentType(mediaType);
        }
        return new HttpEntity<>(object, headers);
    }

    private HttpEntity getHttpEntity(Object object, MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new HttpEntity<>(object, headers);
    }

    public <T> ResponseEntity<T> deleteForEntity(String url, Map<String, Object> header, @Nullable Object request, Class<T> responseType,
                                                 Object... uriVariables) {
        HttpEntity httpEntity = getHttpEntityHeader(header, request, MediaType.APPLICATION_JSON_UTF8);
        ResponseEntity<T> responseEntity = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, responseType, uriVariables);
        return responseEntity;
    }

}
