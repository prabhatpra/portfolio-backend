package com.prabhat.portfolio.exception;

public class ContactException extends RuntimeException {

    public ContactException(String message) {
        super(message);
    }

    // 1. Rate limit exception
    public static class RateLimitException extends ContactException {
        public RateLimitException(String message) {
            super(message);
        }
    }

    // 2. Duplicate message exception
    public static class DuplicateMessageException extends ContactException {
        public DuplicateMessageException(String message) {
            super(message);
        }
    }

    // 3. Not found exception
    public static class NotFoundException extends ContactException {
        public NotFoundException(String message) {
            super(message);
        }
    }
}