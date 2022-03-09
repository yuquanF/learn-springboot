package com.f.missyou.exception;

import com.f.missyou.exception.http.HttpException;

public class DeleteSuccess extends HttpException {
    public DeleteSuccess(int code) {
        this.code = code;
        this.httpStatusCode = 200;
    }
}
