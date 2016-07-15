package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/7/14
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class PropertyPlacardEntity extends BaseEntity<PropertyPlacardEntity.DataBean>{

    public static class DataBean {
        /**
         * time : 0
         * datas : [{"sences":[{"time":1,"btn_name":"1464145578624","title":"这是标题","content":"这是内容"}]},{"sences":[{"time":1,"btn_name":"1464145578624","title":"这是标题","content":"这是内容"}]},{"sences":[{"time":1,"btn_name":"1464145578624","title":"这是标题","content":"这是内容"},{"time":1,"btn_name":"1464145578624","title":"这是标题","content":"这是内容"}]}]
         */

        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String time;
            private List<DatasBean> datas;

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public List<DatasBean> getDatas() {
                return datas;
            }

            public void setDatas(List<DatasBean> datas) {
                this.datas = datas;
            }

            public static class DatasBean {
                /**
                 * time : 1
                 * btn_name : 1464145578624
                 * title : 这是标题
                 * content : 这是内容
                 */

                private List<SencesBean> sences;

                public List<SencesBean> getSences() {
                    return sences;
                }

                public void setSences(List<SencesBean> sences) {
                    this.sences = sences;
                }

                public static class SencesBean {
                    private String time;
                    private String btn_name;
                    private String title;
                    private String content;

                    public String getTime() {
                        return time;
                    }

                    public void setTime(String time) {
                        this.time = time;
                    }

                    public String getBtn_name() {
                        return btn_name;
                    }

                    public void setBtn_name(String btn_name) {
                        this.btn_name = btn_name;
                    }

                    public String getTitle() {
                        return title;
                    }

                    public void setTitle(String title) {
                        this.title = title;
                    }

                    public String getContent() {
                        return content;
                    }

                    public void setContent(String content) {
                        this.content = content;
                    }
                }
            }
        }
    }
}
