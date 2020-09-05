package com.yfy.app.cyclopedia.beans;

import java.util.List;

/**
 * Created by yfy1 on 2017/1/20.
 */

public class CycSppinner {

    /**
     * result : true
     * error_code :
     * angel_list : [{"id":"3","title":"形态特征"},{"id":"4","title":"生长习性"}]
     */

    private String result;
    private String error_code;
    private List<AngelListBean> angel_list;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<AngelListBean> getAngel_list() {
        return angel_list;
    }

    public void setAngel_list(List<AngelListBean> angel_list) {
        this.angel_list = angel_list;
    }

    public static class AngelListBean {
        /**
         * id : 3
         * title : 形态特征
         */

        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
