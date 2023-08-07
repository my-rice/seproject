package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.geometry.Point2D;

public class MoveCommandTest {
    private List<MoveCommand> moveCommandList;
    private List<Point2D> prevPoints;
    private List<Point2D> newPoints;
    private Field shapeField, oldTopLeftPointField, newTopLeftPointField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException {
        moveCommandList = new ArrayList<>();
        prevPoints = List.of(new Point2D(1.0,-2.3),
        new Point2D(432.4324,-2.3),
        new Point2D(76.5,4.88),
        new Point2D(456.,-92.55),
        new Point2D(-645,-2.553),
        new Point2D(166.0,24.38),
        new Point2D(149.0,-25.345)
        );
        newPoints = List.of(new Point2D(1.0,-2.3),
        new Point2D(-234.0,-8.3),
        new Point2D(9.330,-25.3),
        new Point2D(651.05,-3.8),
        new Point2D(7.777,42.3),
        new Point2D(-4.0,-2.3),
        new Point2D(1.0,-8.23)
        );

        int i = 0;
        while(i<prevPoints.size()){
            Point2D prevPoint = prevPoints.get(i);
            Point2D newPoint = newPoints.get(i);
            moveCommandList.add(new MoveCommand( new Rectangle(prevPoint.getX(),prevPoint.getY(),20.45,12.3), prevPoint, newPoint));
            moveCommandList.add(new MoveCommand( new Line(prevPoint.getX(),prevPoint.getY(),999.9,999.9), prevPoint, newPoint));
            moveCommandList.add(new MoveCommand( new Ellipse(prevPoint.getX()+5.5,prevPoint.getY()+6.5,5.5,6.5), prevPoint, newPoint));
            i++;
        }
        shapeField = MoveCommand.class.getDeclaredField("shape");
        shapeField.setAccessible(true);
        oldTopLeftPointField = MoveCommand.class.getDeclaredField("oldTopLeftPoint");
        oldTopLeftPointField.setAccessible(true);
        newTopLeftPointField = MoveCommand.class.getDeclaredField("newTopLeftPoint");
        newTopLeftPointField.setAccessible(true);
    }

    private static boolean almostEqual(Point2D a, Point2D b, double eps){
        return a.distance(b)<eps;
    }

    @Test
    void testExecute() throws IllegalArgumentException, IllegalAccessException {
        int i=0;
        for(MoveCommand command: moveCommandList){
            Shape movedShape = (Shape) shapeField.get(command);
            command.execute();
            assertTrue(almostEqual(newPoints.get(i/3),new Point2D(movedShape.boundsProperty().get().getMinX(), movedShape.boundsProperty().get().getMinY()),0.0000001));
            i++;
        }
        for(MoveCommand command: moveCommandList){
            assertThrows(RuntimeException.class, ()-> {command.execute();});
        }
    }

    @Test
    void testUndo() throws IllegalArgumentException, IllegalAccessException {
        for(MoveCommand command: moveCommandList){
            assertThrows(RuntimeException.class, ()-> {command.undo();});
        }
        for(MoveCommand command: moveCommandList){
            command.execute();
        }
        int i=0;
        for(MoveCommand command: moveCommandList){
            Shape movedShape = (Shape) shapeField.get(command);
            assertTrue(almostEqual(newPoints.get(i/3),new Point2D(movedShape.boundsProperty().get().getMinX(), movedShape.boundsProperty().get().getMinY()),0.0000001));
            command.undo();
            assertTrue(almostEqual(prevPoints.get(i/3),new Point2D(movedShape.boundsProperty().get().getMinX(), movedShape.boundsProperty().get().getMinY()), 0.0000001));
            i++;
        }
    }
}
