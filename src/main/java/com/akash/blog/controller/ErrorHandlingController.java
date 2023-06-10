package com.akash.blog.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

public class ErrorHandlingController implements ErrorController {
    @RequestMapping("/error")
    public String handleError() {
        return "<h1>Something went wrong!</h1>";
    }
 
    public ModelAndView getErrorPath() {
        return new ModelAndView("error", "message", "Something went wrong");
    }
}
