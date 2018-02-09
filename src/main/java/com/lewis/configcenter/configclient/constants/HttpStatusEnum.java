package com.lewis.configcenter.configclient.constants;

/**
 * @author lewis0077
 */

public enum HttpStatusEnum {
    SC_OK(200), SC_NOT_MODIFIED(304), SC_NOT_FOUND(404);

    HttpStatusEnum(int statusCode) {
        this.statusCode = statusCode;
    }

    private int statusCode;

    public int getStatusCode() {
        return statusCode;
    }
}
