package code.service;

import code.domain.Task;
import lombok.extern.log4j.Log4j2;

import java.util.InputMismatchException;
import java.util.Scanner;

@Log4j2
public class MenuService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void menu() {
        TaskAlert taskAlert = new TaskAlert();
        int option = 1;
        while (option != 0) {
            taskAlert.run();
            showMainMenuOptions();
            System.out.print("Write the option: ");
            option = getValidOption();
            switch (option) {
                case 1:
                    try {
                        Task task = TaskService.createTaskToInsertOnDB();
                        TaskService.save(task);
                        System.out.println("A new task has been create with success.");
                    } catch (IllegalArgumentException e) {
                        log.info("Error on creating task process");
                    }
                    break;
                case 2:
                    showViewTaskMenuOptions();
                    option = getValidOption();
                    menuViewTask(option);
                    break;
                case 3:
                    if (TaskService.isDBEmpty()) {
                        System.out.println("No exists any task to be deleted.");
                    } else {
                        TaskService.findAll();
                        System.out.println("Write the id of the task to be deleted: ");
                        int idOfTaskToBeDeleted = getValidOption();
                        TaskService.delete(idOfTaskToBeDeleted);
                    }
                    break;
                case 4:
                    if (TaskService.isDBEmpty()) {
                        System.out.println("No exists any task to be updated.");
                    } else {
                        System.out.println("Write the id of the task to be updated: ");
                        int idOfTaskToBeUpdated = getValidOption();
                        TaskService.update(idOfTaskToBeUpdated);
                    }
                    break;
                case 5:
                    if (TaskService.isSomeTaskNotDone()) {
                        System.out.println("No exists any task to be done.");
                    } else {
                        System.out.println("Write the id of the task to be done: ");
                        int idOfTaskToBeDone = getValidOption();
                        TaskService.changeStatusToDone(idOfTaskToBeDone);
                    }
                    break;
                case 0:
                    break;
                default:
                    log.info("Invalid option.");
            }
        }
    }

    public static int getValidOption() {
        try {
            return Integer.parseInt(SCANNER.nextLine());
        } catch (InputMismatchException | NumberFormatException e) {
            return -1;
        }
    }

    public static void showMainMenuOptions() {
        System.out.println("""
                =================================================
                1 - Create a new task
                2 - View tasks\s
                3 - Delete a task
                4 - Update a task
                5 - Mark a task as done
                0 - Exit
                =================================================
                """);
    }

    public static void showViewTaskMenuOptions() {
        System.out.println("""
                =================================================
                1 - Show all tasks created
                2 - Show all task done
                3 - Show all task to be done
                =================================================
                """);
    }

    public static void menuViewTask(int option) {
        switch (option) {
            case 1 -> TaskService.findAll();
            case 2 -> TaskService.findAllDone();
            case 3 -> TaskService.findAllNotDone();
            default -> System.out.println("Invalid option.");
        }
    }
}
