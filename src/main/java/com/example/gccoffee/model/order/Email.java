package com.example.gccoffee.model.order;

import com.example.gccoffee.exception.InvalidEmailPatternException;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;

public class Email implements Serializable {
    private final String address;

    public Email(String address) {
        Assert.notNull(address, "address should not be null");
        Assert.isTrue(address.length() >= 4 && address.length() <= 50, "address length must be between 4 and 50 characters.");
        Assert.isTrue(checkAddress(address), "Invalid email address");
        this.address = address;
    }
    private static boolean checkAddress(String address) throws InvalidEmailPatternException {
        if (!Pattern.matches("^\\w+@\\w+\\.\\w+(\\.\\w+)?$", address)) {
            throw new InvalidEmailPatternException("이메일 주소 형식이 잘못되었습니다.");
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(address, email.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address);
    }

    @Override
    public String toString() {
        return "address='" + address;
    }

    public String getAddress() {
        return address;
    }
}
