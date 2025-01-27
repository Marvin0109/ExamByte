package exambyte.domain.aggregate.user;

public class Student implements Person {

    private final Long id;
    private final String name;

    private Student(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    //Factory Methode
    public static Student of(Long id, String name) {
        return new Student(id, name);
    }

    @Override
    public Long getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
}