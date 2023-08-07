package it.faint.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.faint.model.Ellipse;
import it.faint.model.Line;
import it.faint.model.Rectangle;
import javafx.scene.paint.Color;

public class DefaultShapeViewDirectorTest {
    private Field builderField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        builderField = DefaultShapeViewDirector.class.getDeclaredField("builder");
        builderField.setAccessible(true);
    }

    @Test
    void testChangeBuilder() throws IllegalArgumentException, IllegalAccessException {
        DefaultShapeViewDirector director = DefaultShapeViewDirector.getInstance();
        AbstractShapeViewBuilder b = new RectangleViewBuilder(new Rectangle());
        director.changeBuilder(b);
        assertEquals(b, builderField.get(director));
        b = new EllipseViewBuilder(new Ellipse());
        director.changeBuilder(b);
        assertEquals(b, builderField.get(director));
        b = new LineViewBuilder(new Line());
        director.changeBuilder(b);
        assertEquals(b, builderField.get(director));
    }

    @Test
    void testGetInstance() {
        assertEquals(DefaultShapeViewDirector.getInstance(), DefaultShapeViewDirector.getInstance());
    }

    @Test
    void testMake() {
        DefaultShapeViewDirector director = DefaultShapeViewDirector.getInstance();
        //Rectangle
        Rectangle r = new Rectangle();
        AbstractShapeViewBuilder b = new RectangleViewBuilder(r);
        assertThrows(RuntimeException.class,()-> director.make());
        director.changeBuilder(b);
        director.make();
        javafx.scene.shape.Rectangle rView = (javafx.scene.shape.Rectangle) b.getResult();
        r.xProperty().set(23.5);
        assertEquals(23.5, rView.getX());
        r.yProperty().set(-87.4);
        assertEquals(-87.4, rView.getY());
        r.widthProperty().set(11);
        assertEquals(11, rView.getWidth());
        r.heightProperty().set(76);
        assertEquals(76, rView.getHeight());
        r.setFill(Color.RED);
        assertEquals(Color.RED.toString(), rView.getFill().toString());
        r.setStroke(Color.BLUE);
        assertEquals(Color.BLUE.toString(), rView.getStroke().toString());
        //Ellipse
        Ellipse e = new Ellipse();
        b = new EllipseViewBuilder(e);
        director.changeBuilder(b);
        director.make();
        javafx.scene.shape.Ellipse eView = (javafx.scene.shape.Ellipse) b.getResult();
        e.centerXProperty().set(23.5);
        assertEquals(23.5, eView.getCenterX());
        e.centerYProperty().set(-87.4);
        assertEquals(-87.4, eView.getCenterY());
        e.radiusXProperty().set(11);
        assertEquals(11, eView.getRadiusX());
        e.radiusYProperty().set(76);
        assertEquals(76, eView.getRadiusY());
        e.setFill(Color.RED);
        assertEquals(Color.RED.toString(), eView.getFill().toString());
        e.setStroke(Color.BLUE);
        assertEquals(Color.BLUE.toString(), eView.getStroke().toString());
        //Line
        Line l = new Line();
        b = new LineViewBuilder(l);
        director.changeBuilder(b);
        director.make();
        javafx.scene.shape.Line lView = (javafx.scene.shape.Line) b.getResult();
        l.startXProperty().set(23.5);
        assertEquals(23.5, lView.getStartX());
        l.startYProperty().set(-87.4);
        assertEquals(-87.4, lView.getStartY());
        l.endXProperty().set(11);
        assertEquals(11, lView.getEndX());
        l.endYProperty().set(76);
        assertEquals(76, lView.getEndY());
        l.setFill(Color.RED);
        assertEquals(Color.RED.toString(), lView.getFill().toString());
        l.setStroke(Color.BLUE);
        assertEquals(Color.BLUE.toString(), lView.getStroke().toString());
    }
}
