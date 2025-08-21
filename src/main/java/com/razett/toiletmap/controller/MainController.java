package com.razett.toiletmap.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 기본(Root) Controller
 *
 * @since 2025-05-25 v1.0.0
 * @author JiwonJeong
 * @version 1.0.0
 */
@Controller
@RequestMapping("/")
public class MainController {

    @RequestMapping("/")
    private String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
