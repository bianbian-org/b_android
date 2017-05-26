package com.techjumper.polyhomeb.entity.medicalEntity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/9/19
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class MedicalUserLoginEntity {

    /**
     * id : 100000001617
     * username : polyvip
     * password :
     * email : 79907475@qq.com
     * mobilePhone : 15010916860
     * homePhone : 123
     * idcard : 430102198002290510
     * idType : 1
     * pname : 陆浩
     * sex : 0
     * birthday : 1980-02-29
     * enabled : 1
     * memberType : 0
     * nickname : 陆昌博
     * height : 175.0
     * weight : 58.6
     * intro :
     * company : 北京紫薇宾馆
     * remark : 厨师
     * doctorStatus : 0
     * memberOrgan : null
     * doctorDeatil : null
     * doctorOrganization : null
     * displayName : 陆昌博
     */

    private MemberBean member;
    /**
     * member : {"id":"100000001617","username":"polyvip","password":"","email":"79907475@qq.com","mobilePhone":"15010916860","homePhone":"123","idcard":"430102198002290510","idType":1,"pname":"陆浩","sex":0,"birthday":"1980-02-29","enabled":1,"memberType":0,"nickname":"陆昌博","height":"175.0","weight":"58.6","intro":"","company":"北京紫薇宾馆","remark":"厨师","doctorStatus":0,"memberOrgan":null,"doctorDeatil":null,"doctorOrganization":null,"displayName":"陆昌博"}
     * status : 1
     */

    private int status;

    public MemberBean getMember() {
        return member;
    }

    public void setMember(MemberBean member) {
        this.member = member;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

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
        private String company;
        private String remark;
        private int doctorStatus;
        private Object memberOrgan;
        private Object doctorDeatil;
        private Object doctorOrganization;
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

        public int getDoctorStatus() {
            return doctorStatus;
        }

        public void setDoctorStatus(int doctorStatus) {
            this.doctorStatus = doctorStatus;
        }

        public Object getMemberOrgan() {
            return memberOrgan;
        }

        public void setMemberOrgan(Object memberOrgan) {
            this.memberOrgan = memberOrgan;
        }

        public Object getDoctorDeatil() {
            return doctorDeatil;
        }

        public void setDoctorDeatil(Object doctorDeatil) {
            this.doctorDeatil = doctorDeatil;
        }

        public Object getDoctorOrganization() {
            return doctorOrganization;
        }

        public void setDoctorOrganization(Object doctorOrganization) {
            this.doctorOrganization = doctorOrganization;
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
                    ", company='" + company + '\'' +
                    ", remark='" + remark + '\'' +
                    ", doctorStatus=" + doctorStatus +
                    ", memberOrgan=" + memberOrgan +
                    ", doctorDeatil=" + doctorDeatil +
                    ", doctorOrganization=" + doctorOrganization +
                    ", displayName='" + displayName + '\'' +
                    '}';
        }
    }
}
