package com.capstone.backend.dto;

import java.time.ZonedDateTime;

public record ExceptionResponse(
    String message,
    int status,
    ZonedDateTime timestamp) {

}
