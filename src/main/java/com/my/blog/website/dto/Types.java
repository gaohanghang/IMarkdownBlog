package com.my.blog.website.dto;

/*
    定义了一个类型枚举类
    带有type成员变量描述类型，定义了一个getType方法，返回类型，
    自定义私有构造函数，在声明枚举实例时传入对应的类型描述

     enum类可以像常规类一样声明变量或成员方法。若定义方法，
     务必在声明完枚举实例后使用分号分开，倘若在枚举实例前定义任何方法，
     编译器都将会报错，无法编译通过，同时即使自定义了构造函数且enum的定义结束，
     我们也永远无法手动调用构造函数创建枚举实例，毕竟这事只能由编译器执行。

     因为虚拟机在加载枚举类的类的时候，会使用ClassLoader的loadClass方法，
     而这个方法使用同步代码块保证了线程安全
*/
public enum Types {
    TAG("tag"),//标签
    CATEGORY("category"),//分类
    ARTICLE("post"),//文章
    PUBLISH("publish"),//发布
    PAGE("page"),//页面
    DRAFT("draft"),
    LINK("link"),
    IMAGE("image"),
    FILE("file"),
    CSRF_TOKEN("csrf_token"),
    COMMENTS_FREQUENCY("comments:frequency"),

    /**
     * 附件存放的URL，默认为网站地址，如集成第三方则为第三方CDN域名
     */
    ATTACH_URL("attach_url"),

    /**
     * 网站要过滤，禁止访问的ip列表
     */
    BLOCK_IPS("site_block_ips");


    private String type;// 类型

    // 返回类型字符串
    public java.lang.String getType() {
        return type;
    }

    // 设置类型
    public void setType(java.lang.String type) {
        this.type = type;
    }

    /*
         自定义私有构造函数，在声明枚举实例时传入对应的类型描述

         私有构造，防止被外部调用
    */
    Types(java.lang.String type) {
        this.type = type;
    }
}
