package com.prabhat.portfolio.exception;

public class ContactException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ContactException(String message) {
        super(message);
    }

    
    public static class RateLimitException extends ContactException {

        private static final long serialVersionUID = 1L;

        public RateLimitException(String message) {
            super(message);
        }
    }

   
    public static class DuplicateMessageException extends ContactException {

        private static final long serialVersionUID = 1L;

        public DuplicateMessageException(String message) {
            super(message);
        }
    }

    // ================= NOT FOUND =================
    public static class NotFoundException extends ContactException {

        private static final long serialVersionUID = 1L;

        public NotFoundException(String message) {
            super(message);
        }
    }

    
    public static class InvalidStatusException extends ContactException {

        private static final long serialVersionUID = 1L;

        public InvalidStatusException(String message) {
            super(message);
        }
    }
}