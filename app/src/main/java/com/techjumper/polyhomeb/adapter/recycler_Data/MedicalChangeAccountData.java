package com.techjumper.polyhomeb.adapter.recycler_Data;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/9/25
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class MedicalChangeAccountData {

    private boolean isCurrentAccount;
    private String name;
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCurrentAccount() {
        return isCurrentAccount;
    }

    public void setCurrentAccount(boolean currentAccount) {
        isCurrentAccount = currentAccount;
    }
}
