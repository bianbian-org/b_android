package com.techjumper.polyhomeb.entity.medicalEntity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/19
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalUserLoginEntity extends BaseMedicalUserEntity<MedicalUserLoginEntity.MemberBean> {

    /**
     * id : 200000000003
     * username : user1
     * password :
     * email :
     * mobilePhone :
     * homePhone :
     * idcard :
     * idType : 1
     * pname :
     * sex : 1
     * birthday : 1978-05-02
     * enabled : 1
     * memberType : 0
     * nickname : user1
     * height : 172.0
     * weight : 76.0
     * intro :
     * weixinUid : null
     * company :
     * remark :
     * displayName : user1
     */

    public static class MemberBean {
        private String id;
        private String username;
        private String password;
        private String email;
        private String mobilePhone;
        private String homePhone;
        private String idcard;
        private int idType;
        private String pname;
        private int sex;
        private String birthday;
        private int enabled;
        private int memberType;
        private String nickname;
        private String height;
        private String weight;
        private String intro;
        private Object weixinUid;
        private String company;
        private String remark;
        private String displayName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobilePhone() {
            return mobilePhone;
        }

        public void setMobilePhone(String mobilePhone) {
            this.mobilePhone = mobilePhone;
        }

        public String getHomePhone() {
            return homePhone;
        }

        public void setHomePhone(String homePhone) {
            this.homePhone = homePhone;
        }

        public String getIdcard() {
            return idcard;
        }

        public void setIdcard(String idcard) {
            this.idcard = idcard;
        }

        public int getIdType() {
            return idType;
        }

        public void setIdType(int idType) {
            this.idType = idType;
        }

        public String getPname() {
            return pname;
        }

        public void setPname(String pname) {
            this.pname = pname;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public int getEnabled() {
            return enabled;
        }

        public void setEnabled(int enabled) {
            this.enabled = enabled;
        }

        public int getMemberType() {
            return memberType;
        }

        public void setMemberType(int memberType) {
            this.memberType = memberType;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public Object getWeixinUid() {
            return weixinUid;
        }

        public void setWeixinUid(Object weixinUid) {
            this.weixinUid = weixinUid;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }


        @Override
        public String toString() {
            return "MemberBean{" +
                    "id='" + id + '\'' +
                    ", username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", email='" + email + '\'' +
                    ", mobilePhone='" + mobilePhone + '\'' +
                    ", homePhone='" + homePhone + '\'' +
                    ", idcard='" + idcard + '\'' +
                    ", idType=" + idType +
                    ", pname='" + pname + '\'' +
                    ", sex=" + sex +
                    ", birthday='" + birthday + '\'' +
                    ", enabled=" + enabled +
                    ", memberType=" + memberType +
                    ", nickname='" + nickname + '\'' +
                    ", height='" + height + '\'' +
                    ", weight='" + weight + '\'' +
                    ", intro='" + intro + '\'' +
                    ", weixinUid=" + weixinUid +
                    ", company='" + company + '\'' +
                    ", remark='" + remark + '\'' +
                    ", displayName='" + displayName + '\'' +
                    '}';
        }
    }
}
