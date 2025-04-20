package com.Consultory.app.exception;

public class InvalidAppointmentTime extends RuntimeException {
    public InvalidAppointmentTime(String message) {
        super(message);
    }
}
