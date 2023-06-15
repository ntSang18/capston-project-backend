package com.capstone.backend.dto.exception;

import java.time.ZonedDateTime;

public record ExceptionResponse(
    String message,
    int status,
    ZonedDateTime timestamp) {

}
