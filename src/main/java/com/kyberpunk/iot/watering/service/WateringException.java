package com.kyberpunk.iot.watering.service;

public class WateringException extends RuntimeException {
    public WateringException(String message) {
        super(message);
    }

    public WateringException(String message, Throwable cause) {
        super(message, cause);
    }
}
