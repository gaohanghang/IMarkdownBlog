package com.my.blog.website.utils;


import com.my.blog.website.modal.Vo.MetaVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 后台公共函数
 */
@Component
public final class AdminCommons {

    /**
     * 判断category和cat的交集
     *
     * 判断category在catgorys字符中是否存在
     * @param cats
     * @return
     */
    public static boolean
    exist_cat(MetaVo category, String cats) {
        // 按,分割到String数组
        String[] arr = StringUtils.split(cats, ",");
        // 如果为不为null
        if (null != arr && arr.length > 0) {
            // 遍历
            for (String c : arr) {
                if (c.trim().equals(category.getName())) {
                    return true;
                }
            }
        }
        return false;
    }

    // 颜色数组
    private static final String[] COLORS = {"default", "primary", "success", "info", "warning", "danger", "inverse", "purple", "pink"};

    // 获取随机颜色字符串
    public static String rand_color() {
        int r = Tools.rand(0, COLORS.length - 1);
        return COLORS[r];
    }

}
