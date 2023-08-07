package it.faint.model;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.faint.model.Drawing.Event;
import it.faint.model.Drawing.Subscriber;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class DrawingTest {
    private Drawing drawing;

    @BeforeEach
    public void setUp(){
        drawing=new Drawing();
    }

    @Test
    void testAddPreview() {
        List<Shape> shapeList = new LinkedList<>();
        shapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        shapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        shapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        shapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        shapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        shapeList.add(new Line(450.0, 450.0, 75.0, 42.0));
        for (Shape shape : shapeList) {
            drawing.addPreview(shape.getBoundingBox());
        }

        List<Shape> resultPreviewList=drawing.getAllPreviews();

        assertEquals(shapeList.size(), resultPreviewList.size());
        for (Shape shape : shapeList) {
            assertEquals(shape.getBoundingBox(),resultPreviewList.get(resultPreviewList.indexOf(shape.getBoundingBox())));
        }
    }

    @Test
    void testAddPreviewListener() {

    }

    @Test
    void testAddShape() {
        List<Shape> expectedShapeList = new LinkedList<>();
        expectedShapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        expectedShapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        expectedShapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        expectedShapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        expectedShapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        expectedShapeList.add(new Line(450.0, 450.0, 75.0, 42.0));

        for (Shape shape : expectedShapeList) {
            drawing.addShape(shape);
        }

        List<Shape> resultShapeList=drawing.getAllShapes();

        assertEquals(expectedShapeList.size(), resultShapeList.size());
        assertEquals(expectedShapeList,resultShapeList);
    }

    @Test
    void testAddShape2() {
        List<Shape> expectedShapeList = new LinkedList<>();
        expectedShapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        expectedShapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        expectedShapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        expectedShapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        expectedShapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        expectedShapeList.add(new Line(450.0, 450.0, 75.0, 42.0));
        int i=0;
        for (Shape shape : expectedShapeList) {
            drawing.addShape(i,shape);
            i++;
        }

        List<Shape> resultShapeList=drawing.getAllShapes();

        assertEquals(expectedShapeList.size(), resultShapeList.size());
        assertEquals(expectedShapeList,resultShapeList);
    }

    @Test
    void testAddShapesListener() {

    }

    @Test
    void testClearPreviews() {
        assertEquals(0,drawing.getAllPreviews().size());
        List<Shape> shapeList = new LinkedList<>();
        shapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        shapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        shapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        shapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        shapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        shapeList.add(new Line(450.0, 450.0, 75.0, 42.0));
        for (Shape shape : shapeList) {
            drawing.addPreview(shape.getBoundingBox());
        }
        
        assertNotEquals(0,drawing.getAllPreviews().size());

        drawing.clearPreviews();
        assertEquals(0,drawing.getAllPreviews().size());
    }

    @Test
    void testClearShapes() {
        assertEquals(0,drawing.getAllShapes().size());
        drawing.addShape(new Rectangle(1.0, 1.0, 10.0, 10.0));
        drawing.addShape(new Rectangle(10.5, 10.5, 20.0, 20.0));
        drawing.addShape(new Ellipse(100.0, 100.0, 10.0, 20.0));
        drawing.addShape(new Ellipse(150.0, 150.0, 20.0, 20.0));
        drawing.addShape(new Line(20.0, 20.0, 10.0, 20.0));
        drawing.addShape(new Line(450.0, 450.0, 75.0, 42.0));
        
        assertNotEquals(0,drawing.getAllShapes().size());

        drawing.clearShapes();
        assertEquals(0,drawing.getAllShapes().size());
    }

    @Test
    void testFireEvent() {
        DrawEditor editor1=new DrawEditor(drawing);
        DrawEditor editor2=new DrawEditor(drawing);
        Rectangle r=new Rectangle(1.0, 1.0, 10.0, 10.0);
        drawing.addShape(r);

        Event event = drawing.new Event(Drawing.EventType.MOUSE_PRESSED, MouseButton.PRIMARY, new Point2D(5.0, 5.0), r, null);
        drawing.fireEvent(event);

        assertEquals(r,editor1.selectedShapeProperty().get());
        assertEquals(r,editor2.selectedShapeProperty().get());
    }

    @Test
    void testGetAllPreviews() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field previews=Drawing.class.getDeclaredField("previews");
        previews.setAccessible(true);
        List<Shape> shapeList = new LinkedList<>();
        shapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        shapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        shapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        shapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        shapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        shapeList.add(new Line(450.0, 450.0, 75.0, 42.0));
        for (Shape shape : shapeList) {
            drawing.addPreview(shape.getBoundingBox());
        }

        List<Shape> expectedShapeList=(List<Shape>) previews.get(drawing);
        List<Shape> resultShapeList = drawing.getAllPreviews();

        assertEquals(expectedShapeList,resultShapeList);
    }

    @Test
    void testGetAllShapes() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        List<Shape> shapeList = new LinkedList<>();
        Field shapes=Drawing.class.getDeclaredField("shapes");
        shapes.setAccessible(true);
        shapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        shapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        shapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        shapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        shapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        shapeList.add(new Line(450.0, 450.0, 75.0, 42.0));

        drawing.insertAllShapes(shapeList);

        List<Shape> expectedShapeList=(List<Shape>) shapes.get(drawing);
        List<Shape> resultShapeList = drawing.getAllShapes();

        assertEquals(expectedShapeList,resultShapeList);
    }

    @Test
    void testGetPreview() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        Field previews=Drawing.class.getDeclaredField("previews");
        previews.setAccessible(true);
        List<Shape> shapeList = new LinkedList<>();
        shapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        shapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        shapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        shapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        shapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        shapeList.add(new Line(450.0, 450.0, 75.0, 42.0));
        for (Shape shape : shapeList) {
            drawing.addPreview(shape.getBoundingBox());
        }

        List<Shape> expectedPreviewsList=(List<Shape>) previews.get(drawing);
        Iterator<Shape> iter=shapeList.iterator();
        for (Shape shape : expectedPreviewsList) {
            Shape result=drawing.getPreview(expectedPreviewsList.indexOf(shape));
            Shape expected=iter.next();
            assertEquals(expected.getBoundingBox(),result);
        }
    }

    @Test
    void testGetShape() {
        List<Shape> expectedShapeList = new LinkedList<>();
        expectedShapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        expectedShapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        expectedShapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        expectedShapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        expectedShapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        expectedShapeList.add(new Line(450.0, 450.0, 75.0, 42.0));

        drawing.insertAllShapes(expectedShapeList);

        for (Shape shape : expectedShapeList) {
            Shape result=drawing.getShape(drawing.getShapeIndex(shape));
            assertEquals(shape,result);
        }
    }

    @Test
    void testGetShapeIndex() {
        List<Shape> expectedShapeList = new LinkedList<>();
        expectedShapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        expectedShapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        expectedShapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        expectedShapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        expectedShapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        expectedShapeList.add(new Line(450.0, 450.0, 75.0, 42.0));

        drawing.insertAllShapes(expectedShapeList);

        List<Shape> list=drawing.getAllShapes();
        
        for (Shape shape : expectedShapeList) {
            int expected=list.indexOf(shape);
            int result=drawing.getShapeIndex(shape);
            assertEquals(expected,result);
        }
    }

    @Test
    void testGridSizeProperty() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field gridSize=Drawing.class.getDeclaredField("gridSize");
        gridSize.setAccessible(true);
        DoubleProperty expected=(DoubleProperty) gridSize.get(drawing);

        ReadOnlyDoubleProperty result=drawing.gridSizeProperty();
        assertEquals(expected.get(),result.get());
    }

    @Test
    void testHeightProperty() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field height=Drawing.class.getDeclaredField("height");
        height.setAccessible(true);
        DoubleProperty expected=(DoubleProperty) height.get(drawing);

        ReadOnlyDoubleProperty result=drawing.heightProperty();
        assertEquals(expected.get(),result.get());
    }

    @Test
    void testHideGrid() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field isGridVisible=Drawing.class.getDeclaredField("isGridVisible");
        isGridVisible.setAccessible(true);
        drawing.hideGrid();
        BooleanProperty result=(BooleanProperty) isGridVisible.get(drawing);
        assertEquals(false,result.get());
    }

    @Test
    void testInsertAllShapes() {
        List<Shape> expectedShapeList = new LinkedList<>();
        expectedShapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        expectedShapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        expectedShapeList.add(new Ellipse(152.0, 152.0, 10.0, 20.0));
        expectedShapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        expectedShapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        expectedShapeList.add(new Line(650.0, 420.0, 75.0, 42.0));

        drawing.insertAllShapes(expectedShapeList);

        List<Shape> resultShapeList=drawing.getAllShapes();

        assertEquals(expectedShapeList.size(), resultShapeList.size());
        assertEquals(expectedShapeList,resultShapeList);
    }

    @Test
    void testIsGridVisibleProperty() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field isGridVisible=Drawing.class.getDeclaredField("isGridVisible");
        isGridVisible.setAccessible(true);
        BooleanProperty expected=(BooleanProperty) isGridVisible.get(drawing);

        ReadOnlyBooleanProperty result=drawing.isGridVisibleProperty();
        assertEquals(expected.get(),result.get());
    }

    @Test
    void testRemovePreview() {
        assertEquals(0,drawing.getAllPreviews().size());
        List<Shape> shapeList = new LinkedList<>();
        shapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        shapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        shapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        shapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        shapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        shapeList.add(new Line(450.0, 450.0, 75.0, 42.0));
        for (Shape shape : shapeList) {
            drawing.addPreview(shape.getBoundingBox());
        }

        List<Shape> list=drawing.getAllPreviews();
        assertNotEquals(0,list.size());

        for (Shape preview : list) {
            drawing.removePreview(preview);
        }
        assertEquals(0,drawing.getAllPreviews().size());
    }

    @Test
    void testRemoveShape() {
        assertEquals(0,drawing.getAllShapes().size());
        drawing.addShape(new Rectangle(1.0, 1.0, 10.0, 10.0));
        drawing.addShape(new Rectangle(10.5, 10.5, 20.0, 20.0));
        drawing.addShape(new Ellipse(100.0, 100.0, 10.0, 20.0));
        drawing.addShape(new Ellipse(150.0, 150.0, 20.0, 20.0));
        drawing.addShape(new Line(20.0, 20.0, 10.0, 20.0));
        drawing.addShape(new Line(450.0, 450.0, 75.0, 42.0));
        
        List<Shape> list=drawing.getAllShapes();
        assertNotEquals(0,list.size());
        for (Shape shape : list) {
            drawing.removeShape(shape);
        }
        assertEquals(0,drawing.getAllShapes().size());
    }

    @Test
    void testRemoveShape2() {
        assertEquals(0,drawing.getAllShapes().size());
        drawing.addShape(new Rectangle(1.0, 1.0, 10.0, 10.0));
        drawing.addShape(new Rectangle(10.5, 10.5, 20.0, 20.0));
        drawing.addShape(new Ellipse(100.0, 100.0, 10.0, 20.0));
        drawing.addShape(new Ellipse(150.0, 150.0, 20.0, 20.0));
        drawing.addShape(new Line(20.0, 20.0, 10.0, 20.0));
        drawing.addShape(new Line(450.0, 450.0, 75.0, 42.0));
        
        List<Shape> list=drawing.getAllShapes();
        assertNotEquals(0,list.size());
        for (Shape shape : list) {
            drawing.removeShape(drawing.getShapeIndex(shape));
        }
        assertEquals(0,drawing.getAllShapes().size());
    }

    @Test
    void testSetGridSize() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field gridSize=Drawing.class.getDeclaredField("gridSize");
        gridSize.setAccessible(true);
        DoubleProperty result=(DoubleProperty) gridSize.get(drawing);
        double expected=20.0;
        for(int i=1; i<=5; i++){
            drawing.setGridSize(expected*i);
            assertEquals(expected*i,result.get());
        }
    }

    @Test
    void testSetPreview() {
        assertEquals(0,drawing.getAllPreviews().size());
        List<Shape> shapeList = new LinkedList<>();
        shapeList.add(new Rectangle(1.0, 1.0, 10.0, 10.0));
        shapeList.add(new Rectangle(10.5, 10.5, 20.0, 20.0));
        shapeList.add(new Ellipse(100.0, 100.0, 10.0, 20.0));
        shapeList.add(new Ellipse(150.0, 150.0, 20.0, 20.0));
        shapeList.add(new Line(20.0, 20.0, 10.0, 20.0));
        shapeList.add(new Line(450.0, 450.0, 75.0, 42.0));
        for (Shape shape : shapeList) {
            drawing.addPreview(shape.getBoundingBox());
        }
     
        Rectangle r=new Rectangle(10.0, 10.0, 100.0, 100.0);
        drawing.setPreview(r.getBoundingBox());

        List<Shape> previewList=drawing.getAllPreviews();
        assertEquals(1,previewList.size());
        assertEquals(r.getBoundingBox(),previewList.get(previewList.indexOf(r.getBoundingBox())));
    }

    @Test
    void testShowGrid() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field isGridVisible=Drawing.class.getDeclaredField("isGridVisible");
        isGridVisible.setAccessible(true);
        drawing.showGrid();
        BooleanProperty result=(BooleanProperty) isGridVisible.get(drawing);
        assertEquals(true,result.get());
    }

    @Test
    void testSubscribe() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        DrawEditor drawEditor=new DrawEditor(drawing); // Nel costruttore DrawEditor usa drawing.subscribe()
        
        Field subscribers=Drawing.class.getDeclaredField("subscribers");
        subscribers.setAccessible(true);

        List<Subscriber> list=(List<Subscriber>) subscribers.get(drawing);
        assertEquals(drawEditor,list.get(0));
    }

    @Test
    void testUnsubscribe() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        DrawEditor drawEditor=new DrawEditor(drawing);
        
        Field subscribers=Drawing.class.getDeclaredField("subscribers");
        subscribers.setAccessible(true);

        List<Subscriber> list=(List<Subscriber>) subscribers.get(drawing);
        assertEquals(1,list.size());

        drawing.unsubscribe(drawEditor);
        assertEquals(0,list.size());
    }

    @Test
    void testWidthProperty() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        Field width=Drawing.class.getDeclaredField("width");
        width.setAccessible(true);
        DoubleProperty expected=(DoubleProperty) width.get(drawing);

        ReadOnlyDoubleProperty result=drawing.widthProperty();
        assertEquals(expected.get(),result.get());
    }
}
