package exambyte.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
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

    @GetMapping("/contact")
    public String contact(Model model, HttpServletRequest request, OAuth2AuthenticationToken auth) {
        System.out.println(auth);
        model.addAttribute("name", auth.getPrincipal().getAttribute("login"));
        model.addAttribute("currentPath", request.getRequestURI());
        return "contact";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/exams")
    @Secured("ROLE_ADMIN")
    public String exams(Model model) {
        System.out.println("Entered Exams");
        return "exams";
    }
    private void preventCaching(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }
    @PostMapping("/force-logout")
    public String forceLogout(HttpServletRequest request) {
        request.getSession(false).invalidate(); // Session invalidieren
        SecurityContextHolder.clearContext(); // Sicherheitskontext löschen
        return "redirect:/";
    }
}