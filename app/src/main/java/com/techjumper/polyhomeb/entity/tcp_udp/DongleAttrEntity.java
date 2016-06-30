package com.techjumper.polyhomeb.entity.tcp_udp;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by zhaoyiding
 * Date: 16/5/8
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class DongleAttrEntity extends BaseReceiveEntity<DongleAttrEntity.DataEntity> {
    public static class DataEntity {
        private String channel;
        private String netnumaber;

        public String getChannel() {
            return channel;
        }

        public void setChannel(String channel) {
            this.channel = channel;
        }

        public String getNetnumaber() {
            return netnumaber;
        }

        public void setNetnumaber(String netnumaber) {
            this.netnumaber = netnumaber;
        }
    }
}
