package com.f.missyou.exception;

import com.f.missyou.exception.http.HttpException;

public class UpdateSuccess extends HttpException{
    public UpdateSuccess(int code){
        this.code = code;
        this.httpStatusCode = 200;
    }

}
