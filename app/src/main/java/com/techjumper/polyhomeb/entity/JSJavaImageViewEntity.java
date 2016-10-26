package com.techjumper.polyhomeb.entity;

import java.util.List;

/**
 * * * * * * * * * * * * * * * * * * * * * * *
 * Created by lixin
 * Date: 16/8/23
 * * * * * * * * * * * * * * * * * * * * * * *
 **/
public class JSJavaImageViewEntity {


    /**
     * method : ImageView
     * params : {"index":0,"images":["http://pl.techjumper.com/upload/images/978fe108a671dd8bc7de38edb1194e6f.jpg","http://pl.techjumper.com/upload/images/2b42f9d5780c6f4c8f70a179ee6b55c4.jpg","http://pl.techjumper.com/upload/images/1bfe741519be23c572521d30366a6e5c.jpg","http://pl.techjumper.com/upload/images/5c0ea19fb441ed1c63e145c6e0a39f86.jpg","http://pl.techjumper.com/upload/images/cf69f8d292bd69ca35e8b245238ace6b.jpg","http://pl.techjumper.com/upload/images/df738eed85de0f45b325967d95b32de4.jpg"]}
     */

    private String method;
    /**
     * index : 0
     * images : ["http://pl.techjumper.com/upload/images/978fe108a671dd8bc7de38edb1194e6f.jpg","http://pl.techjumper.com/upload/images/2b42f9d5780c6f4c8f70a179ee6b55c4.jpg","http://pl.techjumper.com/upload/images/1bfe741519be23c572521d30366a6e5c.jpg","http://pl.techjumper.com/upload/images/5c0ea19fb441ed1c63e145c6e0a39f86.jpg","http://pl.techjumper.com/upload/images/cf69f8d292bd69ca35e8b245238ace6b.jpg","http://pl.techjumper.com/upload/images/df738eed85de0f45b325967d95b32de4.jpg"]
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
        private int index;
        private List<String> images;

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }
}

