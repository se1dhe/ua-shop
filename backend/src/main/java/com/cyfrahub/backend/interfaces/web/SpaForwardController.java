package com.cyfrahub.backend.interfaces.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping(value = {
        "/",
        "/market/**",
        "/orders/**",
        "/wallet/**",
        "/profile/**",
        "/catalog/**"
    })
    public String forwardSpaRoutes() {
        return "forward:/index.html";
    }
}
