package exambyte.web.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Diese Klasse fängt verschiedene HTTP-Fehler ab und gibt eine entsprechende Fehlerseite zurück.
 * - {@link NoHandlerFoundException} (404 Not Found) wird abgefangen und eine benutzerdefinierte 404-Fehlerseite angezeigt.
 * - POST-Requests, die nicht erlaubt sind, werden mit dem Status 405 (Method Not Allowed) behandelt und eine entsprechende Seite angezeigt.
 * - Zugriffsversuche auf Seiten ohne ausreichende Berechtigung werden mit dem Status 403 (Forbidden) abgefangen und eine entsprechende Fehlerseite zurückgegeben.
 * Jede dieser Methoden fängt spezifische HTTP-Fehler ab und leitet den Benutzer auf die passende Fehlerseite weiter.
 *
 * @see NoHandlerFoundException
 */
public class GlobalExceptionHandler {

    private static final String ERROR_MESSAGE = "errorMessage";

    /**
     * Behandelt den Fehler, wenn keine Handler für eine angeforderte URL gefunden werden (404 Not Found).
     *
     * @param ex Der {@link NoHandlerFoundException}-Fehler, der auftritt, wenn eine URL nicht gefunden wurde.
     * @param model Das {@link Model}-Objekt, das für die Übergabe von Attributen an die View verwendet wird.
     * @return Der Name der 404-Fehlerseite, die angezeigt werden soll.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(NoHandlerFoundException ex, Model model) {
        model.addAttribute(ERROR_MESSAGE, "Die angeforderte Seite wurde nicht gefunden.");
        return "error/404";
    }

    /**
     * Behandelt den Fehler, wenn ein POST-Request für eine nicht erlaubte Methode gemacht wird (405 Method Not Allowed).
     *
     * @param exception Der {@link NoHandlerFoundException}-Fehler, der auftritt, wenn ein POST-Request für eine nicht unterstützte
     *                  Methode gemacht wurde.
     * @param model Das {@link Model}-Objekt, das für die Übergabe von Attributen an die View verwendet wird.
     * @return Der Name der 405-Fehlerseite, die angezeigt werden soll.
     */
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String postNotWorking(NoHandlerFoundException exception, Model model) {
        model.addAttribute(ERROR_MESSAGE, "POST Requests sind noch nicht erlaubt.");
        return "error/405";
    }

    /**
     * Behandelt den Fehler, wenn der Benutzer keinen Zugriff auf eine Seite hat (403 Forbidden).
     *
     * @param exception Der {@link NoHandlerFoundException}-Fehler, der auftritt, wenn ein Benutzer ohne Berechtigung auf eine Seite
     *                  zugreift.
     * @param model Das {@link Model}-Objekt, das für die Übergabe von Attributen an die View verwendet wird.
     * @return Der Name der 403-Fehlerseite, die angezeigt werden soll.
     */
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String forbiddenAccess(NoHandlerFoundException exception, Model model) {
        model.addAttribute(ERROR_MESSAGE, "Keine Berechtigung für die Seite.");
        return "error/403";
    }
}
