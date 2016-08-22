package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class ShowBigPic extends BaseEntity<ShowBigPic.DataBean> {

    public static class DataBean {

        private String[][] images;
        private String index;

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String[][] getImages() {
            return images;
        }

        public void setImages(String[][] images) {
            this.images = images;
        }
    }
}
