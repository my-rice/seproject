package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EllipseToolTest {
    private EllipseTool ellipseTool;
    private Field boundingRectangleField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        ellipseTool = new EllipseTool(new DrawEditor(new Drawing()));
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
            boundingRectangleField.set(ellipseTool, r);
    
            result = ellipseTool.getShape();
            expectedResult = new Ellipse( ((r.getMaxX() - r.getMinX() )/2)+ r.getMinX() ,((r.getMaxY() - r.getMinY() )/2)+ r.getMinY() , (r.getMaxX() - r.getMinX() )/2 , (r.getMaxY() - r.getMinY() )/2 );
            assertEquals(result, expectedResult);
        }
        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(ellipseTool, r);
    
            result = ellipseTool.getShape();
            expectedResult = new Ellipse( ((r.getMaxX() - r.getMinX() )/2)+ r.getMinX() ,((r.getMaxY() - r.getMinY() )/2)+ r.getMinY() , (r.getMaxY() - r.getMinY() )/2, (r.getMaxX() - r.getMinX() )/2  );
            assertNotEquals(result, expectedResult);
        }
        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(ellipseTool, r);
    
            result = ellipseTool.getShape();
            expectedResult = new Ellipse( ((r.getMaxY() - r.getMinY() )/2)+ r.getMinY(), ((r.getMaxX() - r.getMinX() )/2)+ r.getMinX() , (r.getMaxX() - r.getMinX() )/2 , (r.getMaxY() - r.getMinY() )/2 );
            assertNotEquals(result, expectedResult);
        }
    }

    @Test
    void testUpdateShape() throws IllegalArgumentException, IllegalAccessException {
        Ellipse result= new Ellipse();
        Shape expectedResult;
        List<javafx.geometry.Rectangle2D> list = new ArrayList<>();
        list.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 10.0, 5.0));
        list.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 5.0, 10.0));
        list.add(new javafx.geometry.Rectangle2D(0.0, -2.0, 100.0, 5.0));
        list.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 1.0, 0.0));
        list.add(new javafx.geometry.Rectangle2D(-1.0, -2.0, 0.0, 1.0));

        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(ellipseTool, r);
    
            ellipseTool.updateShape(result);
            expectedResult = new Ellipse( ((r.getMaxX() - r.getMinX() )/2)+ r.getMinX() ,((r.getMaxY() - r.getMinY() )/2)+ r.getMinY() , (r.getMaxX() - r.getMinX() )/2 , (r.getMaxY() - r.getMinY() )/2 );
            assertEquals(result, expectedResult);
        }
        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(ellipseTool, r);
    
            ellipseTool.updateShape(result);
            expectedResult = new Ellipse( ((r.getMaxX() - r.getMinX() )/2)+ r.getMinX() ,((r.getMaxY() - r.getMinY() )/2)+ r.getMinY() , (r.getMaxY() - r.getMinY() )/2, (r.getMaxX() - r.getMinX() )/2  );
            assertNotEquals(result, expectedResult);
        }
        for(javafx.geometry.Rectangle2D r: list){
            boundingRectangleField.set(ellipseTool, r);
    
            ellipseTool.updateShape(result);
            expectedResult = new Ellipse( ((r.getMaxY() - r.getMinY() )/2)+ r.getMinY(), ((r.getMaxX() - r.getMinX() )/2)+ r.getMinX() , (r.getMaxX() - r.getMinX() )/2 , (r.getMaxY() - r.getMinY() )/2 );
            assertNotEquals(result, expectedResult);
        }
        
        
        assertThrows(java.lang.IllegalArgumentException.class, ()->{
            ellipseTool.updateShape(new Line());
        });
        assertThrows(java.lang.IllegalArgumentException.class, ()->{
            ellipseTool.updateShape(new Rectangle());
        });
    }
    
}