package com.example.sseexperiment.exceptions;

public class LockTimeOutException extends Exception {

    private static final long serialVersionUID = 6700697376100628473L;

    public LockTimeOutException() {
        super();
    }
    
    public LockTimeOutException(String s) {
        super(s);
    }
}
