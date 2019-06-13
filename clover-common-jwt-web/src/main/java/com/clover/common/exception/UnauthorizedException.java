package com.clover.common.exception;

/**
 * 身份认证异常
 * @author liugh
 * @since 2019-05-09
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String msg) {
        super(msg);
    }

    public UnauthorizedException() {
        super();
    }
}
