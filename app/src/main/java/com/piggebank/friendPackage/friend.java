package com.piggebank.friendPackage;

public class friend {

    public String nickname, uid;

    public friend(){

    }

    public friend(String nickname, String uid) {
        this.nickname = nickname;
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }
    public String getUid() {
        return uid;
    }
}
