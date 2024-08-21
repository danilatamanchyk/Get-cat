package com.example.getcat;

public class DogOrCatImage {
    private String message;
    private String status;

    public DogOrCatImage(String message, String status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
