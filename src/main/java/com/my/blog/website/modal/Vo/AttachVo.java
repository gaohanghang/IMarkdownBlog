package com.my.blog.website.modal.Vo;

import java.io.Serializable;

/**
 * VO(view object) 值对象 视图对象，用于展示层，
 * 它的作用是把某个指定页面（或组件）的所有数据封装起来。
 *
 * 文件实体类
 * @author 
 */
public class AttachVo implements Serializable {
    /**
     * attach表主键
     */
    private Integer id;

    /**
     * 文件名称
     */
    private String fname;

    /**
     * 文件类型
     */
    private String ftype;

    /**
     * 文件路径
     */
    private String fkey;

    /**
     * 上传用户id
     */
    private Integer authorId;

    /**
     * 创建时间
     */
    private Integer created;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public String getFkey() {
        return fkey;
    }

    public void setFkey(String fkey) {
        this.fkey = fkey;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public Integer getCreated() {
        return created;
    }

    public void setCreated(Integer created) {
        this.created = created;
    }
}