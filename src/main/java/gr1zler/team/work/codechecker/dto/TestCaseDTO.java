package gr1zler.team.work.codechecker.dto;

import lombok.Data;

@Data
public class TestCaseDTO {
    private String inputData;
    private String expectedOutput;
    private Long taskId;
}
