package code.repository;

import code.connection.ConnectionFactory;
import code.domain.Task;
import code.service.TaskService;
import lombok.extern.log4j.Log4j2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

@Log4j2
public class TaskRepository {
    public static void save(Task task) {
        String sql = """
                INSERT INTO `to_do_list`.`task` (`title`, `description`, `deadline`, `priority`, `done`) VALUES\s
                ('%s', '%s', '%s', '%d', b'%d')""".formatted(task.getTitle(), task.getDescription(), task.getDeadline()
                , task.getPriority(), task.getDone());
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
            log.info("Inserted task with success.");
        } catch (SQLException | InputMismatchException e) {
            log.info("It's not possible to save.", e);
        }
    }

    private static List<Task> find(String sql) {
        List<Task> tasks = new ArrayList<>();
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement();
        ) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                tasks.add(TaskService.buildTask(resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getDate("deadline"),
                        resultSet.getInt("priority"),
                        resultSet.getInt("done")));
            }
        } catch (SQLException e) {
            log.info("It´s not possible findAll.", e);
        }
        return tasks;
    }

    public static List<Task> findAll() {
        String sql = """
                SELECT * FROM to_do_list.task;
                """;
        return find(sql);
    }

    public static List<Task> findAllDone() {
        String sql = """
                SELECT * FROM to_do_list.task WHERE done = 1;
                """;
        return find(sql);
    }

    public static List<Task> findAllNotDone() {
        String sql = """
                SELECT * FROM to_do_list.task WHERE done = 0;
                """;
        return find(sql);
    }

    public static void delete(Integer id) {
        String sql = """
                DELETE FROM `to_do_list`.`task` WHERE (`id` = '%d');
                """.formatted(id);
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.info("The program cannot delete the task, try again later.");
        }
    }

    public static void update(Task task, Integer id) {
        String sql = """
                UPDATE `to_do_list`.`task` SET `title` = '%s', `description` = '%s', `deadline` = '%s', `priority` = '%d' WHERE (`id` = '%d');
                """.formatted(task.getTitle(), task.getDescription(), task.getDeadline(), task.getPriority(), id);
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.info("It´s not possible update the task '{}'", id, e);
        }
    }

    public static void updateStatus(Integer id) {
        String sql = "UPDATE `to_do_list`.`task` SET `done` = b'1' WHERE `id` = '%d'".formatted(id);
        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            log.info("It's not possible mark as done the task '{}'", id, e);
        }
    }
}
