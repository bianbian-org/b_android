package com.techjumper.commonres.entity;

/**
 * Created by kevin on 16/4/27.
 */
public class WeatherEntity extends BaseEntity<WeatherEntity.WeatherDataEntity> {

    public static class WeatherDataEntity {
        private String city_name;
        private String city_id;
        private String date;
        //PM2.5
        private String pm25;
        //空起质量
        private String quality;
        //空气描述
        private String des;
        //天气
        private String weather_info;
        private String temperature;
        //天气图标 img的数目对应本地图片
        private String img;
        //最高气温
        private String temperature_hight;
        //最低气温
        private String temperature_low;
        //一天后日期 后面依次类推
        private String date_one;
        //一天后气温
        private String temperature_one;
        //一天后天气
        private String weather_one;
        //一天后天气图标
        private String img_one;
        private String date_two;
        private String temperature_two;
        private String weather_two;
        private String img_two;
        private String date_three;
        private String temperature_three;
        private String weather_three;
        private String img_three;
        private String date_four;
        private String temperature_four;
        private String weather_four;
        private String img_four;

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPm25() {
            return pm25;
        }

        public void setPm25(String pm25) {
            this.pm25 = pm25;
        }

        public String getQuality() {
            return quality;
        }

        public void setQuality(String quality) {
            this.quality = quality;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getWeather_info() {
            return weather_info;
        }

        public void setWeather_info(String weather_info) {
            this.weather_info = weather_info;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getTemperature_hight() {
            return temperature_hight;
        }

        public void setTemperature_hight(String temperature_hight) {
            this.temperature_hight = temperature_hight;
        }

        public String getTemperature_low() {
            return temperature_low;
        }

        public void setTemperature_low(String temperature_low) {
            this.temperature_low = temperature_low;
        }

        public String getDate_one() {
            return date_one;
        }

        public void setDate_one(String date_one) {
            this.date_one = date_one;
        }

        public String getTemperature_one() {
            return temperature_one;
        }

        public void setTemperature_one(String temperature_one) {
            this.temperature_one = temperature_one;
        }

        public String getWeather_one() {
            return weather_one;
        }

        public void setWeather_one(String weather_one) {
            this.weather_one = weather_one;
        }

        public String getImg_one() {
            return img_one;
        }

        public void setImg_one(String img_one) {
            this.img_one = img_one;
        }

        public String getDate_two() {
            return date_two;
        }

        public void setDate_two(String date_two) {
            this.date_two = date_two;
        }

        public String getTemperature_two() {
            return temperature_two;
        }

        public void setTemperature_two(String temperature_two) {
            this.temperature_two = temperature_two;
        }

        public String getWeather_two() {
            return weather_two;
        }

        public void setWeather_two(String weather_two) {
            this.weather_two = weather_two;
        }

        public String getImg_two() {
            return img_two;
        }

        public void setImg_two(String img_two) {
            this.img_two = img_two;
        }

        public String getDate_three() {
            return date_three;
        }

        public void setDate_three(String date_three) {
            this.date_three = date_three;
        }

        public String getTemperature_three() {
            return temperature_three;
        }

        public void setTemperature_three(String temperature_three) {
            this.temperature_three = temperature_three;
        }

        public String getWeather_three() {
            return weather_three;
        }

        public void setWeather_three(String weather_three) {
            this.weather_three = weather_three;
        }

        public String getImg_three() {
            return img_three;
        }

        public void setImg_three(String img_three) {
            this.img_three = img_three;
        }

        public String getDate_four() {
            return date_four;
        }

        public void setDate_four(String date_four) {
            this.date_four = date_four;
        }

        public String getTemperature_four() {
            return temperature_four;
        }

        public void setTemperature_four(String temperature_four) {
            this.temperature_four = temperature_four;
        }

        public String getWeather_four() {
            return weather_four;
        }

        public void setWeather_four(String weather_four) {
            this.weather_four = weather_four;
        }

        public String getImg_four() {
            return img_four;
        }

        public void setImg_four(String img_four) {
            this.img_four = img_four;
        }

        @Override
        public String toString() {
            return "WeatherDataEntity{" +
                    "city_name='" + city_name + '\'' +
                    ", city_id='" + city_id + '\'' +
                    ", date='" + date + '\'' +
                    ", pm25='" + pm25 + '\'' +
                    ", quality='" + quality + '\'' +
                    ", des='" + des + '\'' +
                    ", weather_info='" + weather_info + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", img='" + img + '\'' +
                    ", temperature_hight='" + temperature_hight + '\'' +
                    ", temperature_low='" + temperature_low + '\'' +
                    ", date_one='" + date_one + '\'' +
                    ", temperature_one='" + temperature_one + '\'' +
                    ", weather_one='" + weather_one + '\'' +
                    ", img_one='" + img_one + '\'' +
                    ", date_two='" + date_two + '\'' +
                    ", temperature_two='" + temperature_two + '\'' +
                    ", weather_two='" + weather_two + '\'' +
                    ", img_two='" + img_two + '\'' +
                    ", date_three='" + date_three + '\'' +
                    ", temperature_three='" + temperature_three + '\'' +
                    ", weather_three='" + weather_three + '\'' +
                    ", img_three='" + img_three + '\'' +
                    ", date_four='" + date_four + '\'' +
                    ", temperature_four='" + temperature_four + '\'' +
                    ", weather_four='" + weather_four + '\'' +
                    ", img_four='" + img_four + '\'' +
                    '}';
        }
    }
}
