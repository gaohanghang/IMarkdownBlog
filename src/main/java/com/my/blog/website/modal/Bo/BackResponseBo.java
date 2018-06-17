package com.my.blog.website.modal.Bo;

import java.io.Serializable;

/**
 * 文件备份业务对象
 */
public class BackResponseBo implements Serializable {

    // 文件路径
    private String attachPath;
    // 主题路径
    private String themePath;
    // sql路径
    private String sqlPath;

    public String getAttachPath() {
        return attachPath;
    }

    public void setAttachPath(String attachPath) {
        this.attachPath = attachPath;
    }

    public String getThemePath() {
        return themePath;
    }

    public void setThemePath(String themePath) {
        this.themePath = themePath;
    }

    public String getSqlPath() {
        return sqlPath;
    }

    public void setSqlPath(String sqlPath) {
        this.sqlPath = sqlPath;
    }
}
