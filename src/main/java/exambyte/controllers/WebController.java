package exambyte.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@Controller
public class WebController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}