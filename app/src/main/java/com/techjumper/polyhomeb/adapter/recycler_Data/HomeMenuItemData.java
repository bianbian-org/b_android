package com.techjumper.polyhomeb.adapter.recycler_Data;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/31
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class HomeMenuItemData {

    public enum ItemType {
        FAMILY,
        SMARTHOME,
        MESSAGE,
        POINIS,
        SETTING,
        OTHER
    }

    private String title;
    private String rightText;
    private ItemType itemType;

    public ItemType getItemType() {
        if (itemType == null)
            return ItemType.OTHER;
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
    }

    public HomeMenuItemData(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
