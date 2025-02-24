package taskList;
import command.Command;
import command.FindCmd;
import dukeexceptions.DeadlineEmptyException;
import dukeexceptions.EventEmptyException;
import dukeexceptions.InvalidCmd;
import dukeexceptions.InvalidDateTimeFormat;
import org.junit.jupiter.api.Test;
import parser.Parser;
import storage.Storage;
import task.Deadline;
import tasklist.TaskList;
import ui.Ui;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class TaskListTest {
    private Ui ui = new Ui();
    private Storage storage = new Storage("data/sirDuke.txt", ui);
    private TaskList taskList = new TaskList(storage, ui);

    private Parser parser = new Parser();

    public TaskListTest() throws IOException {
        storage.setTasks(taskList);
        storage.load();
    }

    @Test
    public void testFind() throws InvalidDateTimeFormat, InvalidCmd, EventEmptyException, DeadlineEmptyException {
        // D | 1 | return book | Dec 2 2019 18:00
        // D | 1 | book | Dec 12 1212 12:12

        taskList.add(new Deadline("return book", LocalDateTime.of(2019,12,2,18, 0)));
        taskList.add(new Deadline("book", LocalDateTime.of(1212,12,12,12,12)));
        String expected = "1. [D][ ] return book (by: Dec 2 2019 18:00) \n" +
                "2. [D][ ] book (by: Dec 12 1212 12:12) \n";
        Command actualCmd = parser.parseCommand("find book");
        actualCmd.setTasksAndUi(taskList, ui);
        actualCmd.execute();
        String actualText = "Check to see if ur sirduke.txt has the right preset tasks!";
        if (actualCmd instanceof FindCmd) {
            actualText = ((FindCmd) actualCmd).getResult().toString();
        } else {
            actualText = "actualCmd is not a FindCmd object";
        }
        assertEquals(expected, actualText);
        int index_of_recent_task = taskList.getTaskCount() - 1;
        taskList.delete(index_of_recent_task);
        taskList.delete(index_of_recent_task - 1);
    }
}
