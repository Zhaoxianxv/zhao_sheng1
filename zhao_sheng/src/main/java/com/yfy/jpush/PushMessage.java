package com.yfy.jpush;

/**
 * Created by yfy1 on 2016/8/8.
 */
public class PushMessage {

    private String alert;
    private String sound;
    private int badge;

    private String title;
    private String description;
    private int builder_id;
    private int basic_style;
    private int open_type;
    private String url;
    private int user_confirm;
    private String pkg_content;
    private String custom_content;
    private int flag;

    public PushMessage(String alert, String sound, int badge, String title,
                       String description, int builder_id, int basic_style, int open_type,
                       String url, int user_confirm, String pkg_content,
                       String custom_content) {
        super();
        this.alert = alert;
        this.sound = sound;
        this.badge = badge;
        this.title = title;
        this.description = description;
        this.builder_id = builder_id;
        this.basic_style = basic_style;
        this.open_type = open_type;
        this.url = url;
        this.user_confirm = user_confirm;
        this.pkg_content = pkg_content;
        this.custom_content = custom_content;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBuilder_id() {
        return builder_id;
    }

    public void setBuilder_id(int builder_id) {
        this.builder_id = builder_id;
    }

    public int getBasic_style() {
        return basic_style;
    }

    public void setBasic_style(int basic_style) {
        this.basic_style = basic_style;
    }

    public int getOpen_type() {
        return open_type;
    }

    public void setOpen_type(int open_type) {
        this.open_type = open_type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getUser_confirm() {
        return user_confirm;
    }

    public void setUser_confirm(int user_confirm) {
        this.user_confirm = user_confirm;
    }

    public String getPkg_content() {
        return pkg_content;
    }

    public void setPkg_content(String pkg_content) {
        this.pkg_content = pkg_content;
    }

    public String getCustom_content() {
        return custom_content;
    }

    public void setCustom_content(String custom_content) {
        this.custom_content = custom_content;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "PushMessage [alert=" + alert + ", sound=" + sound + ", badge="
                + badge + ", title=" + title + ", description=" + description
                + ", builder_id=" + builder_id + ", basic_style=" + basic_style
                + ", open_type=" + open_type + ", url=" + url
                + ", user_confirm=" + user_confirm + ", pkg_content="
                + pkg_content + ", custom_content=" + custom_content
                + ", flag=" + flag + "]";
    }
}
