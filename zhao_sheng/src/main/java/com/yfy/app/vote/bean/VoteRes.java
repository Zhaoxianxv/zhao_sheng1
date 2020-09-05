package com.yfy.app.vote.bean;

import java.util.List;

/**
 * Created by yfy1 on 2017/3/25.
 */

public class VoteRes {
    private List<VoteInfo> Votedetail;
    private String error_code;

    public String getError_code() {
        return error_code;
    }

    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    public List<VoteInfo> getVotedetail() {
        return Votedetail;
    }

    public void setVotedetail(List<VoteInfo> votedetail) {
        Votedetail = votedetail;
    }

    //-------------main_-------------------
    private List<Vote> Votetitle;

    public List<Vote> getVotetitle() {
        return Votetitle;
    }

    public void setVotetitle(List<Vote> votetitle) {
        Votetitle = votetitle;
    }
}
