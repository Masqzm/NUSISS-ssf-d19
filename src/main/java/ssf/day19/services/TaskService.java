package ssf.day19.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ssf.day19.config.Constants;
import ssf.day19.models.Task;
import ssf.day19.repo.TaskRepo;

@Service
public class TaskService {
    @Autowired
    TaskRepo taskRepo;

    public List<Task> getAllTasks() {
        return taskRepo.getAllTasks();
    }

    public List<Task> getFilteredStatusTasks(String qFilter) {
        List<Task> tasksList = taskRepo.getAllTasks();

        switch (qFilter) {
            case Constants.STATUS_ALL:
                return tasksList;

            case Constants.STATUS_PENDING:
            case Constants.STATUS_STARTED:
            case Constants.STATUS_INPROGRESS:
            case Constants.STATUS_COMPLETED:

                return tasksList.stream()
                        .filter(task -> task.getStatus().equals(qFilter))
                        .collect(Collectors.toList());
        
            default:
                return tasksList;
        }
    }    
}
