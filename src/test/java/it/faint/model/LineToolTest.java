package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.geometry.Point2D;

public class LineToolTest {
    private LineTool lineTool;
    private Field firstPointField, secondPointField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        lineTool = new LineTool(new DrawEditor(new Drawing()));
        firstPointField = AbstractTool.class.getDeclaredField("firstPoint");
        firstPointField.setAccessible(true);
        secondPointField = ShapeInsertTool.class.getDeclaredField("secondPoint");
        secondPointField.setAccessible(true);

    }

    @Test
    void testgetShape() throws IllegalArgumentException, IllegalAccessException {
        Shape result, expectedResult;
        List<Point2D[]> list = new ArrayList<>();
        list.add(new Point2D[]{new Point2D(1.0, 1.0), new Point2D(100.0, 100.0)});
        list.add(new Point2D[]{new Point2D(-1.0, 1.0), new Point2D(-100.0, 100.0)});
        list.add(new Point2D[]{new Point2D(1.0, -1.0), new Point2D(100.0, -100.0)});
        list.add(new Point2D[]{new Point2D(-1.0,-1.0), new Point2D(-100.0, -100.0)});
        list.add(new Point2D[]{new Point2D(100.0, 100.0), new Point2D(1.0, 1.0)});
        list.add(new Point2D[]{new Point2D(-100.0, -100.0), new Point2D(-1.0, -1.0)});

        for(Point2D[] p: list){
            firstPointField.set(lineTool, p[0]);
            secondPointField.set(lineTool, p[1]);
    
            result = lineTool.getShape();
            expectedResult = new Line(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY());
            assertEquals(result, expectedResult);
        }
        for(Point2D[] p: list){
            firstPointField.set(lineTool, p[1]);
            secondPointField.set(lineTool, p[0]);
    
            result = lineTool.getShape();
            expectedResult = new Line(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY());
            assertNotEquals(result, expectedResult);
        }

    }

    @Test
    void testupdateShape() throws IllegalArgumentException, IllegalAccessException {

        Line result= new Line();
        Shape expectedResult;
        List<Point2D[]> list = new ArrayList<>();
        list.add(new Point2D[]{new Point2D(1.0, 1.0), new Point2D(100.0, 100.0)});
        list.add(new Point2D[]{new Point2D(-1.0, 1.0), new Point2D(-100.0, 100.0)});
        list.add(new Point2D[]{new Point2D(1.0, -1.0), new Point2D(100.0, -100.0)});
        list.add(new Point2D[]{new Point2D(-1.0,-1.0), new Point2D(-100.0, -100.0)});
        list.add(new Point2D[]{new Point2D(100.0, 100.0), new Point2D(1.0, 1.0)});
        list.add(new Point2D[]{new Point2D(-100.0, -100.0), new Point2D(-1.0, -1.0)});

        
        for(Point2D[] p: list){
            firstPointField.set(lineTool, p[0]);
            secondPointField.set(lineTool, p[1]);

            lineTool.updateShape(result);
            expectedResult = new Line(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY());
            assertEquals(result, expectedResult);
        }
        for(Point2D[] p: list){
            firstPointField.set(lineTool, p[1]);
            secondPointField.set(lineTool, p[0]);

            lineTool.updateShape(result);
            expectedResult = new Line(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY());
            assertNotEquals(result, expectedResult);
        }
        
        
        assertThrows(java.lang.IllegalArgumentException.class, ()->{
            lineTool.updateShape(new Rectangle());
        });
        assertThrows(java.lang.IllegalArgumentException.class, ()->{
            lineTool.updateShape(new Ellipse());
        });


    }
}
