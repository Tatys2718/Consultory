package com.Consultory.app.exception;

public class InvalidAppointmentDoctor extends RuntimeException {
    public InvalidAppointmentDoctor(String message) {
        super(message);
    }
}
