package com.techjumper.polyhomeb.adapter.recycler_Data;

import com.techjumper.polyhomeb.entity.C_RoomsByMemberEntity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/12
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MemberManageData {

    private String memberName;
    private String memberId;
    private C_RoomsByMemberEntity entity;
    private boolean isDeleteMode;

    public boolean isDeleteMode() {
        return isDeleteMode;
    }

    public void setDeleteMode(boolean deleteMode) {
        isDeleteMode = deleteMode;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public C_RoomsByMemberEntity getEntity() {
        return entity;
    }

    public void setEntity(C_RoomsByMemberEntity entity) {
        this.entity = entity;
    }
}
