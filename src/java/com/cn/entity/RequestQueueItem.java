/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cn.entity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author LFeng
 */
public class RequestQueueItem {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private String paramsJson;

    public RequestQueueItem(HttpServletRequest request, HttpServletResponse response, String paramsJson) {
        this.request = request;
        this.response = response;
        this.paramsJson = paramsJson;
    }

    
    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public void setResponse(HttpServletResponse response) {
        this.response = response;
    }

    public String getParamsJson() {
        return paramsJson;
    }

    public void setParamsJson(String paramsJson) {
        this.paramsJson = paramsJson;
    }
    
}
