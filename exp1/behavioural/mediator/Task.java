public abstract class Task {
    protected WorkflowMediator mediator;
    protected String name;

    public Task(String name, WorkflowMediator mediator) {
        this.name = name;
        this.mediator = mediator;
    }

    public abstract void execute();
    
    public String getName() {
        return name;
    }
    
    // Notify mediator when task completes
    protected void notifyCompletion() {
        mediator.taskCompleted(this);
    }
}