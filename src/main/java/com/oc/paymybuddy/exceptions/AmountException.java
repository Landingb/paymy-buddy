package com.oc.paymybuddy.exceptions;

public class AmountException extends Exception {

    private static final long serialVersionUID = -7603232579220102794L;

    private final String errorCode;

    private final String defaultMessage;

    public AmountException(String errorCode, String defaultMessage) {
        this.errorCode = errorCode;
        this.defaultMessage = defaultMessage;
    }
}
