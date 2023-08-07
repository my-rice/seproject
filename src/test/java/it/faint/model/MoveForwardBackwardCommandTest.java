package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MoveForwardBackwardCommandTest {
    private MoveForwardBackwardCommand moveForwardBackwardCommand;
    private Field recieverField, shapeField,nLevelField,oldIndexField, newIndexField;


    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException {

        recieverField = MoveForwardBackwardCommand.class.getDeclaredField("reciever");
        recieverField.setAccessible(true);
        shapeField = MoveForwardBackwardCommand.class.getDeclaredField("shape");
        shapeField.setAccessible(true); 
        nLevelField = MoveForwardBackwardCommand.class.getDeclaredField("nLevel");
        nLevelField.setAccessible(true);
        oldIndexField = MoveForwardBackwardCommand.class.getDeclaredField("oldIndex");
        oldIndexField.setAccessible(true);
        newIndexField = MoveForwardBackwardCommand.class.getDeclaredField("newIndex");
        newIndexField.setAccessible(true);
        
        
    }
    private List<Shape> startingConfigurationHelper(Drawing drawing){
        drawing.clearShapes();
        Shape shapeLevel1 = new Line(2.2, 3.5, 400.9, 485.1);
        Shape shapeLevel2 = new Line(2258.2, 1523.15, 4.9, 8.1);;
        Shape shapeLevel3 = new Rectangle(0, 0, 150, 150);
        Shape shapeLevel4 = new Rectangle(50, 50, 70, 70);;
        Shape shapeLevel5 = new Ellipse(25.7, 29.35, 5.47, 3.9);
        Shape shapeLevel6 = new Ellipse(3.7, 2.9, 0.27, 0.19);;

        drawing.addShape(shapeLevel1);
        drawing.addShape(shapeLevel2);
        drawing.addShape(shapeLevel3);
        drawing.addShape(shapeLevel4);
        drawing.addShape(shapeLevel5);
        drawing.addShape(shapeLevel6);

        List<Shape> list = new ArrayList<>();
        list.addAll(drawing.getAllShapes());
        return list;
    }

    //The same command class manages four different behaviours  

    @Test
    void testExecuteForward() throws IllegalArgumentException, IllegalAccessException {
        Drawing drawing = new Drawing();
        //DrawEditor drawEditor = new DrawEditor(drawing);
        List<Shape> resultShapes,expectedShapes = new LinkedList<>();;
        int nLevel,expectedOldIndex, expectedNewIndex, resultNewIndex,resultOldIndex;
        
        Shape shapeLevel1 = new Line(2.2, 3.5, 400.9, 485.1);
        Shape shapeLevel2 = new Line(2258.2, 1523.15, 4.9, 8.1);;
        Shape shapeLevel3 = new Rectangle(0, 0, 150, 150);
        Shape shapeLevel4 = new Rectangle(50, 50, 70, 70);;
        Shape shapeLevel5 = new Ellipse(25.7, 29.35, 5.47, 3.9);
        Shape shapeLevel6 = new Ellipse(3.7, 2.9, 0.27, 0.19);;

        drawing.addShape(shapeLevel1);
        drawing.addShape(shapeLevel2);
        drawing.addShape(shapeLevel3);
        drawing.addShape(shapeLevel4);
        drawing.addShape(shapeLevel5);
        drawing.addShape(shapeLevel6);

        //Move forward shape1 of 1 level
        nLevel = 1;
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel1,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel1);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel6);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel1);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Move forward the shape1 of another level
        nLevel = +1;
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel1,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel1);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel6);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel1);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Move forward the shape5 of another level
        nLevel = +1;
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel5,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel5);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel6);
        expectedShapes.add(shapeLevel5);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel5);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Edge cases tests.
        //Tring to move a shape5 forward but it is already in last level
        nLevel = +1;
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel5,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel5);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel6);
        expectedShapes.add(shapeLevel5);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel5);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        assertEquals(resultNewIndex, resultNewIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }
    }

    @Test
    void testExecuteBackward() throws IllegalArgumentException, IllegalAccessException {
        Drawing drawing = new Drawing();
        List<Shape> resultShapes,expectedShapes = new LinkedList<>();;
        int nLevel,expectedOldIndex, expectedNewIndex, resultNewIndex,resultOldIndex;
        
        Shape shapeLevel1 = new Line(2.2, 3.5, 400.9, 485.1);
        Shape shapeLevel2 = new Line(2258.2, 1523.15, 4.9, 8.1);;
        Shape shapeLevel3 = new Rectangle(0, 0, 150, 150);
        Shape shapeLevel4 = new Rectangle(50, 50, 70, 70);;
        Shape shapeLevel5 = new Ellipse(25.7, 29.35, 5.47, 3.9);
        Shape shapeLevel6 = new Ellipse(3.7, 2.9, 0.27, 0.19);;

        drawing.addShape(shapeLevel1);
        drawing.addShape(shapeLevel2);
        drawing.addShape(shapeLevel3);
        drawing.addShape(shapeLevel4);
        drawing.addShape(shapeLevel5);
        drawing.addShape(shapeLevel6);

        //Move backward shape4 of 1 level
        nLevel = -1;
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel4,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel4);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel6);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel4);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Move backward the shape4 of another level
        nLevel = -1;
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel4,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel4);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel6);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel4);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Move backward the shape5 of another level
        nLevel = -1;
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel5,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel5);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel6);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel5);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Edge cases tests.
        //Tring to move a shape1 backward but it is already in first level
        nLevel = -1;
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel1,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel1);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel6);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel1);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        assertEquals(resultNewIndex, resultNewIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }
        
    }
    
    @Test
    void testExecuteToFront() throws IllegalArgumentException, IllegalAccessException {
    
        Drawing drawing = new Drawing();
        List<Shape> resultShapes,expectedShapes = new LinkedList<>();;
        int size,nLevel,expectedOldIndex, expectedNewIndex, resultNewIndex,resultOldIndex;
        
        Shape shapeLevel1 = new Line(2.2, 3.5, 400.9, 485.1);
        Shape shapeLevel2 = new Line(2258.2, 1523.15, 4.9, 8.1);;
        Shape shapeLevel3 = new Rectangle(0, 0, 150, 150);
        Shape shapeLevel4 = new Rectangle(50, 50, 70, 70);;
        Shape shapeLevel5 = new Ellipse(25.7, 29.35, 5.47, 3.9);
        Shape shapeLevel6 = new Ellipse(3.7, 2.9, 0.27, 0.19);;

        drawing.addShape(shapeLevel1);
        drawing.addShape(shapeLevel2);
        drawing.addShape(shapeLevel3);
        drawing.addShape(shapeLevel4);
        drawing.addShape(shapeLevel5);
        drawing.addShape(shapeLevel6);

        //Move toFront shape3 
        size = drawing.getAllShapes().size();
        nLevel = size - (drawing.getAllShapes().indexOf(shapeLevel3) + 1);
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel3,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel3);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel6);
        expectedShapes.add(shapeLevel3);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel3);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Move toFront the shape4
        size = drawing.getAllShapes().size();
        nLevel = size - (drawing.getAllShapes().indexOf(shapeLevel4) + 1);
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel4,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel4);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel6);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel4);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel4);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Edge cases tests.
        //Tring to move toFront shape4 but it is already in front.
        size = drawing.getAllShapes().size();
        nLevel = size - (drawing.getAllShapes().indexOf(shapeLevel4) + 1);
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel4,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel4);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel6);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel4);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel4);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        assertEquals(resultNewIndex, resultNewIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }
    }
    
    @Test
    void testExecuteToBack() throws IllegalArgumentException, IllegalAccessException {
        Drawing drawing = new Drawing();
        List<Shape> resultShapes,expectedShapes = new LinkedList<>();;
        int nLevel,expectedOldIndex, expectedNewIndex, resultNewIndex,resultOldIndex;
        
        Shape shapeLevel1 = new Line(2.2, 3.5, 400.9, 485.1);
        Shape shapeLevel2 = new Line(2258.2, 1523.15, 4.9, 8.1);;
        Shape shapeLevel3 = new Rectangle(0, 0, 150, 150);
        Shape shapeLevel4 = new Rectangle(50, 50, 70, 70);;
        Shape shapeLevel5 = new Ellipse(25.7, 29.35, 5.47, 3.9);
        Shape shapeLevel6 = new Ellipse(3.7, 2.9, 0.27, 0.19);;

        drawing.addShape(shapeLevel1);
        drawing.addShape(shapeLevel2);
        drawing.addShape(shapeLevel3);
        drawing.addShape(shapeLevel4);
        drawing.addShape(shapeLevel5);
        drawing.addShape(shapeLevel6);

        //Move toBack shape3 
        nLevel = -drawing.getAllShapes().indexOf(shapeLevel3);
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel3,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel3);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel6);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel3);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Move toBack the shape4
        nLevel = -drawing.getAllShapes().indexOf(shapeLevel4);
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel4,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel4);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel6);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel4);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Edge cases tests.
        //Tring to move toBack shape4 but it is already in back.
        nLevel = -drawing.getAllShapes().indexOf(shapeLevel3);
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeLevel4,nLevel);
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeLevel4);
        moveForwardBackwardCommand.execute();
        resultShapes = drawing.getAllShapes();

        expectedShapes.clear();
        expectedShapes.add(shapeLevel4);
        expectedShapes.add(shapeLevel3);
        expectedShapes.add(shapeLevel1);
        expectedShapes.add(shapeLevel2);
        expectedShapes.add(shapeLevel5);
        expectedShapes.add(shapeLevel6);
        expectedNewIndex = expectedShapes.indexOf(shapeLevel4);
        resultNewIndex = (int) newIndexField.get(moveForwardBackwardCommand);
        resultOldIndex = (int) oldIndexField.get(moveForwardBackwardCommand);

        assertEquals(expectedNewIndex,resultNewIndex);
        assertEquals(expectedOldIndex,resultOldIndex);
        assertEquals(resultNewIndex, resultNewIndex);
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }
        
            
    }
    @Test
    void testUndo() throws IllegalArgumentException, IllegalAccessException {
        Drawing drawing = new Drawing();
        List<Shape> resultShapes,expectedShapes = new LinkedList<>();;
        int size,nLevel,expectedOldIndex, expectedNewIndex, resultNewIndex,resultOldIndex;
        Shape shapeToMove;
        expectedShapes = List.of(new Line(2.2, 3.5, 400.9, 485.1),
        new Line(2258.2, 1523.15, 4.9, 8.1),
        new Rectangle(0, 0, 150, 150),
        new Rectangle(50, 50, 70, 70),
        new Ellipse(25.7, 29.35, 5.47, 3.9),
        new Ellipse(3.7, 2.9, 0.27, 0.19)
        );
        for(Shape s: expectedShapes){
            drawing.addShape(s);
        }

        //Move toFront shape1 
        shapeToMove = expectedShapes.get(0);
        size = drawing.getAllShapes().size();
        nLevel = size - (drawing.getAllShapes().indexOf(shapeToMove) + 1);
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeToMove,nLevel);
        //Check the exception before the execution of the command
        assertThrows(RuntimeException.class, ()-> {moveForwardBackwardCommand.undo();});
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeToMove);
        //Executing the command
        moveForwardBackwardCommand.execute();
        //Undoing the command
        moveForwardBackwardCommand.undo();
        //Checking the results
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Move toBack shape3 
        shapeToMove = expectedShapes.get(2);
        size = drawing.getAllShapes().size();
        nLevel = -drawing.getAllShapes().indexOf(shapeToMove);
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeToMove,nLevel);
        //Check the exception before the execution of the command
        assertThrows(RuntimeException.class, ()-> {moveForwardBackwardCommand.undo();});
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeToMove);
        //Executing the command
        moveForwardBackwardCommand.execute();
        //Undoing the command
        moveForwardBackwardCommand.undo();
        //Checking the results
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Move forward shape2
        shapeToMove = expectedShapes.get(1);
        size = drawing.getAllShapes().size();
        nLevel = +1;
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeToMove,nLevel);
        //Check the exception before the execution of the command
        assertThrows(RuntimeException.class, ()-> {moveForwardBackwardCommand.undo();});
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeToMove);
        //Executing the command
        moveForwardBackwardCommand.execute();
        //Undoing the command
        moveForwardBackwardCommand.undo();
        //Checking the results
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Move backward shape1
        shapeToMove = expectedShapes.get(1);
        size = drawing.getAllShapes().size();
        nLevel = -1;
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeToMove,nLevel);
        //Check the exception before the execution of the command
        assertThrows(RuntimeException.class, ()-> {moveForwardBackwardCommand.undo();});
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeToMove);
        //Executing the command
        moveForwardBackwardCommand.execute();
        //Undoing the command
        moveForwardBackwardCommand.undo();
        //Checking the results
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Edge cases: Move toFront last shape shape3 
        shapeToMove = expectedShapes.get(2);
        size = drawing.getAllShapes().size();
        nLevel = size - (drawing.getAllShapes().indexOf(shapeToMove) + 1);
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeToMove,nLevel);
        //Check the exception before the execution of the command
        assertThrows(RuntimeException.class, ()-> {moveForwardBackwardCommand.undo();});
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeToMove);
        //Executing the command
        moveForwardBackwardCommand.execute();
        //Undoing the command
        moveForwardBackwardCommand.undo();
        //Checking the results
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }

        //Edge Cases: Move toBack shape1 
        shapeToMove = expectedShapes.get(0);
        size = drawing.getAllShapes().size();
        nLevel = -drawing.getAllShapes().indexOf(shapeToMove);
        moveForwardBackwardCommand = new MoveForwardBackwardCommand(drawing,shapeToMove,nLevel);
        //Check the exception before the execution of the command
        assertThrows(RuntimeException.class, ()-> {moveForwardBackwardCommand.undo();});
        expectedOldIndex = drawing.getAllShapes().indexOf(shapeToMove);
        //Executing the command
        moveForwardBackwardCommand.execute();
        //Undoing the command
        moveForwardBackwardCommand.undo();
        //Checking the results
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i),resultShapes.get(i));
        }
    }
    
}
