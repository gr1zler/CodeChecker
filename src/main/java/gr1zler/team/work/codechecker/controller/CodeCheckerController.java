package gr1zler.team.work.codechecker.controller;

import gr1zler.team.work.codechecker.dto.CodeSubmissionDTO;
import gr1zler.team.work.codechecker.dto.TaskDTO;
import gr1zler.team.work.codechecker.dto.TestCaseDTO;
import gr1zler.team.work.codechecker.model.CodeSubmission;
import gr1zler.team.work.codechecker.model.Task;
import gr1zler.team.work.codechecker.model.TestCase;

import gr1zler.team.work.codechecker.service.CodeSubmissionService;
import gr1zler.team.work.codechecker.service.JwtService;
import gr1zler.team.work.codechecker.service.TaskService;
import gr1zler.team.work.codechecker.service.TestCaseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CodeCheckerController {

    private final TaskService taskService;
    private final CodeSubmissionService codeSubmissionService;
    private final JwtService jwtService;
    private final TestCaseService testCaseService;

    public CodeCheckerController(TaskService taskService,
                                 CodeSubmissionService codeSubmissionService,
                                 JwtService jwtService,
                                 TestCaseService testCaseService){
        this.taskService=taskService;
        this.codeSubmissionService=codeSubmissionService;
        this.jwtService=jwtService;
        this.testCaseService=testCaseService;
    }
    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id){
        Task task=taskService.getTaskWithExampleTestCase(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }
    @PostMapping("/task")
    public ResponseEntity<Task> addTask(@RequestBody @Valid TaskDTO taskDTO){
        Task task=taskService.addTask(taskDTO);
        return ResponseEntity.ok(task);
    }
    @PostMapping("/task/testCase")
    public ResponseEntity<TestCase> addTestCase(@RequestBody @Valid TestCaseDTO testCaseDTO) {
        TestCase testCase = testCaseService.addTestCase(testCaseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(testCase);
    }
    @PostMapping("/task/submission")
    public ResponseEntity<String>doSubmission(@RequestBody @Valid CodeSubmissionDTO codeSubmissionDTO
            ,@RequestHeader("Authorization") String token){

        String username = jwtService.extractUsername(token.replace("Bearer ", "")); // Remove "Bearer " prefix
        return codeSubmissionService.CodeSubmissionCheck(codeSubmissionDTO, username);

    }
}
