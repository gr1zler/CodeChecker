package gr1zler.team.work.codechecker.service;

import gr1zler.team.work.codechecker.dto.TaskDTO;
import gr1zler.team.work.codechecker.model.Task;
import gr1zler.team.work.codechecker.model.TestCase;
import gr1zler.team.work.codechecker.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    private  final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository=taskRepository;
    }

    public Task getTaskWithExampleTestCase(Long id){
        Task task=taskRepository.findById(id).orElse(null);
        if (task == null) {
            return null;
        }

        if (task.getTestCases() != null && !task.getTestCases().isEmpty()) {
            List<TestCase> singleTestCaseList = new ArrayList<>();
            singleTestCaseList.add(task.getTestCases().get(0));
            task.setTestCases(singleTestCaseList);
        }
        return task;
    }
    public Task getTaskWithExampleAllTestCase(Long id){
        Task task=taskRepository.findById(id).orElse(null);
        if (task == null) {
            return null;
        }
        return task;
    }
    public Task addTask(TaskDTO taskDTO){
        Task task=new Task()
                .setTitle(taskDTO.getTitle())
                .setProblem(taskDTO.getProblem())
                .setInput(taskDTO.getInput())
                .setOutput(taskDTO.getOutput());
        return taskRepository.save(task);
    }
}
