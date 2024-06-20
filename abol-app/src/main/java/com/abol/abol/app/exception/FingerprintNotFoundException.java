package com.abol.abol.app.exception;

import javax.naming.AuthenticationException;

public class FingerprintNotFoundException extends AuthenticationException{

    public FingerprintNotFoundException(final String msg) {
        super(msg);
    }
}