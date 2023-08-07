package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class RectangleToolTest {
    private RectangleTool rectangleTool;
    private Field boundingRectangleField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        rectangleTool = new RectangleTool(new DrawEditor(new Drawing()));
        boundingRectangleField = ShapeInsertTool.class.getDeclaredField("boundingRectangle");
        boundingRectangleField.setAccessible(true);
    }

    @Test
    void testGetShape() throws IllegalArgumentException, IllegalAccessException {
        Shape result, expectedResult;
        List<javafx.geometry.Rectangle2D> list = new ArrayList<>();
        list.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 10.0, 5.0));
        list.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 5.0, 10.0));
        list.add(new javafx.geometry.Rectangle2D(0.0, -2.0, 100.0, 5.0));
        list.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 1.0, 0.0));
        list.add(new javafx.geometry.Rectangle2D(-1.0, -2.0, 0.0, 1.0));
        

        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(rectangleTool, r);
    
            result = rectangleTool.getShape();
            expectedResult = new Rectangle(r.getMinX(), r.getMinY(),r.getWidth(), r.getHeight());
            assertEquals(result, expectedResult);
        }
        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(rectangleTool, r);
    
            result = rectangleTool.getShape();
            expectedResult = new Rectangle(r.getMinY(), r.getMinX(),r.getWidth(), r.getHeight());
            assertNotEquals(result, expectedResult);
        }
        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(rectangleTool, r);
    
            result = rectangleTool.getShape();
            expectedResult = new Rectangle(r.getMinY(), r.getMinX(),r.getHeight(), r.getWidth());
            assertNotEquals(result, expectedResult);
        }
    }

    @Test
    void testUpdateShape() throws IllegalArgumentException, IllegalAccessException {
        Rectangle result= new Rectangle();
        Shape expectedResult;
        List<javafx.geometry.Rectangle2D> list = new ArrayList<>();
        list.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 10.0, 5.0));
        list.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 5.0, 10.0));
        list.add(new javafx.geometry.Rectangle2D(0.0, -2.0, 100.0, 5.0));
        list.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 0.0, 0.0));
        list.add(new javafx.geometry.Rectangle2D(-1.0, -2.0, 0.0, 0.0));

        
        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(rectangleTool, r);

            rectangleTool.updateShape(result);
            expectedResult = new Rectangle(r.getMinX(), r.getMinY(),r.getWidth(), r.getHeight());
            assertEquals(result, expectedResult);
        }
        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(rectangleTool, r);
    
            rectangleTool.updateShape(result);
            expectedResult = new Rectangle(r.getMinY(), r.getMinX(),r.getWidth(), r.getHeight());
            assertNotEquals(result, expectedResult);
        }
        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(rectangleTool, r);
    
            rectangleTool.updateShape(result);
            expectedResult = new Rectangle(r.getMinY(), r.getMinX(),r.getHeight(), r.getWidth());
            assertNotEquals(result, expectedResult);
        }

        
        
        assertThrows(java.lang.IllegalArgumentException.class, ()->{
            rectangleTool.updateShape(new Line());
        });
        assertThrows(java.lang.IllegalArgumentException.class, ()->{
            rectangleTool.updateShape(new Ellipse());
        });
    }
}
