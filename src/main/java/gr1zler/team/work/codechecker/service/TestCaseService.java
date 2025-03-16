package gr1zler.team.work.codechecker.service;

import gr1zler.team.work.codechecker.dto.TestCaseDTO;
import gr1zler.team.work.codechecker.model.Task;
import gr1zler.team.work.codechecker.model.TestCase;
import gr1zler.team.work.codechecker.repository.TaskRepository;
import gr1zler.team.work.codechecker.repository.TestCaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class TestCaseService {
    private  final TaskRepository taskRepository;
    private final TestCaseRepository testCaseRepository;

    public TestCaseService(TaskRepository taskRepository,
                           TestCaseRepository testCaseRepository){
        this.taskRepository=taskRepository;
        this.testCaseRepository=testCaseRepository;
    }
    public TestCase addTestCase(TestCaseDTO testCaseDTO){
        Task task=taskRepository.findById(testCaseDTO.getTaskId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Task not found with ID: " + testCaseDTO.getTaskId()));

        TestCase testCase=new TestCase()
                .setInputData(testCaseDTO.getInputData())
                .setExpectedOutput(testCaseDTO.getExpectedOutput())
                .setTask(task);

        return testCaseRepository.save(testCase);
    }
}
