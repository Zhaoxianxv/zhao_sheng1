package com.yfy.app.video.beans;

import com.yfy.app.ebook.EbookBean;
import com.yfy.app.footbook.Foot;

import java.util.List;

/**
 * Created by yfyandr on 2017/8/31.
 */

public class VideoInfo {

    /**
     * result : true
     * error_code :
     */
    private String result;
    private String error_code;
    public void setResult(String result) {
        this.result = result;
    }
    public void setError_code(String error_code) {
        this.error_code = error_code;
    }
    public String getResult() {
        return result;
    }
    public String getError_code() {
        return error_code;
    }


    /**
     * video_list
     */

    private List<Videobean> video_list;

    public List<Videobean> getVideo_list() {
        return video_list;
    }

    public void setVideo_list(List<Videobean> video_list) {
        this.video_list = video_list;
    }

    /**
     *
     */

    private List<Tag> video_tag;

    public List<Tag> getVideo_tag() {
        return video_tag;
    }

    public void setVideo_tag(List<Tag> video_tag) {
        this.video_tag = video_tag;
    }



    /**
     * book_list
     */
    private List<EbookBean> book_list;

    public List<EbookBean> getBook_list() {
        return book_list;
    }

    public void setBook_list(List<EbookBean> book_list) {
        this.book_list = book_list;
    }


    /**
     * book_tag
     */

    private List<Tag> book_tag;
    public List<Tag> getBook_tag() {
        return book_tag;
    }
    public void setBook_tag(List<Tag> book_tag) {
        this.book_tag = book_tag;
    }

    /**
     * footbook
     */
    private String page_count;

    private List<Foot> cookbook;

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }

    public List<Foot> getCookbook() {
        return cookbook;
    }

    public void setCookbook(List<Foot> cookbook) {
        this.cookbook = cookbook;
    }
}
