package code.domain;

import lombok.Builder;
import lombok.Value;

import java.sql.Date;

@Builder
@Value
public class Task {
    int id;
    String title;
    String description;
    Date deadline;
    Integer priority;
    Integer done;

    @Override
    public String toString() {
        return String.format("[%d] %s | Prioridade: %s | Prazo: %s | Status: %s",
                id, title, priority, deadline, (done == 0 ? "Done" : "Pending"));
    }
}
