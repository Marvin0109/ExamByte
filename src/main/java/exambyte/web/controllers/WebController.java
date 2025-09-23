package exambyte.web.controllers;

import exambyte.application.service.ExamManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Diese Klasse verwaltet die verschiedenen HTTP-Request-Handler für die Webanwendung,
 * einschließlich der Anzeige von Seiten wie der Startseite, Kontaktseite und Prüfungsliste.
 * Sie implementiert auch Sicherheitslogik, z.B. für den geschützen Zugriff auf die Prüfungsseite
 * und das erzwungene Abmelden.
 */
@Controller
public class WebController {

    private final ExamManagementService examManagementService;

    public WebController(ExamManagementService examManagementService) {
        this.examManagementService = examManagementService;
    }

    /**
     * Zeigt die Startseite an und fügt die aktuelle URL zur Ansicht hinzu.
     *
     * @param model Das Model, um Daten an die View zu übergeben.
     * @param request Das {@link HttpServletRequest}-Objekt, um die aktuelle URL zu ermitteln.
     * @return Der Name der View für die Startseite.
     */
    @GetMapping("/")
    public String index(Model model, HttpServletRequest request) {
        examManagementService.saveAutomaticReviewer();
        model.addAttribute("currentPath", request.getRequestURI());
        return "index";
    }

    /**
     * Zeigt die Kontaktseite an und fügt den Namen des authentifizierten Benutzers sowie die aktuelle URL zur Ansicht hinzu.
     *
     * @param model Das Model, um Daten an die View zu übergeben.
     * @param request Das {@link HttpServletRequest}-Objekt, um die aktuelle URL zu ermitteln.
     * @param auth Das {@link OAuth2AuthenticationToken}-Objekt, das Informationen zum authentifizierten Benutzer enthält.
     * @return Der Name der View für die Kontaktseite.
     */
    @GetMapping("/contact")
    @Secured({"ROLE_STUDENT", "ROLE_REVIEWER", "ROLE_ADMIN"})
    public String contact(Model model, HttpServletRequest request, OAuth2AuthenticationToken auth) {
        System.out.println(auth);
        String name = auth.getPrincipal().getAttribute("login");
        String fachID = examManagementService.getProfFachIDByName(name).get().toString();
        model.addAttribute("name", auth.getPrincipal().getAttribute("login"));
        model.addAttribute("fachID", fachID);
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
        examManagementService.reset();
        redirectAttributes.addFlashAttribute("message", "Daten wurden erfolgreich gelöscht!");
        redirectAttributes.addFlashAttribute("success", true);
        return "redirect:/settings";
    }

    /**
     * Zeigt die Login-Seite an.
     *
     * @return Der Name der View für die Login-Seite.
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // TODO: relevant?

    /**
     * Verhindert das Caching der Anwort.
     * Wird für sicherheitsrelevante Seiten verwendet.
     *
     * @param response Das {@link HttpServletResponse}-Objekt, um die Cache-Header zu setzen.
     */
    private void preventCaching(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
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