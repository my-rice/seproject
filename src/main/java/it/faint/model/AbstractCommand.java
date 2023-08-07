package it.faint.model;

public abstract class AbstractCommand implements Command {
    protected boolean executed;

    public AbstractCommand(){
        executed = false;
    }

    public void execute(){
        if(executed){
            throw new RuntimeException("Command was already executed");
        }
        executed = true;
    }

    public void undo(){
        if(!executed){
            throw new RuntimeException("Cannot undo before executing the command!");
        }
        executed = false;
    }
}
