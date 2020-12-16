package com.test.exception;

/**
 * An internal exception marker
 */
public class WeatherException extends Exception {

    public WeatherException(){

    }

    public WeatherException(String message) {
        super(message);
    }
}
