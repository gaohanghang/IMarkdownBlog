package com.my.blog.website.controller.admin;

import com.my.blog.website.constant.WebConst;
import com.my.blog.website.controller.BaseController;
import com.my.blog.website.dto.LogActions;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.modal.Bo.RestResponseBo;
import com.my.blog.website.modal.Vo.UserVo;
import com.my.blog.website.service.ILogService;
import com.my.blog.website.service.IUserService;
import com.my.blog.website.utils.Commons;
import com.my.blog.website.utils.TaleUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户后台登录/登出
 *
 */
@Controller
@RequestMapping("/admin")
/*
    声明事务，当出现TipException异常时，进行回滚
*/
@Transactional(rollbackFor = TipException.class)
public class AuthController extends BaseController {

    /*
        LoggerFactory是一个为各种日志记录API记录器的实用程序类，最值得注意的是log4j、logback
        and logging
        Logger 日志记录器
    */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Resource
    private IUserService usersService; // 注入日志Service对象

    @Resource
    private ILogService logService; // 注入日志Service对象

    @GetMapping(value = "/login")  // get请求 路径为/admin/login
    public String login() {
        return "admin/login"; // 返回admin/login.html页面
    }

    /**
     * 管理后台登录
     * @param username 用户名
     * @param password 密码
     * @param remeber_me 记住我
     * @param request 请求
     * @param response 响应
     * @return
     *
     * required=false表示不传的话，会给参数赋值为null，required=true就是必须要有
     */
    @PostMapping(value = "login")
    @ResponseBody
    public RestResponseBo doLogin(@RequestParam String username,
                                  @RequestParam String password,
                                  @RequestParam(required = false) String remeber_me,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {

        // 从缓存中获取登录错误次数
        Integer error_count = cache.get("login_error_count");
        try {
            // 使用用户Service的login方法登录
            UserVo user = usersService.login(username, password);
            request.getSession().setAttribute(WebConst.LOGIN_SESSION_KEY, user);
            if (StringUtils.isNotBlank(remeber_me)) {
                TaleUtils.setCookie(response, user.getUid());
            }
            logService.insertLog(LogActions.LOGIN.getAction(), null, request.getRemoteAddr(), user.getUid());
        } catch (Exception e) {
            error_count = null == error_count ? 1 : error_count + 1;
            if (error_count > 3) {
                return RestResponseBo.fail("您输入密码已经错误超过3次，请10分钟后尝试");
            }
            cache.set("login_error_count", error_count, 10 * 60);
            String msg = "登录失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }

    /**
     * 注销
     * @param session
     * @param response
     */
    @RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response, HttpServletRequest request) {
        // 注销后清空指定的login_user属性 ，就是从session中删除login_user名称的绑定对象
        session.removeAttribute(WebConst.LOGIN_SESSION_KEY); // session.removeAttribute(login_user)
        /*
            删除cookie
            cookie对象是一个key-value数值对，key表示cookie的名字，value表示存放的数据，可以是任何对象
         */
        // 删除某个Cookie时，只需要新建一个同名Cookie，然后添加到response中覆盖原来的Cookie
        Cookie cookie = new Cookie(WebConst.USER_IN_COOKIE, ""); // 新建cookie对象
        cookie.setMaxAge(0);                                               // 设置生命周期为0，表示将要删除
        response.addCookie(cookie);                                        //  执行添加后就从response里删除了
        try {
            // 退出后重定向到登录界面 重定向到admin/login
            //response.sendRedirect(Commons.site_url());
            response.sendRedirect(Commons.site_login());
        } catch (IOException e) {
            e.printStackTrace();
            // 如果抛出IO异常就打印注销失败信息
            // logger.error如等级设为Error的话，warn,info,debug的信息不会输出 打印错误堆栈信息
            LOGGER.error("注销失败", e);
        }
    }
}
