package com.apps.developerblog.app.ws.exceptions;

import com.apps.developerblog.app.ws.ui.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value={Exception.class})
    public ResponseEntity<Object> handleAnyException(Exception e, WebRequest request){

        String errorMessageDescription = e.getLocalizedMessage();
        if(errorMessageDescription == null) errorMessageDescription = e.toString();
        ErrorMessage errorMessage = new ErrorMessage(new Date(), errorMessageDescription);

        return new ResponseEntity<>(
                errorMessage,
                new HttpHeaders(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
