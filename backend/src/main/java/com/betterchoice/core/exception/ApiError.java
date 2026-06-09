package com.betterchoice.core.exception;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ApiError {

    private boolean success;
    private ErrorBody error;
    private Instant timestamp;

    @Data
    @Builder
    public static class ErrorBody {
        private String code;
        private String message;
        private List<FieldError> details;
    }

    @Data
    @Builder
    public static class FieldError {
        private String field;
        private String message;
    }
}
