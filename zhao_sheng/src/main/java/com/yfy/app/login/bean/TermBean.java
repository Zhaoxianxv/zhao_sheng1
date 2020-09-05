package com.yfy.app.login.bean;

import java.util.List;

/**
 * Created by yfyandr on 2017/6/9.
 */

public class TermBean {

    /**
     * result : true
     * error_code :
     * term : [{"isnow":"0","name":"2015-2016上期","id":"1"},{"isnow":"1","name":"2016-2017上期","id":"2"}]
     */
    private String result;
    private String error_code;
    private List<TermEntity> term;

    public void setResult(String result) {
        this.result = result;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public void setTerm(List<TermEntity> term) {
        this.term = term;
    }

    public String getResult() {
        return result;
    }

    public String getError_code() {
        return error_code;
    }

    public List<TermEntity> getTerm() {
        return term;
    }

    public class TermEntity {
        /**
         * isnow : 0
         * name : 2015-2016上期
         * id : 1
         */
        private String isnow;
        private String name;
        private String id;

        public void setIsnow(String isnow) {
            this.isnow = isnow;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIsnow() {
            return isnow;
        }

        public String getName() {
            return name;
        }

        public String getId() {
            return id;
        }
    }
}
