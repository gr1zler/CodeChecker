package gr1zler.team.work.codechecker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodeSubmissionDTO {

    @NotBlank(message = "Language is required")
    private String language;

    @NotBlank(message = "Source code cannot be empty")
    private String sourceCode;

    @NotNull(message = "Task ID is required")
    private Long taskId;
}
