package gr1zler.team.work.codechecker.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private String title;
    private String problem;
    private String input;
    private String output;
}
