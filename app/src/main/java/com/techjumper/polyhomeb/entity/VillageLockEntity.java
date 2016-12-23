package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 2016/12/22
 * * * * * * * * * * * * * * * * * * * * * * *
 **/

public class VillageLockEntity extends BaseEntity<VillageLockEntity.DataBean> {

    public static class DataBean {

        private List<UnitLocksBean> unit_locks;  //门口机
        private List<OutdoorLocksBean> outdoor_locks;   //围墙机

        public List<UnitLocksBean> getUnit_locks() {
            return unit_locks;
        }

        public void setUnit_locks(List<UnitLocksBean> unit_locks) {
            this.unit_locks = unit_locks;
        }

        public List<OutdoorLocksBean> getOutdoor_locks() {
            return outdoor_locks;
        }

        public void setOutdoor_locks(List<OutdoorLocksBean> outdoor_locks) {
            this.outdoor_locks = outdoor_locks;
        }

        public static class UnitLocksBean {
            /**
             * lock_id : 1
             * lock_name : 1栋1单元1号门口机
             */

            private int lock_id;
            private String lock_name;

            public int getLock_id() {
                return lock_id;
            }

            public void setLock_id(int lock_id) {
                this.lock_id = lock_id;
            }

            public String getLock_name() {
                return lock_name;
            }

            public void setLock_name(String lock_name) {
                this.lock_name = lock_name;
            }
        }

        public static class OutdoorLocksBean {
            /**
             * lock_id : 5
             * lock_name : 8号围墙机
             */

            private int lock_id;
            private String lock_name;

            public int getLock_id() {
                return lock_id;
            }

            public void setLock_id(int lock_id) {
                this.lock_id = lock_id;
            }

            public String getLock_name() {
                return lock_name;
            }

            public void setLock_name(String lock_name) {
                this.lock_name = lock_name;
            }
        }
    }
}
