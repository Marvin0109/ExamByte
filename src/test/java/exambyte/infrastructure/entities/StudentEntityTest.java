package exambyte.infrastructure.entities;

import exambyte.infrastructure.persistence.entities.StudentEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StudentEntityTest {

    @ParameterizedTest
    @DisplayName("Field missing -> IllegalStateException")
    @MethodSource("invalidBuilder")
    void createStudentEntity_fail(StudentEntity.StudentEntityBuilder builder) {
        assertThrows(IllegalStateException.class, builder::build);
    }

    private static Stream<StudentEntity.StudentEntityBuilder> invalidBuilder() {
        return Stream.of(
                new StudentEntity.StudentEntityBuilder()
                        .name(" "),

                new StudentEntity.StudentEntityBuilder()
                        .name("")
        );
    }
}
