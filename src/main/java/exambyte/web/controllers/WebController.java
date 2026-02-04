package exambyte.web.controllers;

import exambyte.application.service.ExamControllerService;
import exambyte.application.service.UserCreationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
public class WebController {

    private final ExamControllerService service;
    private final UserCreationService userCreationService;
    private static final String CURRENT_PATH = "currentPath";

    public WebController(ExamControllerService service, UserCreationService userCreationService) {
        this.service = service;
        this.userCreationService = userCreationService;
    }

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        service.saveAutomaticReviewer();
        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        return "index";
    }

    @GetMapping("/contact")
    @Secured({"ROLE_STUDENT", "ROLE_REVIEWER", "ROLE_ADMIN"})
    public String contact(Model model, HttpServletRequest request, OAuth2AuthenticationToken auth) {
        model.addAttribute("name", auth.getPrincipal().getAttribute("login"));
        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        return "contact";
    }

    @GetMapping("/settings")
    @Secured({"ROLE_STUDENT", "ROLE_REVIEWER", "ROLE_ADMIN"})
    public String showSettings(Model model, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2User user = (OAuth2User) auth.getPrincipal();
        String name = user.getAttribute("login");

        String role = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(r -> !r.equals("OAUTH2_USER"))
                .findFirst()
                .orElse("No role yet");

        if (!role.contains("No role yet")) {
            role = role.substring(5).toLowerCase();
        }
        role = role.substring(0, 1).toUpperCase() + role.substring(1);

        model.addAttribute(CURRENT_PATH, request.getRequestURI());
        model.addAttribute("name", name);
        model.addAttribute("role", role);
        return "settings";
    }

    @PostMapping("/settings/role")
    @Secured({"ROLE_STUDENT", "ROLE_REVIEWER", "ROLE_ADMIN"})
    public String changeRole(@RequestParam String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        OAuth2User user = (OAuth2User) auth.getPrincipal();

        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));

        OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                user,
                authorities,
                "github"
        );

        SecurityContextHolder.getContext().setAuthentication(newAuth);
        userCreationService.createUser(user, authorities);

        return "redirect:/";
    }


    @PostMapping("/settings/reset")
    @Secured("ROLE_ADMIN")
    public String resetExamData(RedirectAttributes redirectAttributes) {
        service.reset();

        redirectAttributes.addFlashAttribute("message", "Daten wurden erfolgreich gelöscht!");
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/settings";
    }

    /**
     * Erzwingt das Abmelden des Benutzers, indem die Session invalidiert und der Sicherheitskontext gelöscht wird.
     *
     * @param request Das {@link HttpServletRequest}-Objekt, um die Session zu invalidieren.
     * @return Eine Weiterleitung zur Startseite nach dem Logout.
     */
    @PostMapping("/force-logout")
    public String forceLogout(HttpServletRequest request) {
        request.getSession(false).invalidate();
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }
}