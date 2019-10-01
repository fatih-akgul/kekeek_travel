package com.kekeek.travel.model;

import lombok.Data;

import java.util.Collection;
import java.util.HashSet;

@Data
abstract class BaseForm {
    private Collection<String> errors = new HashSet<>();
    private Collection<String> messages = new HashSet<>();

    public void addError(String errorMessage) {
        errors.add(errorMessage);
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public abstract boolean validate();
    public abstract void clearFields();
}
