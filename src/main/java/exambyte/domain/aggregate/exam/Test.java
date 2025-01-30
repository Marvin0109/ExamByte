package exambyte.domain.aggregate.exam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Test {

    private final Long id;
    private final UUID uuid;
    private final String title;
    private final List<Frage> fragen = new ArrayList<>();
    private final List<Antwort> antworten = new ArrayList<>();

    public Test(Long id, UUID uuid, String title, List<Frage> fragen) {
        if (fragen.isEmpty()) {
            throw new IllegalArgumentException("Test muss mindestens eine Frage enthalten");
        }
        this.uuid = uuid;
        this.id = id;
        this.title = title;
        this.fragen.addAll(fragen);
    }

    // Getter und Methoden für das Arbeiten mit Fragen und Antworten
}
