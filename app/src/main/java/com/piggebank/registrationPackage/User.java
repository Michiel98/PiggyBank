package com.piggebank.registrationPackage;

public class User {
    public String nickname, email;

    public User(){}
    public User(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }
    public String getEmail() {
        return email;
    }
}
