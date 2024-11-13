package exambyte.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ContactController {

    @GetMapping("/contact")
    public String contact(Model model, HttpServletRequest request) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "contact";
    }
}
