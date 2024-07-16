package com.danyatheworst;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyController {

    private final SessionFactory sessionFactory;

    public MyController(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @GetMapping("/")
    public String test() {
        System.out.println("root");
        Session session = this.sessionFactory.getCurrentSession();


        return "index";
    }

    @GetMapping("get")
    public String get() {

        System.out.println("get");


        return "index";
    }
}
