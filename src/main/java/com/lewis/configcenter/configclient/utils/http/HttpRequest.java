package com.lewis.configcenter.configclient.utils.http;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lewis0077
 */
@Data
@NoArgsConstructor
public class HttpRequest {

    private String url;

    private int connectTimeout;

    private int readTimeout;

    public HttpRequest(String url) {
        this.url = url;
        this.connectTimeout = -1;
        this.readTimeout = -1;
    }

    public HttpRequest(String url, int connectTimeout, int readTimeout) {
        this.url = url;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

}
