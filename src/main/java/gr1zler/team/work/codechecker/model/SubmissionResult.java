package gr1zler.team.work.codechecker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Table
@Entity(name = "submissionResult")
public class SubmissionResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long submissionId;
    @Lob  // Large Object (for large text storage)
    @Column(columnDefinition = "TEXT")
    private String stdIn;
    @Lob  // Large Object (for large text storage)
    @Column(columnDefinition = "TEXT")
    private String stdOut;
    @Lob  // Large Object (for large text storage)
    @Column(columnDefinition = "TEXT")
    private String expectedOut;
    private String status;
}
