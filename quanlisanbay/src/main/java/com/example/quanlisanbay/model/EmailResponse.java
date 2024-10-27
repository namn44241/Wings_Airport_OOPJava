package com.example.quanlisanbay.model;

public class EmailResponse {
    private boolean success;
    private String error;

    public EmailResponse(boolean success) {
        this.success = success;
    }

    public EmailResponse(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    // Getters and setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}