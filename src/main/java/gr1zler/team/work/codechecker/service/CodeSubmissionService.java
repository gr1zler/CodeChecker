package gr1zler.team.work.codechecker.service;

import gr1zler.team.work.codechecker.dto.CodeSubmissionDTO;
import gr1zler.team.work.codechecker.model.CodeSubmission;
import gr1zler.team.work.codechecker.model.SubmissionResult;
import gr1zler.team.work.codechecker.model.Task;
import gr1zler.team.work.codechecker.model.TestCase;
import gr1zler.team.work.codechecker.repository.CodeSubmissionRepository;
import gr1zler.team.work.codechecker.repository.SubmissionResultRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CodeSubmissionService {
    private final TaskService taskService;
    private final Judge0Service judge0Service;
    private final CodeSubmissionRepository codeSubmissionRepository;
    private final SubmissionResultRepository submissionResultRepository;

    public CodeSubmissionService(TaskService taskService,
                                 Judge0Service judge0Service,
                                 CodeSubmissionRepository codeSubmissionRepository,
                                 SubmissionResultRepository submissionResultRepository){
        this.taskService=taskService;
        this.judge0Service=judge0Service;
        this.codeSubmissionRepository=codeSubmissionRepository;
        this.submissionResultRepository=submissionResultRepository;
    }

    public ResponseEntity<String> CodeSubmissionCheck(CodeSubmissionDTO codeSubmissionDTO,String username){
        CodeSubmission codeSubmission=new CodeSubmission()
                .setLanguage(codeSubmissionDTO.getLanguage())
                .setSourceCode(codeSubmissionDTO.getSourceCode())
                .setTaskId(codeSubmissionDTO.getTaskId())
                .setUsername(username);
        Long submissionId=codeSubmissionRepository.save(codeSubmission).getId();
        Task task = taskService.getTaskWithExampleAllTestCase(codeSubmissionDTO.getTaskId());
        if (task == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }
        List<TestCase> testCases = task.getTestCases();
        if (testCases == null || testCases.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No test cases available for this task");
        }
        int passed=executeAndEvaluate(codeSubmissionDTO.getLanguage(), codeSubmissionDTO.getSourceCode(), testCases,submissionId);
        return ResponseEntity.ok("Passed " + passed + " out of " + testCases.size() + " test cases.");
    }
    private int executeAndEvaluate(String language, String sourceCode, List<TestCase> testCases,Long submissionId) {
        int passed = 0;
        for (TestCase testCase : testCases) {
            String expectedOutput = testCase.getExpectedOutput();
            SubmissionResult result =judge0Service.executeCode(language, sourceCode, testCase).setSubmissionId(submissionId);
            System.out.println(result.getStatus());
            submissionResultRepository.save(result);
            if (expectedOutput.equals(result.getStdOut())) {
                passed++;
            }
        }
        return passed;
    }



}
