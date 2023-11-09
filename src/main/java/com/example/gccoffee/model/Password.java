package com.example.gccoffee.model;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

public class Password implements Serializable {
    private final String password;

    public Password(String password) {
        Assert.notNull(password, "password should not be null");
        Assert.isTrue(password.length()>=8 && password.length()<=20);
        Assert.isTrue(checkPassword(password), "Invalid email address");
        this.password = password;
    }

    private static boolean checkPassword(String password) {
        return Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$", password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password1 = (Password) o;
        return Objects.equals(password, password1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }
}
