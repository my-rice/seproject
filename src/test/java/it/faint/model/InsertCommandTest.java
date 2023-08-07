package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InsertCommandTest {
    private List<InsertCommand> insertCommandList;
    private Field shapeField,recieverField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException {
        insertCommandList = new ArrayList<>();
        insertCommandList.add(new InsertCommand(new Drawing(), new Rectangle()));
        insertCommandList.add(new InsertCommand(new Drawing(), new Line()));
        insertCommandList.add(new InsertCommand(new Drawing(), new Ellipse()));
        shapeField = InsertCommand.class.getDeclaredField("shape");
        shapeField.setAccessible(true);
        recieverField = InsertCommand.class.getDeclaredField("reciever");
        recieverField.setAccessible(true);
    }

    @Test
    void testExecute() throws IllegalArgumentException, IllegalAccessException {
        for(InsertCommand command: insertCommandList){
            Shape insertedShape = (Shape) shapeField.get(command);
            Drawing reciever = (Drawing) recieverField.get(command);
            command.execute();
            assertNotEquals(-1, reciever.removeShape(insertedShape)); //Check that the shape was inserted
        }
        for(InsertCommand command: insertCommandList){
            assertThrows(RuntimeException.class, ()-> {command.execute();});
        }
    }

    @Test
    void testUndo() throws IllegalArgumentException, IllegalAccessException{
        for(InsertCommand command: insertCommandList){
            assertThrows(RuntimeException.class, ()-> {command.undo();});
        }


        for(InsertCommand command: insertCommandList){
            command.execute();
        }

        for(InsertCommand command: insertCommandList){
            Shape insertedShape = (Shape) shapeField.get(command);
            Drawing reciever = (Drawing) recieverField.get(command);
            command.undo();
            assertEquals(-1, reciever.removeShape(insertedShape));
        }
        
    }
}