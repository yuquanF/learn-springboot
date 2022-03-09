package com.f.missyou.exception;

import com.f.missyou.exception.http.HttpException;

public class CreateSuccess extends HttpException {
    public CreateSuccess(int code) {
        this.code = code;
        this.httpStatusCode = 201;
    }
}
