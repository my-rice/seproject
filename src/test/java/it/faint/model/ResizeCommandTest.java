package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.geometry.Point2D;

public class ResizeCommandTest {
    private List<ResizeCommand> resizeCommandList;
    private List<Point2D> prevPoints;
    private List<Point2D> newPoints;
    private Field shapeField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException {
        resizeCommandList = new ArrayList<>();
        prevPoints = List.of(new Point2D(19.0,-2.3),
        new Point2D(432.4324,-2.3),
        new Point2D(76.5,4.88),
        new Point2D(456.,-92.55),
        new Point2D(-645,-2.553),
        new Point2D(166.0,24.38),
        new Point2D(149.0,-25.345)
        );
        Point2D offset = new Point2D(-4.5,11.8);
        newPoints = new ArrayList<>();
        newPoints = prevPoints.stream().map((point)->point.add(offset)).collect(Collectors.toList());

        int i = 0;
        while(i<prevPoints.size()){
            Point2D prevPoint = prevPoints.get(i);
            Point2D newPoint = newPoints.get(i);
            resizeCommandList.add(new ResizeCommand( new Rectangle(prevPoint.getX()-20.45,prevPoint.getY()-12.3,20.45,12.3), prevPoint, newPoint));
            resizeCommandList.add(new ResizeCommand( new Line(prevPoint.getX()-999.9,prevPoint.getY()-999.9,prevPoint.getX(),prevPoint.getY()), prevPoint, newPoint));
            resizeCommandList.add(new ResizeCommand( new Ellipse(prevPoint.getX()-5.5,prevPoint.getY()-6.5,5.5,6.5), prevPoint, newPoint));
            i++;
        }
        shapeField = ResizeCommand.class.getDeclaredField("shape");
        shapeField.setAccessible(true);
    }

    private static boolean almostEqual(Point2D a, Point2D b, double eps){
        return a.distance(b)<eps;
    }

    @Test
    void testExecute() throws IllegalArgumentException, IllegalAccessException {
        int i=0;
        for(ResizeCommand command: resizeCommandList){
            Shape movedShape = (Shape) shapeField.get(command);
            command.execute();
            assertTrue(almostEqual(newPoints.get(i/3),new Point2D(movedShape.boundsProperty().get().getMaxX(), movedShape.boundsProperty().get().getMaxY()),0.0000001));
            i++;
        }
        for(ResizeCommand command: resizeCommandList){
            assertThrows(RuntimeException.class, ()-> {command.execute();});
        }
    }

    @Test
    void testUndo() throws IllegalArgumentException, IllegalAccessException {
        for(ResizeCommand command: resizeCommandList){
            assertThrows(RuntimeException.class, ()-> {command.undo();});
        }
        for(ResizeCommand command: resizeCommandList){
            command.execute();
        }
        int i=0;
        for(ResizeCommand command: resizeCommandList){
            Shape movedShape = (Shape) shapeField.get(command);
            assertTrue(almostEqual(newPoints.get(i/3),new Point2D(movedShape.boundsProperty().get().getMaxX(), movedShape.boundsProperty().get().getMaxY()),0.0000001));
            command.undo();
            assertTrue(almostEqual(prevPoints.get(i/3),new Point2D(movedShape.boundsProperty().get().getMaxX(), movedShape.boundsProperty().get().getMaxY()), 0.0000001));
            i++;
        }
    }
}
