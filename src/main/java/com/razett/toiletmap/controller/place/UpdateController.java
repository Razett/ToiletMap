package com.razett.toiletmap.controller.place;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UpdateController {
    @GetMapping("/update")
    public String updateList() {
        return "update";
    }
}
