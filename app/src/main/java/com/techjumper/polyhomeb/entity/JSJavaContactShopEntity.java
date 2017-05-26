package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/11/1
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JSJavaContactShopEntity {


    /**
     * method : ContactShop
     * params : {"store_id":"25","shop_service_id":"71","tel":"15712908185"}
     */

    private String method;
    /**
     * store_id : 25
     * shop_service_id : 71
     * tel : 15712908185
     */

    private ParamsBean params;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public static class ParamsBean {
        private String store_id;
        private String shop_service_id;
        private String tel;

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getShop_service_id() {
            return shop_service_id;
        }

        public void setShop_service_id(String shop_service_id) {
            this.shop_service_id = shop_service_id;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }
    }
}
