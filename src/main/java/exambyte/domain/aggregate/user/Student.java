package exambyte.domain.aggregate.user;

public class Student implements Person {

    private final Integer id;
    private final String name;

    private Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    //Factory Methode
    public static Student of(Integer id, String name) {
        return new Student(id, name);
    }

    @Override
    public int getId() {
        return id;
    }
    @Override
    public String getName() {
        return name;
    }
}