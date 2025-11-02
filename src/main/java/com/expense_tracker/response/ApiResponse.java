package com.expense_tracker.response;

public class ApiResponse<T> {
    private String status;
    private String message;
    private T data;
    private int statusCode;

    public ApiResponse() {}

    public ApiResponse(String status, String message, T data, int statusCode) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}