package com.main.bbangbbang.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HelloHomeController {
    @RequestMapping("hello-oauth2")
    public String redirectToHome() {

        return "hello-oauth2";
    }
    @RequestMapping("receive-token")
    public String redirectToStaticResource() {

        return "receive-token";
    }
}
