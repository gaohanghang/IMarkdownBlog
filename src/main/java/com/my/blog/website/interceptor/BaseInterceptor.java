package com.my.blog.website.interceptor;

import com.my.blog.website.modal.Vo.UserVo;
import com.my.blog.website.service.IUserService;
import com.my.blog.website.utils.*;
import com.my.blog.website.constant.WebConst;
import com.my.blog.website.dto.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义拦截器
 *
 * 发生在控制层之前
 */
@Component
public class BaseInterceptor implements HandlerInterceptor {
    // 初始化日志对象给常量LOGGE
    private static final Logger LOGGE = LoggerFactory.getLogger(BaseInterceptor.class);
    // 字符串常量 用户的客户端
    private static final String USER_AGENT = "user-agent";

    @Resource
    private IUserService userService; // 注入userService实例

    // 获取map缓存
    private MapCache cache = MapCache.single();

    @Resource
    private Commons commons; // 注入主题公共函数对象

    @Resource
    private AdminCommons adminCommons; // 注入后台公共函数对象


    /*
        该方法将在请求处理之前进行调用
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 获取请求的URI
        // 比如URL:http://localhost:8080/myapp/admin/login.html
        // 那么URI就是URI:/myapp/admin/login.html
        String uri = request.getRequestURI();

        // 日志打印用户使用设备信息
        LOGGE.info("UserAgent: {}", request.getHeader(USER_AGENT));
        // 日志打印用户访问地址，来路地址
        LOGGE.info("用户访问地址: {}, 来路地址: {}", uri, IPKit.getIpAddrByRequest(request));

        //请求拦截处理 从session获取用户对象
        UserVo user = TaleUtils.getLoginUser(request);
        // 如果用户为空，说明没有登录
        if (null == user) {
            // 从cookie中获取用户id
            Integer uid = TaleUtils.getCookieUid(request);
            if (null != uid) {
                //这里还是有安全隐患,cookie是可以伪造的 通过用户id获取用户对象
                user = userService.queryUserById(uid);
                // 在session中保存用户数据 ("login_user",user)
                request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
            }
        }
        // 如果uri为admin且不是/admin/login且用户信息为空就跳转到登录页面
        if (uri.startsWith("/admin") && !uri.startsWith("/admin/login") && null == user) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return false;
        }


        /*
            第一步：后端随机产生一个token，把这个token保存在SESSION状态中；同时，后端把这个token交给前端页面；

            第二步：下次前端需要发起请求（比如发帖）的时候把这个token加入到请求数据或者头信息中，一起传给后端；

            第三步：后端校验前端请求带过来的token和SESSION里的token是否一致；
         */
        //设置get请求的token
        if (request.getMethod().equals("GET")) {
            String csrf_token = UUID.UU64();
            // 默认存储30分钟
            cache.hset(Types.CSRF_TOKEN.getType(), csrf_token, uri, 30 * 60);
            request.setAttribute("_csrf_token", csrf_token);
        }
        return true;
    }

    /**
     * 处理请求
     * httpServletRequest.setAttribute request范围内设置一个属性主要用来存值供其他页面操作
     * @param httpServletRequest
     * @param httpServletResponse
     * @param o
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        httpServletRequest.setAttribute("commons", commons);//一些工具类和公共方法
        httpServletRequest.setAttribute("adminCommons", adminCommons);
    }

    /*
        该方法也是需要当前对应的Interceptor 的preHandle 方法的返回值为true 时才会执行。
        顾名思义，该方法将在整个请求结束之后，也就是在DispatcherServlet
         渲染了对应的视图之后执行。这个方法的主要作用是用于进行资源清理工作的。
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
