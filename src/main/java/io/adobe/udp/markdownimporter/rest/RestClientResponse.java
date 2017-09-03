package io.adobe.udp.markdownimporter.rest;

public class RestClientResponse {
    private int status;
    private String json;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getStatus() {
        return status;
    }

    public String getJson() {
        return json;
    }
}
