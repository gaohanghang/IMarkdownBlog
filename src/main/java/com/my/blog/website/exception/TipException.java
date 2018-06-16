package com.my.blog.website.exception;

/*
    自定义异常

    自定义异常几个步骤：
        1.自定义一个类，继承自Exception类或其子类。
        2.重写父类Exception所有的公共方法
        3.重载构造函数

     try{
        throw exception; // 抛出异常
     }Catch(Exception plate){
        捕获异常
     }finally {
        See you!  //一般用于释放资源
     }
*/
public class TipException extends RuntimeException {

    // 无参构造器
    public TipException() {
    }

    // 有参构造器，有参构造器用来自定义异常信息
    public TipException(String message) {
        super(message);
    }

    // 用指定的详信息和原因构造一个新的异常
    public TipException(String message, Throwable cause) {
        super(message, cause);
    }

    // 用指定原因构造一个新的异常
    public TipException(Throwable cause) {
        super(cause);
    }

}
