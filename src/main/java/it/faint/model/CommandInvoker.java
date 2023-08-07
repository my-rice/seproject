package it.faint.model;

import java.util.LinkedList;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

public class CommandInvoker {
    private ListProperty<Command> commandHistory; //Stack
    private ListProperty<Command> undoneCommands; //Stack
    private BooleanProperty hasUndoableCommand;
    private BooleanProperty hasRedoableCommand;

    public CommandInvoker(){
        this.commandHistory = new SimpleListProperty<Command>(FXCollections.observableList(new LinkedList<>()));
        this.undoneCommands = new SimpleListProperty<Command>(FXCollections.observableList(new LinkedList<>()));
        this.hasUndoableCommand = new SimpleBooleanProperty(false);
        this.hasRedoableCommand = new SimpleBooleanProperty(false);
        this.hasUndoableCommand.bind(commandHistory.sizeProperty().greaterThan(0));
        this.hasRedoableCommand.bind(undoneCommands.sizeProperty().greaterThan(0));
    }

    public void execute(Command command){
        this.commandHistory.add(command);
        command.execute();
        undoneCommands.clear();
    }

    public void undoLast(){
        if(commandHistory.size() == 0){
            throw new RuntimeException("Nothing to be undone");
        }
        int last = commandHistory.size() - 1;
        Command c = this.commandHistory.remove(last);
        c.undo();
        undoneCommands.add(c);
    }

    public void redoLast(){
        if(undoneCommands.size() == 0){
            throw new RuntimeException("Nothing to be redone");
        }
        int last = undoneCommands.size() - 1;
        Command c = this.undoneCommands.remove(last);
        this.commandHistory.add(c);
        c.execute();
    }

    public ReadOnlyBooleanProperty hasUndoableCommandProperty(){
        return BooleanProperty.readOnlyBooleanProperty(hasUndoableCommand);
    }

    public ReadOnlyBooleanProperty hasRedoableCommandProperty() {
        return BooleanProperty.readOnlyBooleanProperty(hasRedoableCommand);
    }
}
