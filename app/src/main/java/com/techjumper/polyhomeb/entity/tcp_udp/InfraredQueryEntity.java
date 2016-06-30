package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/3/24
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class InfraredQueryEntity extends BaseReceiveEntity<InfraredQueryEntity.ParamEntity> {

    public static class ParamEntity {
        private String sn;

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }
    }
}
