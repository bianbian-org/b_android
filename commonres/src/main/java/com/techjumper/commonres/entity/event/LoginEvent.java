package com.techjumper.commonres.entity.event;

/**
 * Created by kevin on 16/5/10.
 */
public class LoginEvent {

    boolean isLogin;

    public LoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
