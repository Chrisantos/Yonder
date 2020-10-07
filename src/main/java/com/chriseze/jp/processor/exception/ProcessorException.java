package com.chriseze.jp.processor.exception;

public class ProcessorException extends Exception {
    private static final long serialVersionUID = -8701989629591090931L;

    public ProcessorException() {
        super();
    }

    public ProcessorException(String message) {
        super(message);
    }

    public ProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessorException(Throwable cause) {
        super(cause);
    }

    protected ProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
