package exambyte.persistence.mapper.JDBC;

import exambyte.domain.aggregate.exam.Exam;
import exambyte.persistence.entities.JDBC.ExamEntityJDBC;

public class ExamMapperJDBC {

    public ExamEntityJDBC toEntity(Exam exam) {
        return new ExamEntityJDBC(
                null,
                exam.getFachId(),
                exam.getTitle(),
                exam.getProfessorFachId());
    }

    public Exam toDomain(ExamEntityJDBC examEntityJDBC) {
       return Exam.of(
                null,
                examEntityJDBC.getFachId(),
                examEntityJDBC.getTitle(),
                examEntityJDBC.getProfessorFachId());
    }
}
