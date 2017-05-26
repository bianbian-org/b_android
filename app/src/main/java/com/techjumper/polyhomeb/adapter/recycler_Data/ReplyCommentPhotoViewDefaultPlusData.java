package com.techjumper.polyhomeb.adapter.recycler_Data;

import java.util.ArrayList;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/16
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ReplyCommentPhotoViewDefaultPlusData {

    private int ImageResource;
    private ArrayList<String> selectPhotoes;

    public ArrayList<String> getSelectPhotoes() {
        return selectPhotoes;
    }

    public void setSelectPhotoes(ArrayList<String> selectPhotoes) {
        this.selectPhotoes = selectPhotoes;
    }

    public int getImageResource() {
        return ImageResource;
    }

    public void setImageResource(int imageResource) {
        ImageResource = imageResource;
    }
}
