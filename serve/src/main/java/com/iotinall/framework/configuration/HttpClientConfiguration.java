package com.iotinall.framework.configuration;

import lombok.Data;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Data
@Configuration
public class HttpClientConfiguration {

    @Bean
    public CloseableHttpClient httpClient(HttpClientBuilder httpClientBuilder) {
        return httpClientBuilder.build();
    }

    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(200);
        clientConnectionManager.setDefaultMaxPerRoute(15);
        return clientConnectionManager;
    }

    @Bean
    public HttpClientBuilder httpClientBuilder(PoolingHttpClientConnectionManager clientConnectionManager) {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(300000)
                .setConnectionRequestTimeout(300000)
                .build();
        HttpClientBuilder builder = HttpClientBuilder.create();
        Header[] defaultHeaders = {new BasicHeader("charset", "utf-8"),
                new BasicHeader("content-type", "application/x-www-form-urlencoded")};
        builder.setDefaultHeaders(Arrays.asList(defaultHeaders));
        builder.setConnectionManager(clientConnectionManager);
        builder.setDefaultRequestConfig(requestConfig);
        builder.disableAutomaticRetries();
        return builder;
    }

    /**
     * 获取一个新的client池
     * @return
     */
    public static CloseableHttpClient getNewHttpClientPool(){
        PoolingHttpClientConnectionManager clientConnectionManager = new PoolingHttpClientConnectionManager();
        clientConnectionManager.setMaxTotal(200);
        clientConnectionManager.setDefaultMaxPerRoute(12);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(300000)
                .setConnectionRequestTimeout(300000)
                .build();

        HttpClientBuilder builder = HttpClientBuilder.create();
        Header[] defaultHeaders = {new BasicHeader("charset", "utf-8"),
                new BasicHeader("content-type", "application/x-www-form-urlencoded")};
        builder.setDefaultHeaders(Arrays.asList(defaultHeaders));
        builder.setConnectionManager(clientConnectionManager);
        builder.setRetryHandler(new DefaultHttpRequestRetryHandler());
        builder.setDefaultRequestConfig(requestConfig);
        return builder.build();
    }

}
