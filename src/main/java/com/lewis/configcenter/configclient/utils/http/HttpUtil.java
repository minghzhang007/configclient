package com.lewis.configcenter.configclient.utils.http;

import com.google.common.base.Function;
import com.google.common.io.BaseEncoding;
import com.google.common.io.CharStreams;
import com.google.gson.Gson;
import com.lewis.configcenter.configclient.constants.HttpStatusEnum;
import com.lewis.configcenter.configclient.inject.ConfigInjector;
import com.lewis.configcenter.configclient.utils.ConfigUtil;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * @author lewis0077
 */
public class HttpUtil {

    private ConfigUtil configUtil;

    private String basicAuth;

    private Gson gson;

    public HttpUtil() {
        configUtil = ConfigInjector.getInstance(ConfigUtil.class);
        gson = new Gson();
        try {
            basicAuth = "Basic" + BaseEncoding.base64().encode("user".getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public <T> HttpResponse<T> get(HttpRequest request, final Class<T> clazz) {
        Function<String, T> function = new Function<String, T>() {
            @Override
            public T apply(String input) {

                return gson.fromJson(input, clazz);
            }
        };
        return doGet(request, function);
    }

    private <T> HttpResponse<T> doGet(HttpRequest request, Function<String, T> function) {
        InputStreamReader isr = null;
        int statusCode;
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(request.getUrl()).openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", basicAuth);

            int connectTimeout = request.getConnectTimeout();
            if (connectTimeout < 0) {
                connectTimeout = configUtil.getConnectTimeout();
            }

            int readTimeout = request.getReadTimeout();
            if (readTimeout < 0) {
                readTimeout = configUtil.getReadTimeout();
            }

            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);

            conn.connect();

            statusCode = conn.getResponseCode();

            if (statusCode == HttpStatusEnum.SC_OK.getStatusCode()) {
                isr = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
                String content = CharStreams.toString(isr);
                return new HttpResponse<>(statusCode, function.apply(content));
            }

            if (statusCode == HttpStatusEnum.SC_NOT_MODIFIED.getStatusCode()) {
                return new HttpResponse<>(statusCode, null);
            }

        } catch (Throwable ex) {

        } finally {
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }

        throw new RuntimeException("");
    }
}
