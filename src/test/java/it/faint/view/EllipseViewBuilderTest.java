package it.faint.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import it.faint.model.Ellipse;

public class EllipseViewBuilderTest {
    private EllipseViewBuilder ellipseViewBuilder;
    private Ellipse ellipse;
    private Field modelField;
    private Field resultField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        ellipseViewBuilder = new EllipseViewBuilder(new Ellipse());
        modelField = EllipseViewBuilder.class.getDeclaredField("model");
        modelField.setAccessible(true);
        resultField = AbstractShapeViewBuilder.class.getDeclaredField("result");
        resultField.setAccessible(true);

        ellipse = new Ellipse(0.0,0.0,0.0,0.0);
        
    }

    @Test
    void testBindViewToSpecificShapeProperties() throws IllegalArgumentException, IllegalAccessException {
        javafx.scene.shape.Ellipse view= new javafx.scene.shape.Ellipse();

        ellipse.centerXProperty().set(1.0);
        ellipse.centerYProperty().set(1.0);
        ellipse.radiusXProperty().set(100.0);
        ellipse.radiusYProperty().set(100.0);

        resultField.set(ellipseViewBuilder, view);
        modelField.set(ellipseViewBuilder, ellipse);

        ellipseViewBuilder.bindViewToSpecificShapeProperties();

        assertEquals(view.getCenterX(), ellipse.centerXProperty().getValue());
        assertEquals(view.getCenterY(), ellipse.centerYProperty().getValue());
        assertEquals(view.getRadiusX(), ellipse.radiusXProperty().getValue());
        assertEquals(view.getRadiusY(), ellipse.radiusYProperty().getValue());

        ellipse.centerXProperty().set(10.0);
        ellipse.centerYProperty().set(10.0);
        ellipse.radiusXProperty().set(1000.0);
        ellipse.radiusYProperty().set(1000.0);

        view = (javafx.scene.shape.Ellipse) resultField.get(ellipseViewBuilder);

        assertEquals(view.getCenterX(), ellipse.centerXProperty().getValue());
        assertEquals(view.getCenterY(), ellipse.centerYProperty().getValue());
        assertEquals(view.getRadiusX(), ellipse.radiusXProperty().getValue());
        assertEquals(view.getRadiusY(), ellipse.radiusYProperty().getValue());
    }

    @Test
    void testGetResult() throws IllegalArgumentException, IllegalAccessException {
        javafx.scene.shape.Ellipse view= new javafx.scene.shape.Ellipse();

        ellipse.centerXProperty().set(1.0);
        ellipse.centerYProperty().set(1.0);
        ellipse.radiusXProperty().set(100.0);
        ellipse.radiusYProperty().set(100.0);

        resultField.set(ellipseViewBuilder, view);
        modelField.set(ellipseViewBuilder, ellipse);

        ellipseViewBuilder.bindViewToSpecificShapeProperties();

        ellipse.centerXProperty().set(10.0);
        ellipse.centerYProperty().set(10.0);
        ellipse.radiusXProperty().set(1000.0);
        ellipse.radiusYProperty().set(1000.0);

        view = ellipseViewBuilder.getResult();

        assertEquals(view.getCenterX(), ellipse.centerXProperty().getValue());
        assertEquals(view.getCenterY(), ellipse.centerYProperty().getValue());
        assertEquals(view.getRadiusX(), ellipse.radiusXProperty().getValue());
        assertEquals(view.getRadiusY(), ellipse.radiusYProperty().getValue());
    }

    @Test
    void testReset() throws IllegalArgumentException, IllegalAccessException {
        javafx.scene.shape.Ellipse view= new javafx.scene.shape.Ellipse(1.0,1.0,100.0,100.0);
        javafx.scene.shape.Ellipse expectedView;
        resultField.set(ellipseViewBuilder, view);
        expectedView = (javafx.scene.shape.Ellipse) resultField.get(ellipseViewBuilder);
        assertEquals(expectedView, view);
        ellipseViewBuilder.reset();
        expectedView = (javafx.scene.shape.Ellipse) resultField.get(ellipseViewBuilder);
        assertNotEquals(expectedView, view);
    }
}
