package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.ListProperty;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;

public class CommandInvokerTest {
    private CommandInvoker invoker;
    private List<Command> commands;
    private ListProperty<Command> commandList, undoneCommandList;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        invoker = new CommandInvoker();

        Field commandListField = CommandInvoker.class.getDeclaredField("commandHistory");
        commandListField.setAccessible(true);
        Field undoneCommandListField = CommandInvoker.class.getDeclaredField("undoneCommands");
        undoneCommandListField.setAccessible(true);
        commandList = (ListProperty<Command>) commandListField.get(invoker);
        undoneCommandList = (ListProperty<Command>) undoneCommandListField.get(invoker);
        Drawing drawing = new Drawing();
        Shape shape = new Rectangle();

        commands = List.of(new InsertCommand(drawing, shape),
        new ChangeFillColorCommand(shape, shape.fillProperty().get(), Color.ALICEBLUE),
        new ChangeBorderColorCommand(shape, shape.strokeProperty().get(), Color.BISQUE),
        new MoveCommand(shape, new Point2D(shape.boundsProperty().get().getMinX(), shape.boundsProperty().get().getMinY()), new Point2D(3.0, 4.0)),
        new ResizeCommand(shape, new Point2D(shape.boundsProperty().get().getMaxX()+3.0, shape.boundsProperty().get().getMaxY()+4.0), new Point2D(11.5,23.5)),
        new DeleteCommand(drawing,shape)
        );

        

    }

    @Test
    void testExecute() {
        int len = 0;
        assertEquals(len, commandList.size());
        for(Command command: commands){
            invoker.execute(command);
            len++;
            assertEquals(len, commandList.size());
        }
    }

    @Test
    void testHasUndoableCommandProperty() {
        for(Command command: commands){
            commandList.add(command);
            assertTrue(invoker.hasUndoableCommandProperty().get());
            commandList.remove(command);
            assertFalse(invoker.hasUndoableCommandProperty().get());
        }
    }

    @Test
    void testUndoLast() {
        for(Command command: commands){
            invoker.execute(command);
        }
        int len = commandList.getSize();
        for(Command command: commands){
            invoker.undoLast();
            assertEquals(len-1, commandList.getSize());
            len--;
        }
        assertThrows(RuntimeException.class, ()->{invoker.undoLast();});
    }

    @Test
    void testHasRedoableCommandProperty() {
        // Initially nothing can be redone
        assertThrows(RuntimeException.class, ()->{invoker.redoLast();});
        for(Command command: commands){
            invoker.execute(command);
        }
        // Nothing can be redone
        assertThrows(RuntimeException.class, ()->{invoker.redoLast();});
        
        for(Command command: commands){
            invoker.undoLast();
        }
        // Now we have commands that can be redone
        assertTrue(invoker.hasRedoableCommandProperty().get());
        invoker.execute(commands.get(0));
        // Check that after an execute, the undone commands list is reset
        assertFalse(invoker.hasRedoableCommandProperty().get());
        assertThrows(RuntimeException.class, ()->{invoker.redoLast();});
    }

    @Test
    void testRedoLast() {
        for(Command command: commands){
            invoker.execute(command);
        }
        int len = commandList.getSize();
        for(Command command: commands){
            invoker.undoLast();
            len--;
        }
        len = commandList.getSize();
        for(Command command: commands){
            invoker.redoLast();
            assertEquals(len+1, commandList.getSize());
            len++;
        }
    }
}
