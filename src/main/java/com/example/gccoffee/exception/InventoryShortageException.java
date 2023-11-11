package com.example.gccoffee.exception;

public class InventoryShortageException extends RuntimeException{
    public InventoryShortageException(String message) {
        super(message);
    }
}
