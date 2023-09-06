package com.main.bbangbbang.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloHomeController {
    @RequestMapping("/api/receive/tokenl.html")
    public String redirectToStaticResource() {

        return "forward:/receive-token.html";
    }
}
