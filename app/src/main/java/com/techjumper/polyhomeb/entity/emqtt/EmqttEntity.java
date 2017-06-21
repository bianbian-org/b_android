package com.techjumper.polyhomeb.entity.emqtt;

/**
 * Created by Admin on 2017/6/19.
 */

public class EmqttEntity {


    /**
     * app_key :
     * timestamp : 1497833398
     * validation_token :
     * payload : {"body":{"title":"您的投诉有新的回复","text":"物业向您回复: 45","after_open":"go_activity","after_open_page":"com.techjumper.polyhomeb.mvp.v.activity.PropertyDetailActivity"},"extra":{"complaint":2},"display_type":"notification","play_sound":"true"}
     */

    private String app_key;
    private int timestamp;
    private String validation_token;
    private PayloadBean payload;
    private String sign;
    private String ticket;

    public String getApp_key() {
        return app_key;
    }

    public void setApp_key(String app_key) {
        this.app_key = app_key;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }


    public String getValidation_token() {
        return validation_token;
    }

    public void setValidation_token(String validation_token) {
        this.validation_token = validation_token;
    }

    public PayloadBean getPayload() {
        return payload;
    }

    public void setPayload(PayloadBean payload) {
        this.payload = payload;
    }

    public static class PayloadBean<T> {
        /**
         * body : {"title":"您的投诉有新的回复","text":"物业向您回复: 45","after_open":"go_activity","after_open_page":"com.techjumper.polyhomeb.mvp.v.activity.PropertyDetailActivity"}
         * extra : {"complaint":2}
         * display_type : notification
         * play_sound : true
         */

        private BodyBean body;
        private T extra;
        private String display_type;
        private String play_sound;

        public BodyBean getBody() {
            return body;
        }

        public void setBody(BodyBean body) {
            this.body = body;
        }

        public T getExtra() {
            return extra;
        }

        public void setExtra(T extra) {
            this.extra = extra;
        }

        public String getDisplay_type() {
            return display_type;
        }

        public void setDisplay_type(String display_type) {
            this.display_type = display_type;
        }

        public String getPlay_sound() {
            return play_sound;
        }

        public void setPlay_sound(String play_sound) {
            this.play_sound = play_sound;
        }

        public static class BodyBean {
            /**
             * title : 您的投诉有新的回复
             * text : 物业向您回复: 45
             * after_open : go_activity
             * after_open_page : com.techjumper.polyhomeb.mvp.v.activity.PropertyDetailActivity
             */

            private String title;
            private String text;
            private String after_open;
            private String after_open_page;
            private int timestamp;


            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public String getAfter_open() {
                return after_open;
            }

            public void setAfter_open(String after_open) {
                this.after_open = after_open;
            }

            public String getAfter_open_page() {
                return after_open_page;
            }

            public void setAfter_open_page(String after_open_page) {
                this.after_open_page = after_open_page;
            }

            public int getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(int timestamp) {
                this.timestamp = timestamp;
            }

        }

    }
}
