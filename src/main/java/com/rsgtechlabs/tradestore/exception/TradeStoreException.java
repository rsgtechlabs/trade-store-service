package com.rsgtechlabs.tradestore.exception;

//Custom Exception class
public class TradeStoreException extends RuntimeException {

    public TradeStoreException() {
    }
    public TradeStoreException(final String message) {
        super(message);
    }

    public TradeStoreException(final String message, Throwable cause) {
        super(message, cause);
    }
}

