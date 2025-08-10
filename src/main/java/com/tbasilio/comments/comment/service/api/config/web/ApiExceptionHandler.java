package com.tbasilio.comments.comment.service.api.config.web;

import com.tbasilio.comments.comment.service.api.client.ModerationClientBadGatewayException;
import com.tbasilio.comments.comment.service.api.controller.exception.BadCommentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.nio.channels.ClosedChannelException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
            SocketTimeoutException.class,
            ConnectException.class,
            ClosedChannelException.class
    })
    public ProblemDetail handle(IOException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.GATEWAY_TIMEOUT);

        problemDetail.setTitle("Gateway timeout");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("errors/gateway-timeout"));

        return problemDetail;
    }

    @ExceptionHandler(ModerationClientBadGatewayException.class)
    public ProblemDetail handle(ModerationClientBadGatewayException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);

        problemDetail.setTitle("Bad gateway");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("errors/bad-gateway"));

        return problemDetail;
    }

    @ExceptionHandler(BadCommentException.class)
    public ProblemDetail handle(BadCommentException e){
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        problemDetail.setTitle("Bad comment");
        problemDetail.setDetail(e.getMessage());
        problemDetail.setType(URI.create("errors/bad-comment"));

        return problemDetail;
    }
}
