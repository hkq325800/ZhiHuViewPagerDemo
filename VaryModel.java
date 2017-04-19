package zj.health.fjzl.hnrm.ui;

import java.io.Serializable;

/**
 * Created by hkq325800 on 2017/4/19.
 */

public class VaryModel implements Serializable {
    public int getZan() {
        return zan;
    }

    public void setZan(int zan) {
        this.zan = zan;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    int zan;
    int comment;
    int id;
    int page;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    String detail;

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    String user_icon;

    public VaryModel(int id, int zan, int comment, int page, String detail, String user_icon){
        this.id = id;
        this.zan = zan;
        this.comment = comment;
        this.page = page;
        this.detail = detail;
        this.user_icon = user_icon;
    }
}
