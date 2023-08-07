package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class DeleteCommandTest {
    private List<DeleteCommand> deleteCommandList;
    private Field shapeField,recieverField, indexField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException {
        deleteCommandList = new ArrayList<>();
        Rectangle r = new Rectangle();
        Ellipse e = new Ellipse();
        Line l = new Line();
        Drawing d = new Drawing();
        d.insertAllShapes(List.of(r,e,l));
        deleteCommandList.add(new DeleteCommand(d, r));
        deleteCommandList.add(new DeleteCommand(d, l));
        deleteCommandList.add(new DeleteCommand(d, e));
        shapeField = DeleteCommand.class.getDeclaredField("shape");
        shapeField.setAccessible(true);
        recieverField = DeleteCommand.class.getDeclaredField("reciever");
        recieverField.setAccessible(true);
        indexField = DeleteCommand.class.getDeclaredField("index");
        indexField.setAccessible(true);
    }

    @Test
    void testExecute() throws IllegalArgumentException, IllegalAccessException {
        for(DeleteCommand command: deleteCommandList){
            Shape insertedShape = (Shape) shapeField.get(command);
            Drawing reciever = (Drawing) recieverField.get(command);
            command.execute();
            assertEquals(-1, reciever.removeShape(insertedShape)); //Check that the shape was deleted
        }
        for(DeleteCommand command: deleteCommandList){
            assertThrows(RuntimeException.class, ()-> {command.execute();});
        }
    }

    @Test
    void testUndo() throws IllegalArgumentException, IllegalAccessException{
        for(DeleteCommand command: deleteCommandList){
            assertThrows(RuntimeException.class, ()-> {command.undo();});
        }


        for(DeleteCommand command: deleteCommandList){
            command.execute();
        }
        //Delete commands must be undone in reverse order!
        ListIterator<DeleteCommand> i = deleteCommandList.listIterator(deleteCommandList.size());
        while(i.hasPrevious()){
            Command command = i.previous();
            Shape deletedShape = (Shape) shapeField.get(command);
            Drawing reciever = (Drawing) recieverField.get(command);
            command.undo();
            assertNotEquals(-1, reciever.getShapeIndex(deletedShape)); //Assert shape was inserted back
        }
        
    }
}
