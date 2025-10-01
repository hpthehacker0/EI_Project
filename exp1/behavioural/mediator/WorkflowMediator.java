import java.util.ArrayList;
import java.util.List;

public class WorkflowMediator {
    private List<Task> tasks = new ArrayList<>();
    private int completedCount = 0;

    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("Mediator: Task '" + task.getName() + "' registered.");
    }

    public void executeWorkflow() {
        if (tasks.isEmpty()) {
            System.out.println("\nMediator: No tasks to execute.");
            return;
        }
        
        completedCount = 0;
        System.out.println("\n--- Mediator: Starting Workflow ---");
        System.out.println("Total tasks: " + tasks.size());
        
        for (Task task : tasks) {
            try {
                System.out.println("\nMediator: Dispatching task '" + task.getName() + "'");
                task.execute();
            } catch (Exception e) {
                System.err.println("Mediator: Error in task '" + task.getName() + "': " + e.getMessage());
            }
        }
        
        System.out.println("\n--- Mediator: Workflow Complete ---");
        System.out.println("Tasks completed: " + completedCount + "/" + tasks.size() + "\n");
    }

    // Mediator receives notification from tasks
    public void taskCompleted(Task task) {
        completedCount++;
        System.out.println("Mediator: Received completion notification from '" + task.getName() + "'");
    }
    
    public void clearTasks() {
        tasks.clear();
        completedCount = 0;
        System.out.println("Mediator: All tasks cleared.");
    }
    
    public int getTaskCount() {
        return tasks.size();
    }
}