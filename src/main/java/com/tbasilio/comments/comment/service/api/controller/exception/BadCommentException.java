package com.tbasilio.comments.comment.service.api.controller.exception;

public class BadCommentException extends RuntimeException{
    public BadCommentException(String message){
        super(message);
    }
}
