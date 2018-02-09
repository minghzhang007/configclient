package com.lewis.configcenter.configclient.utils.http;

import lombok.Data;

/**
 * @author lewis0077
 */
@Data
public class HttpResponse<T> {

    private final int statusCode;

    private final T body;

    public HttpResponse(int statusCode, T body) {
        this.statusCode = statusCode;
        this.body = body;
    }
}
