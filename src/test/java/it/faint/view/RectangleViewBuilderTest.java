package it.faint.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.faint.model.Rectangle;
import it.faint.view.AbstractShapeViewBuilder;
import it.faint.view.RectangleViewBuilder;

public class RectangleViewBuilderTest {
    private RectangleViewBuilder rectangleViewBuilder;
    private Rectangle rectangle;
    private Field modelField;
    private Field resultField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        rectangleViewBuilder = new RectangleViewBuilder(new Rectangle());
        modelField = RectangleViewBuilder.class.getDeclaredField("model");
        modelField.setAccessible(true);
        resultField = AbstractShapeViewBuilder.class.getDeclaredField("result");
        resultField.setAccessible(true);

        rectangle =new Rectangle(0.0,0.0,0.0,0.0);
        
    }

    @Test
    void testBindViewToSpecificShapeProperties() throws IllegalArgumentException, IllegalAccessException {
        javafx.scene.shape.Rectangle view= new javafx.scene.shape.Rectangle();

        rectangle.xProperty().set(1.0);
        rectangle.yProperty().set(1.0);
        rectangle.widthProperty().set(100.0);
        rectangle.heightProperty().set(100.0);

        resultField.set(rectangleViewBuilder, view);
        modelField.set(rectangleViewBuilder, rectangle);

        rectangleViewBuilder.bindViewToSpecificShapeProperties();

        assertEquals(view.getX(), rectangle.xProperty().getValue());
        assertEquals(view.getY(), rectangle.yProperty().getValue());
        assertEquals(view.getWidth(), rectangle.widthProperty().getValue());
        assertEquals(view.getHeight(), rectangle.heightProperty().getValue());

        rectangle.xProperty().set(10.0);
        rectangle.yProperty().set(10.0);
        rectangle.widthProperty().set(1000.0);
        rectangle.heightProperty().set(1000.0);

        view = (javafx.scene.shape.Rectangle) resultField.get(rectangleViewBuilder);

        assertEquals(view.getX(), rectangle.xProperty().getValue());
        assertEquals(view.getY(), rectangle.yProperty().getValue());
        assertEquals(view.getWidth(), rectangle.widthProperty().getValue());
        assertEquals(view.getHeight(), rectangle.heightProperty().getValue());
    }

    @Test
    void testGetResult() throws IllegalArgumentException, IllegalAccessException {
        javafx.scene.shape.Rectangle view= new javafx.scene.shape.Rectangle();

        rectangle.xProperty().set(1.0);
        rectangle.yProperty().set(1.0);
        rectangle.widthProperty().set(100.0);
        rectangle.heightProperty().set(100.0);

        resultField.set(rectangleViewBuilder, view);
        modelField.set(rectangleViewBuilder, rectangle);

        rectangleViewBuilder.bindViewToSpecificShapeProperties();

        rectangle.xProperty().set(10.0);
        rectangle.yProperty().set(10.0);
        rectangle.widthProperty().set(1000.0);
        rectangle.heightProperty().set(1000.0);

        view = rectangleViewBuilder.getResult();

        assertEquals(view.getX(), rectangle.xProperty().getValue());
        assertEquals(view.getY(), rectangle.yProperty().getValue());
        assertEquals(view.getWidth(), rectangle.widthProperty().getValue());
        assertEquals(view.getHeight(), rectangle.heightProperty().getValue());
    }

    @Test
    void testReset() throws IllegalArgumentException, IllegalAccessException {
        javafx.scene.shape.Rectangle view= new javafx.scene.shape.Rectangle(1.0,1.0,100.0,100.0);
        javafx.scene.shape.Rectangle expectedView;
        resultField.set(rectangleViewBuilder, view);
        expectedView = (javafx.scene.shape.Rectangle) resultField.get(rectangleViewBuilder);
        assertEquals(expectedView, view);
        rectangleViewBuilder.reset();
        expectedView = (javafx.scene.shape.Rectangle) resultField.get(rectangleViewBuilder);
        assertNotEquals(expectedView, view);
    }
}
