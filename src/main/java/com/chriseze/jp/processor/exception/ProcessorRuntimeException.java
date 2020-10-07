package com.chriseze.jp.processor.exception;

public class ProcessorRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 56718756104107988L;

    public ProcessorRuntimeException() {
        super();
    }

    public ProcessorRuntimeException(String message) {
        super(message);
    }

    public ProcessorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProcessorRuntimeException(Throwable cause) {
        super(cause);
    }

    protected ProcessorRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
