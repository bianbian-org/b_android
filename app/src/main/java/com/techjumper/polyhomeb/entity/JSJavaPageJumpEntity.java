package com.techjumper.polyhomeb.entity;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JSJavaPageJumpEntity {
    /**
     * method : PageJump
     * params : {"url":"http://pl.techjumper.com/neighbor/articles/show/69?title=%E5%B8%96%E5%AD%90%E8%AF%A6%E6%83%85&left=::NativeReturn&right="}
     */

    private String method;
    /**
     * url : http://pl.techjumper.com/neighbor/articles/show/69?title=%E5%B8%96%E5%AD%90%E8%AF%A6%E6%83%85&left=::NativeReturn&right=
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
        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
