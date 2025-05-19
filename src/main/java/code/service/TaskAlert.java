package code.service;

import code.domain.Task;
import code.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

public class TaskAlert implements Runnable {
    @Override
    public void run() {
        int dayOfYearNow = LocalDate.now().getDayOfYear();
        List<Task> tasks = TaskRepository.findAllNotDone();

        for (Task task : tasks) {
            int dayOfYearOfTask = task.getDeadline().toLocalDate().getDayOfYear();
            int daysToExpire = Math.abs(dayOfYearNow - dayOfYearOfTask);
            if (daysToExpire <= 5 && task.getDone() == 0) {
                System.out.printf("[%d] - %s | Will be expire in %s days%n", task.getId(), task.getTitle(), daysToExpire);
            }
        }
    }
}
