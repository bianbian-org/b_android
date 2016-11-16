package com.techjumper.commonres.entity.event;

import com.techjumper.commonres.entity.UserInfoEntity;

/**
 * Created by kevin on 16/6/23.
 */
public class UserInfoEvent {

    private UserInfoEntity entity;

    public UserInfoEvent(UserInfoEntity entity) {
        this.entity = entity;
    }

    public UserInfoEntity getEntity() {
        return entity;
    }

    public void setEntity(UserInfoEntity entity) {
        this.entity = entity;
    }
}
