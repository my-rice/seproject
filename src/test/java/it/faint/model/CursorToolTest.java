package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.faint.model.Drawing.EventType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

public class CursorToolTest {
    private CursorTool cursorTool;
    private DrawEditor drawEditor;
    private Drawing drawing;
    private Field prevSelectedFillColorField,prevSelectedContourColorField,prevBottomRightField;
    private Field isSelectedField,selectedShapeField,isMovingField;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException{
        drawing = new Drawing();
        drawEditor = new DrawEditor(drawing);
        cursorTool = new CursorTool(drawEditor);
        
        prevSelectedFillColorField = CursorTool.class.getDeclaredField("prevSelectedFillColor");
        prevSelectedFillColorField.setAccessible(true);
        
        prevSelectedContourColorField = CursorTool.class.getDeclaredField("prevSelectedContourColor");
        prevSelectedContourColorField.setAccessible(true);
        
        isSelectedField = CursorTool.class.getDeclaredField("isSelected");
        isSelectedField.setAccessible(true);
        
        isMovingField = CursorTool.class.getDeclaredField("isMoving");
        isMovingField.setAccessible(true);

        selectedShapeField = DrawEditor.class.getDeclaredField("selectedShape");  
        selectedShapeField.setAccessible(true);

        prevBottomRightField = CursorTool.class.getDeclaredField("prevBottomRight");
        prevBottomRightField.setAccessible(true);
        
    }

    @Test
    void testHandlePrimaryPressedRectangle() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        //Testing the case when a rectangle is not selected yet

        Color colorFillExpected = Color.WHITE;
        Color colorContourExpected = Color.BLACK;
        Boolean isSelectedExpected = true;
        Point2D expectedFirstPoint;

        Drawing.Event event;
        ObjectProperty<Shape> selectedShape;
        Color resultCursorColor;
        Color resultFillColor;
        Boolean resultIsSelected;

        List<Point2D> rectangleMouseClickPoints = new ArrayList<>();
        List<Rectangle> rectangleList = new ArrayList<>();
        
        drawEditor.setSelectedBorderColor(colorContourExpected);
        drawEditor.setSelectedFillColor(colorFillExpected);
        
        Point2D firstPoint;
        Rectangle rectangle;
        
        firstPoint = new Point2D(1.0, 1.0);
        rectangle = new Rectangle(firstPoint.getX(),firstPoint.getY(),4.0,5.0);
        rectangle.setFill(Color.ANTIQUEWHITE);
        rectangle.setStroke(Color.AQUA);
        rectangleMouseClickPoints.add(new Point2D(2.0, 2.0));
        rectangleList.add(rectangle);
        
        firstPoint = new Point2D(500.0, 500.0);
        rectangle = new Rectangle(firstPoint.getX(),firstPoint.getY(),40.0,50.0);
        rectangle.setFill(Color.DARKCYAN);
        rectangle.setStroke(Color.LIGHTPINK);
        rectangleMouseClickPoints.add(new Point2D(520.0, 520.0));
        rectangleList.add(rectangle);
        
        firstPoint = new Point2D(-50.0, -40.0);
        rectangle = new Rectangle(firstPoint.getX(),firstPoint.getY(),60.0,70.0);
        rectangle.setFill(Color.DARKCYAN);
        rectangle.setStroke(Color.LIGHTPINK);
        rectangleMouseClickPoints.add(new Point2D(10.0, 10.0));
        rectangleList.add(rectangle);
        
        
        Rectangle e;
        for (int i=0;i<rectangleList.size();i++){
            e = rectangleList.get(i);
            drawing.addShape(e);

            expectedFirstPoint = rectangleMouseClickPoints.get(i);
            event = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, rectangleMouseClickPoints.get(i), e, null);
            cursorTool.handlePressed(event);
            selectedShape = (ObjectProperty<Shape>) selectedShapeField.get(drawEditor);
            
            resultCursorColor = (Color)this.prevSelectedContourColorField.get(cursorTool);
            resultFillColor = (Color)this.prevSelectedFillColorField.get(cursorTool);
            resultIsSelected = ((BooleanProperty)this.isSelectedField.get(cursorTool)).getValue();

            assertEquals(colorContourExpected, resultCursorColor);
            assertEquals(colorFillExpected, resultFillColor);
            assertEquals(isSelectedExpected,resultIsSelected);
            assertEquals(expectedFirstPoint, rectangleMouseClickPoints.get(i));
        }

    }

    @Test
    void testHandlePrimaryPressedEllipse() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        //Testing the selection of an ellipse
        Color colorFillExpected = Color.WHITE;
        Color colorContourExpected = Color.BLACK;
        Boolean isSelectedExpected = true;
        Point2D expectedFirstPoint;

        Drawing.Event event;
        ObjectProperty<Shape> selectedShape;
        Color resultCursorColor;
        Color resultFillColor;
        Boolean resultIsSelected;


        List<Point2D> ellipseMouseClickPoints = new ArrayList<>();
        List<Ellipse> ellipseList = new ArrayList<>();
        
        drawEditor.setSelectedBorderColor(colorContourExpected);
        drawEditor.setSelectedFillColor(colorFillExpected);

        Ellipse ellipse;
        Point2D centerPointEllipse;
        Point2D radiusPointEllipse;
        
        centerPointEllipse = new Point2D(100.0, 100.0);
        radiusPointEllipse = new Point2D(20.0, 20.0);
        ellipseMouseClickPoints.add(new Point2D(105.0, 105.0));
        ellipse = new Ellipse(centerPointEllipse.getX(), centerPointEllipse.getY(),radiusPointEllipse.getX(),radiusPointEllipse.getY());
        ellipse.setFill(Color.ANTIQUEWHITE);
        ellipse.setStroke(Color.AQUA);
        ellipseList.add(ellipse);
        
        centerPointEllipse = new Point2D(50.0, 10.0);
        radiusPointEllipse = new Point2D(2.0, 5.0);
        ellipseMouseClickPoints.add(new Point2D(50.0, 10.0));
        ellipse = new Ellipse(centerPointEllipse.getX(), centerPointEllipse.getY(),radiusPointEllipse.getX(),radiusPointEllipse.getY());
        ellipse.setFill(Color.ANTIQUEWHITE);
        ellipse.setStroke(Color.AQUA);
        ellipseList.add(ellipse);

        centerPointEllipse = new Point2D(50.0, 10.0);
        radiusPointEllipse = new Point2D(2.0, 5.0);
        ellipseMouseClickPoints.add(new Point2D(50.0, 10.0));
        ellipse = new Ellipse(centerPointEllipse.getX(), centerPointEllipse.getY(),radiusPointEllipse.getX(),radiusPointEllipse.getY());
        ellipse.setFill(Color.ANTIQUEWHITE);
        ellipse.setStroke(Color.AQUA);
        ellipseList.add(ellipse);

        Ellipse e;
        for (int i=0;i<ellipseList.size();i++){
            e = ellipseList.get(i);
            drawing.addShape(e);

            expectedFirstPoint = ellipseMouseClickPoints.get(i);
            event = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, ellipseMouseClickPoints.get(i), e, null);
            cursorTool.handlePressed(event);
            selectedShape = (ObjectProperty<Shape>) selectedShapeField.get(drawEditor);
            
            resultCursorColor = (Color)this.prevSelectedContourColorField.get(cursorTool);
            resultFillColor = (Color)this.prevSelectedFillColorField.get(cursorTool);
            resultIsSelected = ((BooleanProperty)this.isSelectedField.get(cursorTool)).getValue();

            assertEquals(colorContourExpected, resultCursorColor);
            assertEquals(colorFillExpected, resultFillColor);
            assertEquals(isSelectedExpected,resultIsSelected);
            assertEquals(expectedFirstPoint, ellipseMouseClickPoints.get(i));
        }

    }

    @Test
    void testHandlePrimaryPressedLine() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        //Testing the selection of a line
        Color colorFillExpected = Color.WHITE;
        Color colorContourExpected = Color.BLACK;
        Boolean isSelectedExpected = true;
        Point2D expectedFirstPoint;

        Drawing.Event event;
        ObjectProperty<Shape> selectedShape;
        Color resultCursorColor;
        Color resultFillColor;
        Boolean resultIsSelected;

        List<Point2D> lineMouseClickPoints = new ArrayList<>();
        List<Line> lineList = new ArrayList<>();
        
        drawEditor.setSelectedBorderColor(colorContourExpected);
        drawEditor.setSelectedFillColor(colorFillExpected);

        Line line;
        Point2D startPointLine;
        Point2D endPointLine;

        startPointLine = new Point2D(10.0, 10.0);
        endPointLine = new Point2D(15.0, 15.0);
        lineMouseClickPoints.add(new Point2D(12.5, 12.5));
        line = new Line(startPointLine.getX(), startPointLine.getY(), endPointLine.getX(), endPointLine.getY()); 
        line.setFill(Color.ANTIQUEWHITE);
        line.setStroke(Color.AQUA);
        lineList.add(line);
        
        startPointLine = new Point2D(80.0, 100.0);
        endPointLine = new Point2D(2.0, 4.0);
        lineMouseClickPoints.add(new Point2D(41.0, 52.0));
        line = new Line(startPointLine.getX(), startPointLine.getY(), endPointLine.getX(), endPointLine.getY()); 
        line.setFill(Color.CORNSILK);
        line.setStroke(Color.DARKCYAN);
        lineList.add(line);
        
        startPointLine = new Point2D(-10.0, -10.0);
        endPointLine = new Point2D(10.0, 10.0);
        lineMouseClickPoints.add(new Point2D(0, 0));
        line = new Line(startPointLine.getX(), startPointLine.getY(), endPointLine.getX(), endPointLine.getY()); 
        line.setFill(Color.DARKKHAKI);
        line.setStroke(Color.DIMGREY);
        lineList.add(line);
        
        Line e;
        for (int i=0;i<lineList.size();i++){
            e = lineList.get(i);
            drawing.addShape(e);

            expectedFirstPoint = lineMouseClickPoints.get(i);
            event = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, lineMouseClickPoints.get(i), e, null);
            cursorTool.handlePressed(event);
            selectedShape = (ObjectProperty<Shape>) selectedShapeField.get(drawEditor);
            
            resultCursorColor = (Color)this.prevSelectedContourColorField.get(cursorTool);
            resultFillColor = (Color)this.prevSelectedFillColorField.get(cursorTool);
            resultIsSelected = ((BooleanProperty)this.isSelectedField.get(cursorTool)).getValue();

            assertEquals(colorContourExpected, resultCursorColor);
            assertEquals(colorFillExpected, resultFillColor);
            assertEquals(isSelectedExpected,resultIsSelected);
            assertEquals(expectedFirstPoint, lineMouseClickPoints.get(i));
        }
    }
    @Test
    void testHandlePrimaryPressedGizmo() throws IllegalArgumentException, IllegalAccessException {
        //Testing the gizmo

        Drawing.Event event;
        Point2D expectedPrevBottomRight,result, mouseClick = new Point2D(53.0, 57.0);

        Rectangle rectangle = new Rectangle(5.0, 5.0, 53.0, 57.0);
        ResizeGizmo resizeGizmo= rectangle.getResizeGizmo();
        expectedPrevBottomRight = new Point2D(resizeGizmo.getTarget().boundsProperty().get().getMaxX(),resizeGizmo.getTarget().boundsProperty().get().getMaxY());
        
        event = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, mouseClick,resizeGizmo, null);
        cursorTool.handlePressed(event);
        result = (Point2D) prevBottomRightField.get(cursorTool);
        assertEquals(expectedPrevBottomRight, result);

    }
    @Test
    void testHandlePrimaryPressed2() throws IllegalArgumentException, IllegalAccessException {
        //When a shape is selected, clicking outside the shape will deselect the shape
        Drawing.Event event;
        ObjectProperty<Shape> selectedShape;
        Boolean resultIsSelected;
        Boolean isSelectedExpected;

        //Selecting a shape
        Point2D firstPoint;
        Rectangle rectangle;
        
        firstPoint = new Point2D(1.0, 1.0);
        rectangle = new Rectangle(firstPoint.getX(),firstPoint.getY(),4.0,5.0);
        rectangle.setFill(Color.ANTIQUEWHITE);
        rectangle.setStroke(Color.AQUA);
        Point2D rectangleMouseClickPoint = new Point2D(2.0, 2.0);
        event = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, rectangleMouseClickPoint, rectangle, null);
        cursorTool.handlePressed(event);
        selectedShape = (ObjectProperty<Shape>) selectedShapeField.get(drawEditor);
        resultIsSelected = ((BooleanProperty)this.isSelectedField.get(cursorTool)).getValue();

        //Now the shape is selected
        isSelectedExpected = true;
        assertEquals(isSelectedExpected,resultIsSelected);
            
        //Clicking outside 
        Point2D mouseClickPoint = new Point2D(100.0, 200.0);
        event = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, mouseClickPoint, null, null);
        cursorTool.handlePressed(event);
        selectedShape = (ObjectProperty<Shape>) selectedShapeField.get(drawEditor);
        resultIsSelected = ((BooleanProperty)this.isSelectedField.get(cursorTool)).getValue();

        //Now the shape is deselected
        isSelectedExpected = false;
        assertEquals(isSelectedExpected,resultIsSelected);
         
        
    }


    @Test
    void testHandleDragged() throws IllegalArgumentException, IllegalAccessException {

        Drawing.Event eventClick,eventDrag;
        ObjectProperty<Shape> selectedShape;
        Boolean resultIsSelected;

        Boolean isSelectedExpected = true;
        Point2D expectedFirstPoint, expectedPointAfterMovement;

        Rectangle rectangle;
        Point2D rectangleMouseClickPoint,firstPoint,localPoint,rectangleDraggedPoint;

        drawEditor.setTool(cursorTool);

        //To perform this test, first of all a shape must be drawn.
        //Drawing a rectangle ...
        firstPoint = new Point2D(1.0, 1.0);
        rectangle = new Rectangle(firstPoint.getX(),firstPoint.getY(),4.0,5.0);
        rectangle.setFill(Color.ANTIQUEWHITE);
        rectangle.setStroke(Color.AQUA);
        rectangleMouseClickPoint = new Point2D(2.0, 2.0);
        drawing.addShape(rectangle);

        expectedFirstPoint = rectangleMouseClickPoint;

        localPoint = rectangleMouseClickPoint.subtract(firstPoint.getX(), firstPoint.getY());        
        
        //Clicking on the rectangle, so it is selected.
        eventClick = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, rectangleMouseClickPoint, rectangle, localPoint);
        cursorTool.handlePressed(eventClick);
        selectedShape = (ObjectProperty<Shape>) selectedShapeField.get(drawEditor);
        
        resultIsSelected = ((BooleanProperty)this.isSelectedField.get(cursorTool)).getValue();

        //Checking if the shape is selected
        assertEquals(isSelectedExpected,resultIsSelected);
        assertEquals(expectedFirstPoint, rectangleMouseClickPoint);

        //Moving the rectangle
        rectangleDraggedPoint = new Point2D(200.0, 200.0);
        expectedPointAfterMovement = rectangleDraggedPoint.subtract(localPoint);

        eventDrag = drawing.new Event(EventType.MOUSE_DRAGGED,MouseButton.PRIMARY, rectangleDraggedPoint, rectangle, null);
        cursorTool.handleDragged(eventDrag);

        //Final test. Checking if the shape moved properly
        assertEquals(expectedPointAfterMovement.getX(),rectangle.x.get());
        assertEquals(expectedPointAfterMovement.getY(),rectangle.y.get());

    }

    @Test
    void testDismiss() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        selectedShapeField = DrawEditor.class.getDeclaredField("selectedShape");
        selectedShapeField.setAccessible(true);
        ObjectProperty<Shape> selectedShapeDrawEditor;

        //No shape is selected. Testing dismiss method.
        cursorTool.dismiss();
        selectedShapeDrawEditor = (ObjectProperty<Shape>) selectedShapeField.get(drawEditor);
        assertNull(selectedShapeDrawEditor.getValue());

        
        //Selecting a shape so selectedShape in drawEditor is not null
        Point2D firstPoint = new Point2D(1.0, 1.0);
        Rectangle rectangle = new Rectangle(firstPoint.getX(),firstPoint.getY(),4.0,5.0);
        rectangle.setFill(Color.ANTIQUEWHITE);
        rectangle.setStroke(Color.AQUA);
        Point2D rectangleMouseClickPoint = new Point2D(2.0, 2.0);
        Drawing.Event event = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, rectangleMouseClickPoint, rectangle, null);
        cursorTool.handlePressed(event);

        selectedShapeDrawEditor = (ObjectProperty<Shape>) selectedShapeField.get(drawEditor);
        assertNotNull(selectedShapeDrawEditor);

        //Testing dismiss method
        cursorTool.dismiss();
        selectedShapeDrawEditor = (ObjectProperty<Shape>) selectedShapeField.get(drawEditor);
        assertNull(selectedShapeDrawEditor.getValue());
    }

    @Test
    void testHandleReleased() throws IllegalArgumentException, IllegalAccessException {

        //This test case check only the else branch of the method and not the if branches. 
        //This is because all the if branches don't have side effects. 


        Drawing.Event eventClick,eventDrag,eventRealese;
        ObjectProperty<Shape> selectedShape;
        Boolean resultIsSelected,resultIsMoving;

        Boolean isSelectedExpected = true;
        Point2D expectedFirstPoint,expectedPointAfterMovement,expectedPointAfterRelease;

        Rectangle rectangle;
        Point2D rectangleMouseClickPoint,rectangleDraggedPoint,rectangleReleasedPoint,firstPoint,localPoint;

        //To perform this test, first of all a shape must be drawn.
        //Drawing a rectangle ...
        firstPoint = new Point2D(1.0, 1.0);
        rectangle = new Rectangle(firstPoint.getX(),firstPoint.getY(),4.0,5.0);
        rectangle.setFill(Color.ANTIQUEWHITE);
        rectangle.setStroke(Color.AQUA);
        rectangleMouseClickPoint = new Point2D(2.0, 2.0);
        drawing.addShape(rectangle);

        expectedFirstPoint = rectangleMouseClickPoint;

        localPoint = rectangleMouseClickPoint.subtract(firstPoint.getX(), firstPoint.getY());        
        
        //Clicking on the rectangle, so it is selected.
        eventClick = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, rectangleMouseClickPoint, rectangle, localPoint);
        cursorTool.handlePressed(eventClick);
        selectedShape = (ObjectProperty<Shape>) selectedShapeField.get(drawEditor);
    
        resultIsSelected = ((BooleanProperty)this.isSelectedField.get(cursorTool)).getValue();

        //Checking if the shape is selected
        assertEquals(isSelectedExpected,resultIsSelected);
        assertEquals(expectedFirstPoint, rectangleMouseClickPoint);


        //Moving the rectangle
        rectangleDraggedPoint = new Point2D(250.0, 250.0);
        expectedPointAfterMovement = rectangleDraggedPoint.subtract(localPoint);

        eventDrag = drawing.new Event(EventType.MOUSE_DRAGGED,MouseButton.PRIMARY, rectangleDraggedPoint, rectangle, null);
        cursorTool.handleDragged(eventDrag);
        
        //Final test. Checking if the shape moved properly
        assertEquals(expectedPointAfterMovement.getX(),rectangle.x.get());
        assertEquals(expectedPointAfterMovement.getY(),rectangle.y.get());

        //Simulating the releasing of the rectangle after a movement
        rectangleReleasedPoint = rectangleDraggedPoint;
        expectedPointAfterRelease = rectangleReleasedPoint.subtract(localPoint);

        //Launching Releasing event
        eventRealese = drawing.new Event(EventType.MOUSE_RELEASED, MouseButton.PRIMARY, rectangleReleasedPoint, rectangle, null);
        cursorTool.handleReleased(eventRealese);

        //Check if isMoving is set correctly after the release event
        resultIsMoving = (Boolean) isMovingField.get(cursorTool);
        assertFalse(resultIsMoving);

        assertEquals(expectedPointAfterRelease.getX(),rectangle.x.get());
        assertEquals(expectedPointAfterRelease.getY(),rectangle.y.get());

    }

    private static boolean almostEqual(Point2D a, Point2D b, double eps){
        return a.distance(b)<eps;
    }

    private static void assertRectangleAlmostEqual(Rectangle a, Rectangle b){
        assertTrue(almostEqual(new Point2D(a.boundsProperty().get().getMinX(), a.boundsProperty().get().getMinY()),new Point2D(b.boundsProperty().get().getMinX(), b.boundsProperty().get().getMinY()),0.000000001));
        assertTrue(almostEqual(new Point2D(a.boundsProperty().get().getMaxX(), a.boundsProperty().get().getMaxY()),new Point2D(b.boundsProperty().get().getMaxX(), b.boundsProperty().get().getMaxY()),0.000000001));
    }

    @Test
    void testHandleReleasedGizmo() throws IllegalArgumentException, IllegalAccessException {
        
        Drawing.Event eventClick,eventDrag,eventRealese;
        Boolean resultIsMoving;

        Rectangle rectangle,resultRetangle,expectedResizedRectangle;
        Point2D rectangleMouseClickPoint,firstPoint,localPoint;
        Point2D gizmoDraggedPoint,gizmoReleasedPoint;
        //To perform this test, first of all a shape must be drawn.
        //Drawing a rectangle ...
        firstPoint = new Point2D(1.0, 1.0);
        rectangle = new Rectangle(firstPoint.getX(),firstPoint.getY(),4.0,5.0);
        rectangle.setFill(Color.ANTIQUEWHITE);
        rectangle.setStroke(Color.AQUA);
        rectangleMouseClickPoint = new Point2D(2.0, 2.0);
        drawing.addShape(rectangle);
        
        //Clicking on the rectangle, so it is selected.
        localPoint = rectangleMouseClickPoint.subtract(firstPoint.getX(), firstPoint.getY());        
        eventClick = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, rectangleMouseClickPoint, rectangle, localPoint);
        cursorTool.handlePressed(eventClick);
        assertEquals(drawEditor.selectedShapeProperty().get(), rectangle);
        
        //Moving the gizmo
        ResizeGizmo resizeGizmo = rectangle.getResizeGizmo();
        Point2D gizmoMouseClickPoint = new Point2D(resizeGizmo.centerX.get(), resizeGizmo.centerY.get());
        //Clicking on the Gizmo
        localPoint = new Point2D(resizeGizmo.centerX.get() - resizeGizmo.boundsProperty().get().getMinX(), resizeGizmo.centerY.get() - resizeGizmo.boundsProperty().get().getMinY());  
        eventClick = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, gizmoMouseClickPoint, resizeGizmo, localPoint);
        cursorTool.handlePressed(eventClick);

        gizmoDraggedPoint = new Point2D(600.0, 600.0);
        eventDrag = drawing.new Event(EventType.MOUSE_DRAGGED,MouseButton.PRIMARY, gizmoDraggedPoint, resizeGizmo, localPoint);
        cursorTool.handleDragged(eventDrag);
        
        //Simulating the releasing of the gizmo after a movement
        gizmoReleasedPoint = gizmoDraggedPoint;
        expectedResizedRectangle = new Rectangle(firstPoint.getX(), firstPoint.getY(), gizmoReleasedPoint.getX() - firstPoint.getX(), gizmoReleasedPoint.getY() - firstPoint.getX());
        
        //Launching Releasing event
        eventRealese = drawing.new Event(EventType.MOUSE_RELEASED, MouseButton.PRIMARY, gizmoDraggedPoint, resizeGizmo, localPoint);
        cursorTool.handleReleased(eventRealese);
        
        //Check if isMoving is set correctly after the release event
        resultIsMoving = (Boolean) isMovingField.get(cursorTool);
        assertFalse(resultIsMoving);

        //Check if the rectangle has been resized properly
        resultRetangle = (Rectangle) drawing.getShape(0);

        assertRectangleAlmostEqual(expectedResizedRectangle, resultRetangle);
    }

}
