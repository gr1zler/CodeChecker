package gr1zler.team.work.codechecker.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table
@Entity(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "title")
    private String title;

    @Lob  // Large Object (for large text storage)
    @Column(columnDefinition = "TEXT",name = "problem")
    private String problem;

    @Lob  // Large Object (for large text storage)
    @Column(columnDefinition = "TEXT",name = "input")
    private String input;

    @Lob  // Large Object (for large text storage)
    @Column(columnDefinition = "TEXT",name = "output")
    private String output;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestCase> testCases;
}
