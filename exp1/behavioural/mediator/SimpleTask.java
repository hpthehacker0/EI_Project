public class SimpleTask extends Task {
    public SimpleTask(String name, WorkflowMediator mediator) {
        super(name, mediator);
    }

    @Override
    public void execute() {
        System.out.println("Task '" + name + "' is executing...");
        // Simulate work
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Task '" + name + "' completed.");
        notifyCompletion(); // Notify mediator through parent
    }
}
