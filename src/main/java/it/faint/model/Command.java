package it.faint.model;

public interface Command {
    public abstract void execute();
    public abstract void undo();
}
