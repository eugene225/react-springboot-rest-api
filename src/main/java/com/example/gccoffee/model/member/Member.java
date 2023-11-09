package com.example.gccoffee.model.member;

import com.example.gccoffee.model.Email;
import com.example.gccoffee.model.Password;

import java.time.LocalDateTime;
import java.util.UUID;

public class Member {
    private final UUID memberUUID;
    private final String name;
    private Email email;
    private Password password;
    private long money;
    private final LocalDateTime createdAt;

    public Member(UUID memberUUID, String name, Email email, Password password, long money, LocalDateTime createdAt) {
        this.memberUUID = memberUUID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.money = 0;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getMemberUUID() {
        return memberUUID;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public long getMoney() {
        return money;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
