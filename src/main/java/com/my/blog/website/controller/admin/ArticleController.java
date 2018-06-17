package com.my.blog.website.controller.admin;


import com.github.pagehelper.PageInfo;
import com.my.blog.website.controller.BaseController;
import com.my.blog.website.dto.LogActions;
import com.my.blog.website.dto.Types;
import com.my.blog.website.exception.TipException;
import com.my.blog.website.modal.Bo.RestResponseBo;
import com.my.blog.website.modal.Vo.ContentVo;
import com.my.blog.website.modal.Vo.ContentVoExample;
import com.my.blog.website.modal.Vo.MetaVo;
import com.my.blog.website.modal.Vo.UserVo;
import com.my.blog.website.service.IContentService;
import com.my.blog.website.service.ILogService;
import com.my.blog.website.service.IMetaService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 文章管理
 *
 * Created by GaoHangHang.
 */
@Controller
@RequestMapping("/admin/article")
/*
    声明事务
    @Transactional(rollbackFor=Exception.class)
    当这个类里面的方法抛出运行时异常时，就会回滚，数据库里面的数据也会回滚。TipException是
    运行指定当出现运行时异常的子类TipException时RuntimeException的子类。
*/
@Transactional(rollbackFor = TipException.class)
public class ArticleController extends BaseController {

    /*
        LoggerFactory.getLogger(ArticleController.class);
        使用指定类初始化日志对象
        在日志输出的时候，可以打印出日志信息所在类
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleController.class);

    /*@Resource默认是按照名称来装配注入的，只有当找不到与名称匹配的bean才会按照类型来装配注入*/
    @Resource
    private IContentService contentsService;//内容Service

    @Resource
    private IMetaService metasService;//配置service   获取分类/便签信息

    @Resource
    private ILogService logService;//日志service  日志记录

    /**
     * 文章列表
     * @param page
     * @param limit
     * @param request
     * @return
     */
    @GetMapping(value = "")
    /*defaultValue 设置请求参数的默认值；*/
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
                        @RequestParam(value = "limit", defaultValue = "15") int limit, HttpServletRequest request) {
        // 动态生成sql语句
        ContentVoExample contentVoExample = new ContentVoExample();
        contentVoExample.setOrderByClause("created desc");//升序排列，desc为降序排列。
        contentVoExample.createCriteria().andTypeEqualTo(Types.ARTICLE.getType());
        // 通过PageHelper获取指定页数和数量的数据
        PageInfo<ContentVo> contentsPaginator = contentsService.getArticlesWithpage(contentVoExample,page,limit);
        request.setAttribute("articles", contentsPaginator);
        // 转发到文章列表页
        return "admin/article_list";
    }

    /**
     * 文章发表
     * @param request
     * @return
     */
    @GetMapping(value = "/publish")
    public String newArticle(HttpServletRequest request) {
        List<MetaVo> categories = metasService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories", categories);
        return "admin/article_edit";
    }

    /**
     * 文章编辑
     * @param cid
     * @param request
     * @return
     */
    @GetMapping(value = "/{cid}")
    public String editArticle(@PathVariable String cid, HttpServletRequest request) {
        // 获取内容
        ContentVo contents = contentsService.getContents(cid);
        request.setAttribute("contents", contents);
        // 获取标签
        List<MetaVo> categories = metasService.getMetas(Types.CATEGORY.getType());
        request.setAttribute("categories", categories);
        // 将发布文章选择激活
        request.setAttribute("active", "article");
        // 转发页面
        return "admin/article_edit";
    }

    /**
     * 文章发表
     * @param contents
     * @param request
     * @return
     */
    @PostMapping(value = "/publish")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo publishArticle(ContentVo contents,  HttpServletRequest request) {
        // 获取登陆对象
        UserVo users = this.user(request);
        contents.setAuthorId(users.getUid());
        contents.setType(Types.ARTICLE.getType());
        // 如果分类为空
        if (StringUtils.isBlank(contents.getCategories())) {
            // 设置分类为默认分类
            contents.setCategories("默认分类");
        }
        try {
            // 使用内容Service发布文章
            contentsService.publish(contents);
        } catch (Exception e) {
            String msg = "文章发布失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            // 发布失败就弹窗
            return RestResponseBo.fail(msg);
        }
        // 发布成功弹窗
        return RestResponseBo.ok();
    }

    /**
     * 文章更新
     * @param contents
     * @param request
     * @return
     */
    @PostMapping(value = "/modify")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo modifyArticle(ContentVo contents,HttpServletRequest request) {
        UserVo users = this.user(request);
        contents.setAuthorId(users.getUid());
        contents.setType(Types.ARTICLE.getType());
        try {
            contentsService.updateArticle(contents);
        } catch (Exception e) {
            String msg = "文章编辑失败";
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
     * 删除文章
     * @param cid
     * @param request
     * @return
     */
    @RequestMapping(value = "/delete")
    @ResponseBody
    @Transactional(rollbackFor = TipException.class)
    public RestResponseBo delete(@RequestParam int cid, HttpServletRequest request) {
        try {
            contentsService.deleteByCid(cid);
            logService.insertLog(LogActions.DEL_ARTICLE.getAction(), cid+"", request.getRemoteAddr(), this.getUid(request));
        } catch (Exception e) {
            String msg = "文章删除失败";
            if (e instanceof TipException) {
                msg = e.getMessage();
            } else {
                LOGGER.error(msg, e);
            }
            return RestResponseBo.fail(msg);
        }
        return RestResponseBo.ok();
    }
}
