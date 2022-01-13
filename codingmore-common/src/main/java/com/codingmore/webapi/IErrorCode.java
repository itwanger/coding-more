package com.codingmore.webapi;

/**
 * 封装API的错误码
 * Created by zhanglei on 2019/4/19.
 */
public interface IErrorCode {
    long getCode();

    String getMessage();
}
