package exambyte.web.controllers;

import exambyte.application.service.ExamControllerService;
import exambyte.application.service.ExamManagementService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;
import java.util.UUID;

@Controller
public class WebController {

    private final ExamControllerService service;

    public WebController(ExamControllerService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        service.saveAutomaticReviewer();
        model.addAttribute("currentPath", request.getRequestURI());
        return "index";
    }

    @GetMapping("/contact")
    @Secured({"ROLE_STUDENT", "ROLE_REVIEWER", "ROLE_ADMIN"})
    public String contact(Model model, HttpServletRequest request, OAuth2AuthenticationToken auth) {
        String name = auth.getPrincipal().getAttribute("login");
        Optional<UUID> fachId = service.getProfFachIDByName(name);
        String id = "Keine FachID vorhanden! Kontaktieren Sie den Admin.";

        if(fachId.isPresent()){
            id = fachId.get().toString();
        }

        model.addAttribute("name", auth.getPrincipal().getAttribute("login"));
        model.addAttribute("fachID", id);
        model.addAttribute("currentPath", request.getRequestURI());
        return "contact";
    }

    @GetMapping("/settings")
    @Secured("ROLE_ADMIN")
    public String showSettings(Model model, HttpServletRequest request) {
        model.addAttribute("currentPath", request.getRequestURI());
        return "settings";
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
        request.getSession(false).invalidate(); // Session invalidieren
        SecurityContextHolder.clearContext(); // Sicherheitskontext löschen
        return "redirect:/";
    }
}