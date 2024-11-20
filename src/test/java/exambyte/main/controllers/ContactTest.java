package exambyte.main.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(WebController.class)
public class ContactTest {

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("Die Kontaktseite ist erreichbar für GET-Requests")
    void test_01() throws Exception {
        mvc.perform(get("/contact"))
                .andExpect(status().isOk())
                .andExpect(view().name("contact"));
    }

    @Test
    @DisplayName("Der Kontakt-Endpunkt verarbeitet unerwartete Methoden korrekt")
    void test_02() throws Exception {
        mvc.perform(post("/contact"))
                .andExpect(status().isMethodNotAllowed());
    }
}
