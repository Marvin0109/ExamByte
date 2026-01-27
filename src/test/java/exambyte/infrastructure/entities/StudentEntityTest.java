package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.StudentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentEntityTest {

    @Test
    @DisplayName("StudentEntity-Fach-ID wird immer generiert")
    void createStudentEntity_success() {
        StudentEntity studentEntity = new StudentEntity.StudentEntityBuilder()
                .name("Student")
                .build();

        assertThat(studentEntity.getFachId()).isNotNull();
    }

    @ParameterizedTest
    @DisplayName("Pflichtfeld fehlt -> IllegalStateException")
    @MethodSource("ungueltigeBuilder")
    void createStudentEntity_fail(StudentEntity.StudentEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    private static Stream<StudentEntity.StudentEntityBuilder> ungueltigeBuilder() {
        return Stream.of(
                new StudentEntity.StudentEntityBuilder()
                        .name(" "),

                new StudentEntity.StudentEntityBuilder()
                        .name("")
        );
    }
}
