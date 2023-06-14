package com.gon.dashboards.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Http request tool
 */

@Slf4j
public class HttpClientUtil {


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final int TIME_OUT = 60;
    private static final HttpClientUtil INSTANCE = new HttpClientUtil();
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(TIME_OUT, TimeUnit.SECONDS)
            .build();


    /**
     * Get request method
     *
     * @param url       Request url
     * @param headerMap Request header
     * @return
     */
    public static String get(String url, Map<String, String> headerMap) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        Request.Builder builder = new Request.Builder().url(url);
        if (headerMap != null && !headerMap.isEmpty()) {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        try {
            ResponseBody responseBody = CLIENT.newCall(request).execute().body();
            if (responseBody != null) {
                return responseBody.string();
            }
        } catch (IOException e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return null;
    }


    /**
     * Get request method
     *
     * @param url Request url
     * @return
     */
    public static String get(String url) {
        return get(url, null);
    }

    /**
     * Post request method
     *
     * @param url  Request url
     * @param json Request boy
     * @return
     */
    public static String post(String url, String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            ResponseBody responseBody = CLIENT.newCall(request).execute().body();
            if (responseBody != null) {
                String bodystr = responseBody.string();
                return bodystr;
            }
        } catch (IOException e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return null;
    }


    /**
     * Post request method
     *
     * @param url    Request url
     * @param params Request params
     * @return
     */
    public String post(String url, Map<String, String> params) {

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            ResponseBody responseBody = CLIENT.newCall(request).execute().body();
            if (responseBody != null) {
                return responseBody.string();
            }
        } catch (IOException e) {
            log.error("error: {}", e.getMessage(), e);
        }
        return null;
    }

}
