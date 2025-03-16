package gr1zler.team.work.codechecker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table
@Entity(name = "codeSubmission")
public class CodeSubmission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String language; // "java", "python", "cpp"
    @Lob
    @Column(columnDefinition = "TEXT")
    private String sourceCode;
    private Long taskId;
    private String username;
}
