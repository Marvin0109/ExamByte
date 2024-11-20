package exambyte.main.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;
public class GlobalExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandlerFoundException(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorMessage", "Die angeforderte Seite wurde nicht gefunden.");
        return "error/404";
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public String postNotWorking(NoHandlerFoundException exception, Model model) {
        model.addAttribute("errorMessage", "POST Requests sind noch nicht erlaubt.");
        return "error/405";
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String forbiddenAccess(NoHandlerFoundException exception, Model model) {
        model.addAttribute("errorMessage", "Keine Berechtigung für die Seite.");
        return "error/403";
    }
}
