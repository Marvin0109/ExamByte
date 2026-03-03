package exambyte.web.form.load_old_submit_data;

import java.util.List;
import java.util.UUID;

public record OldDataForm(
        UUID examId,
        String examTitle,
        List<OldDataDTO> components) {}
