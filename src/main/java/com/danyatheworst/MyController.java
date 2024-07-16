package com.danyatheworst;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

    @GetMapping("/")
    public String test() {

        return "index";
    }

    @GetMapping("get")
    public String get() {

        int a = 123;
        System.out.println(123);

        return "index";
    }
}
