package com.saigou.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ResponseVO<T> implements Serializable {
    //        privatestaticfinallong serialVersionUID = -1005863670741860901L;
// 响应码
    private Integer code;

    // 描述信息
    private String message;

    // 响应内容
    private T data;
    private Long timestamp;

    private ResponseVO(ResponseEnum responseCode) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.timestamp = new Date().getTime()/1000;
    }

    private ResponseVO(ResponseEnum responseCode, T data) {
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
        this.data = data;
        this.timestamp = new Date().getTime()/1000;
    }

    private ResponseVO(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = new Date().getTime()/1000;
    }




    public static <T> ResponseVO massage(boolean a, T success, T err) {
        if(a){
            return new ResponseVO<>(ResponseEnum.SUCCESS, success);
        }else {
            return new ResponseVO<>(ResponseEnum.ERROR, err);
        }
    }

    /**
     * 返回成功信息
     *
     * @param data 信息内容
     * @param <T>
     * @return
     */
    public static <T> ResponseVO success(T data) {
        return new ResponseVO<>(ResponseEnum.SUCCESS, data);
    }

    /**
     * 返回成功信息
     *
     * @return
     */
    public static ResponseVO success() {
        return new ResponseVO(ResponseEnum.SUCCESS);
    }

    /**
     * 返回错误信息
     *
     * @param responseCode 响应码
     * @return
     */
    public static ResponseVO error(ResponseEnum responseCode) {
        return new ResponseVO(responseCode);
    }
    public static ResponseVO error(Integer code,String message) {
        return new ResponseVO(code,message);
    }
}