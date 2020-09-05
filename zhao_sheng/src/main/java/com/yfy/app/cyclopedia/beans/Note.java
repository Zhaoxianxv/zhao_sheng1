package com.yfy.app.cyclopedia.beans;

import java.util.List;

/**
 * Created by yfy1 on 2017/1/19.
 */

public class Note {

    /**
     * result : true
     * error_code :
     * count : 2
     * ancyclopedia_list : [{"title":"牵牛花分布范围","id":"8","angelid":"6","angel":"生长习性"},{"title":"牵牛花形态特征","id":"7","angelid":"5","angel":"形态特征"}]
     */

    private String result;
    private String error_code;
    private String count;
    private List<NoteBean> ancyclopedia_list;

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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<NoteBean> getAncyclopedia_list() {
        return ancyclopedia_list;
    }

    public void setAncyclopedia_list(List<NoteBean> ancyclopedia_list) {
        this.ancyclopedia_list = ancyclopedia_list;
    }
    public static class NoteBean {

        /**
         * title : 牵牛花生长习性
         * id : 10
         * author : 管理员
         * keyword : 自然
         * keyword_id : 2
         * tag_title : 生长习性
         * tag_id : 6
         * parentname : 牵牛花
         * parentid : 3
         * count : 0
         * content : 当地时间18时许，习近平在第71届联合国大会主席汤姆森和联合国秘书长古特雷斯陪同下步入万国宫大会厅，全场起立，热烈鼓掌欢迎。
         * praise : 0
         * ispraise : false
         * images : []
         * url : http://www.cdeps.sc.cn/bkdetail.aspx?id=10
         */
        private String title;
        private String id;
        private String author;
        private String keyword;
        private String keyword_id;
        private String tag_title;
        private String tag_id;
        private String parentname;
        private String parentid;
        private String count;
        private String content;
        private String praise;
        private String ispraise;
        private String url;
        private List<Images> images;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public String getKeyword_id() {
            return keyword_id;
        }

        public void setKeyword_id(String keyword_id) {
            this.keyword_id = keyword_id;
        }

        public String getTag_title() {
            return tag_title;
        }

        public void setTag_title(String tag_title) {
            this.tag_title = tag_title;
        }

        public String getTag_id() {
            return tag_id;
        }

        public void setTag_id(String tag_id) {
            this.tag_id = tag_id;
        }

        public String getParentname() {
            return parentname;
        }

        public void setParentname(String parentname) {
            this.parentname = parentname;
        }

        public String getParentid() {
            return parentid;
        }

        public void setParentid(String parentid) {
            this.parentid = parentid;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPraise() {
            return praise;
        }

        public void setPraise(String praise) {
            this.praise = praise;
        }

        public String getIspraise() {
            return ispraise;
        }

        public void setIspraise(String ispraise) {
            this.ispraise = ispraise;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public List<Images> getImages() {
            return images;
        }

        public void setImages(List<Images> images) {
            this.images = images;
        }
    }
}
