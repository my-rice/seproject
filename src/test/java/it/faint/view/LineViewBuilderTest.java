package it.faint.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.faint.model.Line;

public class LineViewBuilderTest {
    private LineViewBuilder lineViewBuilder;
    private Line line;
    private Field modelField;
    private Field resultField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        lineViewBuilder = new LineViewBuilder(new Line());
        modelField = LineViewBuilder.class.getDeclaredField("model");
        modelField.setAccessible(true);
        resultField = AbstractShapeViewBuilder.class.getDeclaredField("result");
        resultField.setAccessible(true);

        line = new Line(0.0,0.0,0.0,0.0);
        
    }

    @Test
    void testBindViewToSpecificShapeProperties() throws IllegalArgumentException, IllegalAccessException {
        javafx.scene.shape.Line view= new javafx.scene.shape.Line();

        line.startXProperty().set(1.0);
        line.startYProperty().set(1.0);
        line.endXProperty().set(100.0);
        line.endYProperty().set(100.0);

        resultField.set(lineViewBuilder, view);
        modelField.set(lineViewBuilder, line);

        lineViewBuilder.bindViewToSpecificShapeProperties();

        assertEquals(view.getStartX(), line.startXProperty().getValue());
        assertEquals(view.getStartY(), line.startYProperty().getValue());
        assertEquals(view.getEndX(), line.endXProperty().getValue());
        assertEquals(view.getEndY(), line.endYProperty().getValue());

        line.startXProperty().set(10.0);
        line.startYProperty().set(10.0);
        line.endXProperty().set(1000.0);
        line.endYProperty().set(1000.0);

        view = (javafx.scene.shape.Line) resultField.get(lineViewBuilder);

        assertEquals(view.getStartX(), line.startXProperty().getValue());
        assertEquals(view.getStartY(), line.startYProperty().getValue());
        assertEquals(view.getEndX(), line.endXProperty().getValue());
        assertEquals(view.getEndY(), line.endYProperty().getValue());
    }

    @Test
    void testGetResult() throws IllegalArgumentException, IllegalAccessException {
        javafx.scene.shape.Line view= new javafx.scene.shape.Line();

        line.startXProperty().set(1.0);
        line.startYProperty().set(1.0);
        line.endXProperty().set(100.0);
        line.endYProperty().set(100.0);

        resultField.set(lineViewBuilder, view);
        modelField.set(lineViewBuilder, line);

        lineViewBuilder.bindViewToSpecificShapeProperties();

        line.startXProperty().set(10.0);
        line.startYProperty().set(10.0);
        line.endXProperty().set(1000.0);
        line.endYProperty().set(1000.0);

        view = lineViewBuilder.getResult();

        assertEquals(view.getStartX(), line.startXProperty().getValue());
        assertEquals(view.getStartY(), line.startYProperty().getValue());
        assertEquals(view.getEndX(), line.endXProperty().getValue());
        assertEquals(view.getEndY(), line.endYProperty().getValue());
    }

    @Test
    void testReset() throws IllegalArgumentException, IllegalAccessException {
        javafx.scene.shape.Line view= new javafx.scene.shape.Line(1.0,1.0,100.0,100.0);
        javafx.scene.shape.Line expectedView;
        resultField.set(lineViewBuilder, view);
        expectedView = (javafx.scene.shape.Line) resultField.get(lineViewBuilder);
        assertEquals(expectedView, view);
        lineViewBuilder.reset();
        expectedView = (javafx.scene.shape.Line) resultField.get(lineViewBuilder);
        assertNotEquals(expectedView, view);
    }
}
