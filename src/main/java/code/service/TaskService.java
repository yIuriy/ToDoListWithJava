package code.service;

import code.domain.Task;
import code.repository.TaskRepository;
import code.validator.Validator;
import lombok.extern.log4j.Log4j2;

import java.sql.Date;
import java.util.List;
import java.util.Scanner;

@Log4j2
public class TaskService {
    private static final Scanner scan = new Scanner(System.in);

    public static Date getDateFormatted() {
        System.out.println("Write the day that you want to the task expire: ");
        int day = Integer.parseInt(scan.nextLine());
        System.out.println("Write the month number that you want to the task expire: ");
        int month = Integer.parseInt(scan.nextLine());
        System.out.println("Write the year that you want to the task expire: ");
        int year = Integer.parseInt(scan.nextLine());
        String date = String.format("%s-%s-%s", year, month, day);
        return Validator.validateDate(date);
    }

    public static Integer getPriorityFormatted() {
        Integer priority;
        do {
            System.out.println("Which priority you want to give to this task(0 - 10): ");
            priority = Integer.parseInt(scan.nextLine());
        } while (!Validator.isValidPriority(priority));
        return priority;
    }


    public static void save(Task task) {
        TaskRepository.save(task);
    }

    public static void findAll() {
        List<Task> tasks = TaskRepository.findAll();
        System.out.println("=================================================================================================");
        tasks.forEach(System.out::println);
        System.out.println("=================================================================================================");
    }

    public static void findAllDone() {
        List<Task> tasks = TaskRepository.findAllDone();
        System.out.println("=================================================================================================");
        tasks.forEach(System.out::println);
        System.out.println("=================================================================================================");
    }

    public static void findAllNotDone() {
        List<Task> tasks = TaskRepository.findAllNotDone();
        System.out.println("=================================================================================================");
        tasks.forEach(System.out::println);
        System.out.println("=================================================================================================");
    }

    public static Task createTaskToInsertOnDB() {
        String title;
        String description;
        Date deadline;
        Integer priority;
        Scanner scan = new Scanner(System.in);
        System.out.println("Write the title of the task: ");
        title = scan.nextLine();
        System.out.println("Write a description for the task(optional): ");
        description = scan.nextLine();
        deadline = getDateFormatted();
        priority = getPriorityFormatted();
        return buildTaskToDB(title, description, deadline, priority);
    }

    private static Task buildTaskToDB(String title, String description, Date deadline, Integer priority) {
        return Task.builder()
                .title(title)
                .description(description)
                .deadline(deadline)
                .priority(priority)
                .done(0).build();
    }

    public static Task buildTask(Integer id, String title, String description, Date deadline, Integer priority, Integer done) {
        return Task.builder()
                .id(id)
                .title(title)
                .description(description)
                .deadline(deadline)
                .priority(priority)
                .done(done).build();
    }

    public static void delete(int option) {
        TaskRepository.delete(option);
    }

    public static void update(int id) {
        TaskRepository.update(createTaskToInsertOnDB(), id);
    }

    public static void changeStatusToDone(int idOfTaskToBeDone) {
        if (checkIfTheTaskIsDone(idOfTaskToBeDone)) {
            log.info("The task '{}' is already done.", idOfTaskToBeDone);
        } else {
            TaskRepository.updateStatus(idOfTaskToBeDone);
        }
    }

    private static boolean checkIfTheTaskIsDone(int id) {
        List<Task> tasks = TaskRepository.findAllDone();
        for (Task task : tasks) {
            if (task.getId() == id && task.getDone() == 1) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDBEmpty() {
        return TaskRepository.findAll().isEmpty();
    }

    public static boolean isSomeTaskNotDone(){
        return TaskRepository.findAllNotDone().isEmpty();
    }
}
