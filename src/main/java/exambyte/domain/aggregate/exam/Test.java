package exambyte.domain.aggregate.exam;

import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateRoot;

import java.util.ArrayList;
import java.util.List;

@AggregateRoot
public class Test {

    @AggregateIdentifier
    private final Long id;
    private final String title;
    private final List<Frage> fragen = new ArrayList<>();
    private final List<Antwort> antworten = new ArrayList<>();

    public Test(Long id, String title, List<Frage> fragen) {
        if (fragen.isEmpty()) {
            throw new IllegalArgumentException("Test muss mindestens eine Frage enthalten");
        }
        this.id = id;
        this.title = title;
        this.fragen.addAll(fragen);
    }

    // Getter und Methoden für das Arbeiten mit Fragen und Antworten
}
