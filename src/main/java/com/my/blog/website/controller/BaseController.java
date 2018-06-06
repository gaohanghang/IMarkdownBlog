package com.my.blog.website.controller;

import com.my.blog.website.modal.Vo.UserVo;
import com.my.blog.website.utils.TaleUtils;
import com.my.blog.website.utils.MapCache;

import javax.servlet.http.HttpServletRequest;

/**
 *
 *
 * Created by GaoHangHang.
 */
public abstract class BaseController {

    // 使用的主题
    public static String THEME = "themes/default";

    // Map缓存
    protected MapCache cache = MapCache.single();

    /**
     * 主页的页面主题
     * @param viewName
     * @return 默认主题下的页面字符串
     */
    public String render(String viewName) {
        return THEME + "/" + viewName;
    }

    // 标题
    public BaseController title(HttpServletRequest request, String title) {
        request.setAttribute("title", title);
        return this;
    }

    // 关键字
    public BaseController keywords(HttpServletRequest request, String keywords) {
        request.setAttribute("keywords", keywords);
        return this;
    }

    /**
     * 获取请求绑定的登录对象
     * @param request
     * @return
     */
    public UserVo user(HttpServletRequest request) {
        return TaleUtils.getLoginUser(request);
    }

    public Integer getUid(HttpServletRequest request){
        return this.user(request).getUid();
    }

    public String render_404() {
        return "comm/error_404";
    }

}
