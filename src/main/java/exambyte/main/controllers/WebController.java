package exambyte.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "index";
    }
}