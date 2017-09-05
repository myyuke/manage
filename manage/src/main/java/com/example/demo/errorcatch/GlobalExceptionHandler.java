package com.example.demo.errorcatch;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = MyException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, MyException e) throws Exception {
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setMessage(e.getMessage());
        r.setCode(ErrorInfo.PERERROR);
        r.setData("Don't have data.");
        r.setUrl(req.getRequestURL().toString());
        return r;
    }
    @ExceptionHandler(value = RequestException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, RequestException e) throws Exception {
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setMessage(e.getMessage());
        r.setCode(ErrorInfo.REQUESTERROR);
        r.setData("Don't have data.");
        r.setUrl(req.getRequestURL().toString());
        return r;
    }
    @ExceptionHandler(value = LoginException.class)
    @ResponseBody
    public ErrorInfo<String> jsonErrorHandler(HttpServletRequest req, LoginException e) throws Exception {
        ErrorInfo<String> r = new ErrorInfo<>();
        r.setMessage(e.getMessage());
        r.setCode(ErrorInfo.LOGINERROR);
        r.setData("Don't have data.");
        r.setUrl(req.getRequestURL().toString());
        return r;
    }

}
