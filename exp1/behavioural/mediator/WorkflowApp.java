import java.util.Scanner;

public class WorkflowApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WorkflowMediator mediator = new WorkflowMediator();

        System.out.println("=================================");
        System.out.println("Workflow Management System");
        System.out.println("(Mediator Design Pattern)");
        System.out.println("=================================");
        
        boolean running = true;

        while (running) {
            System.out.println("\n--- Menu ---");
            System.out.println("1. Add Task");
            System.out.println("2. Execute Workflow");
            System.out.println("3. Clear All Tasks");
            System.out.println("4. Show Task Count");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.print("Enter task name: ");
                    String name = scanner.nextLine();
                    Task task = new SimpleTask(name, mediator);
                    mediator.addTask(task);
                    break;
                case 2:
                    mediator.executeWorkflow();
                    break;
                case 3:
                    mediator.clearTasks();
                    break;
                case 4:
                    System.out.println("Current tasks in workflow: " + mediator.getTaskCount());
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }

        scanner.close();
    }
}