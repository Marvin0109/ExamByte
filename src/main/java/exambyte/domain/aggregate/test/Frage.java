package exambyte.domain.aggregate.test;

import exambyte.domain.aggregate.user.Professor;

public class Frage {

    private final Integer id;
    private final String[] frageText;
    private final Professor professor;

    private Frage(Integer id, String[] frageText, Professor professor) {
        this.id = id;
        this.frageText = frageText;
        this.professor = professor;
    }

    // Factory Methode
    public static Frage of(Integer id, String[] frageText, Professor professor) {
        return new Frage(id, frageText, professor);
    }

    public Professor getProfessor() {
        return professor;
    }

    public String[] getFrageText() {
        return frageText;
    }

    public int getId() {
        return id;
    }
}
