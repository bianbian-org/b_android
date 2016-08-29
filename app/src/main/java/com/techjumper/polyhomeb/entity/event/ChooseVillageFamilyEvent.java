package com.techjumper.polyhomeb.entity.event;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/29
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ChooseVillageFamilyEvent {
    /**
     * 需要发送到HomeMenuFragment中,作为侧边栏显示当前小区或者家庭
     * 发送到HomeFragment中作为title
     * 发送到MyVillageFamilyActivity中作为默认选中打钩的item
     */

    private int id;
    private String name;
    private int verified;
    private boolean isFamily;
    private int position;

    public boolean isFamily() {
        return isFamily;
    }

    public ChooseVillageFamilyEvent(int id, String name, int verified, boolean isFamily, int position) {
        this.id = id;
        this.name = name;
        this.verified = verified;
        this.isFamily = isFamily;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getVerified() {
        return verified;
    }

    public int getPosition() {
        return position;
    }
}
