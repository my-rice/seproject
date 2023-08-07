package it.faint.model;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.function.DoublePredicate;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.faint.model.Drawing.EventType;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.Clipboard;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;


public class DrawEditorTest {
    private Field drawingField,selectedToolField,selectedShapeField,commandInvokerField, isGridVisibleField, gridSizeField;
    private DrawEditor drawEditor;
    private Drawing drawing;
    private static Clipboard clipboard;
    private String serializedShape;
    private Shape cutCopyPasteshape;
    @BeforeEach
    public void setUpEach() throws NoSuchFieldException, SecurityException, InterruptedException{
        drawing = new Drawing();
        drawEditor = new DrawEditor(drawing);

        selectedToolField = DrawEditor.class.getDeclaredField("selectedTool");
        selectedToolField.setAccessible(true);
        
        selectedShapeField = DrawEditor.class.getDeclaredField("selectedShape");
        selectedShapeField.setAccessible(true);
        
        commandInvokerField = DrawEditor.class.getDeclaredField("commandInvoker");
        commandInvokerField.setAccessible(true);
        
        drawingField = DrawEditor.class.getDeclaredField("drawing");
        drawingField.setAccessible(true);
        
        isGridVisibleField = Drawing.class.getDeclaredField("isGridVisible");
        isGridVisibleField.setAccessible(true);

        gridSizeField = Drawing.class.getDeclaredField("gridSize");
        gridSizeField.setAccessible(true);
    }
    @BeforeAll
    public static void setUpAll() throws InterruptedException{
        initFX();
        Platform.runLater(()->{
            clipboard = Clipboard.getSystemClipboard();
        });
        waitForRunLater();
    }

    private synchronized static void initFX(){
        try{
        Platform.startup(()->{});
        } catch (IllegalStateException e){
            
        }
    }

    private static void waitForRunLater() throws InterruptedException {
        Semaphore semaphore = new Semaphore(0);
        Platform.runLater(() -> semaphore.release());
        semaphore.acquire();
    }

    @Test
    void testGetSelectedBorderColor() {
        LinkedList<Color> list = new LinkedList<>();

        list.add(new Color(0.46,0.23,0.13,0.8)); 
        list.add(Color.DARKCYAN);
        list.add(Color.DARKCYAN);
        list.add(Color.BISQUE);
        list.add(Color.SEASHELL);
        list.add(Color.GAINSBORO);
        list.add(Color.CORNFLOWERBLUE);
        list.add(Color.PAPAYAWHIP);
        list.add(Color.TURQUOISE);

        for(int i = 0;i<list.size();i++){
            drawEditor.setSelectedBorderColor(list.get(i));
            assertEquals(list.get(i),drawEditor.getSelectedBorderColor());
        }
    }

    @Test
    void testGetSelectedFillColor() {
        LinkedList<Color> list = new LinkedList<>();

        list.add(Color.AQUA);
        list.add(Color.AQUAMARINE);
        list.add(Color.BISQUE);
        list.add(Color.BROWN);
        list.add(Color.KHAKI);
        list.add(Color.SEASHELL);
        list.add(Color.GAINSBORO);
        list.add(Color.CORNFLOWERBLUE);
        list.add(Color.PAPAYAWHIP);
        list.add(Color.TURQUOISE);
        list.add(new Color(0.396,0.0,0.043,1.0)); //Palissandro

        for(int i = 0;i<list.size();i++){
            drawEditor.setSelectedFillColor(list.get(i));
            assertEquals(list.get(i),drawEditor.getSelectedFillColor());
        }
    }

    @Test
    void testGetSelectedTool() {
        LinkedList<Tool> l = new LinkedList<>();
        l.add(new CursorTool(drawEditor));
        l.add(new EllipseTool(drawEditor));
        l.add(new RectangleTool(drawEditor));
        l.add(new LineTool(drawEditor));

        for(int i = 0;i<l.size();i++){
            drawEditor.setTool(l.get(i));
            assertEquals(l.get(i),drawEditor.getSelectedTool());
        }
    }

    @Test
    void testGetAllShapes() {
        List<Shape> expectedShapeList = new LinkedList<>();
        expectedShapeList.add(new Rectangle(1.0,5.0,8.0,6.0));
        expectedShapeList.add(new Rectangle(100.0,400.0,90.0,115.0));
        expectedShapeList.add(new Ellipse(3.0,7.0,5.0,3.0));
        expectedShapeList.add(new Ellipse(130.0,170.0,50.0,15.0));
        expectedShapeList.add(new Line(0.0,0.0,12.0,12.0));
        expectedShapeList.add(new Line(200.0,370.0,500.0,500.0));

        for(int i=0;i<expectedShapeList.size();i++){
            drawing.addShape(expectedShapeList.get(i));
        }
        List<Shape> resultList = drawEditor.getAllShapes();

        assertEquals(expectedShapeList.size(), resultList.size());

        for(int i = 0;i<resultList.size();i++){
            assertEquals(expectedShapeList.get(i),resultList.get(i));
        }

    }

    @Test
    void testGetCommandInvoker() throws IllegalArgumentException, IllegalAccessException {
        CommandInvoker expected = new CommandInvoker();
        commandInvokerField.set(drawEditor, expected);

        CommandInvoker result = drawEditor.getCommandInvoker();
        assertEquals(expected, result);
    }

    @Test
    void testGetDrawing() throws IllegalArgumentException, IllegalAccessException {
        Drawing expected = new Drawing();
        drawingField.set(drawEditor, expected);

        Drawing result = drawEditor.getDrawing();
        assertEquals(expected,result);
    }

    @Test
    void testSetSelectedBorderColor() {
        LinkedList<Color> list = new LinkedList<>();

        list.add(new Color(0.46,0.23,0.13,0.8)); 
        list.add(Color.DARKCYAN);
        list.add(Color.DARKCYAN);
        list.add(Color.BISQUE);
        list.add(Color.SEASHELL);
        list.add(Color.GAINSBORO);
        list.add(Color.CORNFLOWERBLUE);
        list.add(Color.PAPAYAWHIP);
        list.add(Color.TURQUOISE);

        for(int i = 0;i<list.size();i++){
            drawEditor.setSelectedBorderColor(list.get(i));
            assertEquals(list.get(i),drawEditor.getSelectedBorderColor());
        }
    }

    @Test
    void testSetSelectedFillColor() {
        LinkedList<Color> list = new LinkedList<>();

        list.add(Color.AQUA);
        list.add(Color.AQUAMARINE);
        list.add(Color.BISQUE);
        list.add(Color.BROWN);
        list.add(Color.KHAKI);
        list.add(Color.SEASHELL);
        list.add(Color.GAINSBORO);
        list.add(Color.CORNFLOWERBLUE);
        list.add(Color.PAPAYAWHIP);
        list.add(Color.TURQUOISE);
        list.add(new Color(0.396,0.0,0.043,1.0)); //Palissandro

        for(int i = 0;i<list.size();i++){
            drawEditor.setSelectedFillColor(list.get(i));
            assertEquals(list.get(i),drawEditor.getSelectedFillColor());
        }
    }

    @Test
    void testSetTool() {
        LinkedList<Tool> expectedToolList = new LinkedList<>();
        expectedToolList.add(new CursorTool(drawEditor));
        expectedToolList.add(new EllipseTool(drawEditor));
        expectedToolList.add(new RectangleTool(drawEditor));
        expectedToolList.add(new LineTool(drawEditor));

        for(int i = 0;i<expectedToolList.size();i++){
            drawEditor.setTool(expectedToolList.get(i));
            assertEquals(expectedToolList.get(i),drawEditor.getSelectedTool());
        }
    }

    @Test
    void testSelectedBorderColorProperty() {
        List<Color> expectedColorList = new LinkedList<>();

        expectedColorList.add(Color.SKYBLUE);
        expectedColorList.add(Color.SEAGREEN);
        expectedColorList.add(Color.BISQUE);
        expectedColorList.add(Color.GREY);
        expectedColorList.add(Color.KHAKI);
        expectedColorList.add(Color.SEASHELL);
        expectedColorList.add(Color.YELLOWGREEN);
        expectedColorList.add(Color.LIGHTGOLDENRODYELLOW);
        expectedColorList.add(Color.hsb(0.5, 0.6, 0.7));
        expectedColorList.add(Color.SILVER);
        expectedColorList.add(new Color(0.15,0.7,0.92,1.0));

        for(int i = 0;i<expectedColorList.size();i++){
            drawEditor.selectedBorderColorProperty().set(expectedColorList.get(i));
            assertEquals(expectedColorList.get(i),drawEditor.selectedBorderColorProperty().get());
        }
    }

    @Test
    void testSelectedFillColorProperty() {
        List<Color> expectedColorList = new LinkedList<>();

        expectedColorList.add(Color.DARKGOLDENROD);
        expectedColorList.add(Color.DARKKHAKI);
        expectedColorList.add(Color.BISQUE);
        expectedColorList.add(Color.PAPAYAWHIP);
        expectedColorList.add(Color.KHAKI);
        expectedColorList.add(Color.SEASHELL);
        expectedColorList.add(Color.MINTCREAM);
        expectedColorList.add(Color.SANDYBROWN);
        expectedColorList.add(Color.STEELBLUE);
        expectedColorList.add(Color.SILVER);
        expectedColorList.add(new Color(0.55,0.14,0.22,1.0));

        for(int i = 0;i<expectedColorList.size();i++){
            drawEditor.selectedFillColorProperty().set(expectedColorList.get(i));
            assertEquals(expectedColorList.get(i),drawEditor.selectedFillColorProperty().get());
        }

    }

    @Test
    void testSelectedShapeProperty() {
        List<Shape> expectedShapeList = new LinkedList<>();
        expectedShapeList.add(new Rectangle(3.0,3.0,9.0,4.0));
        expectedShapeList.add(new Rectangle(120.0,200.0,50.0,100.0));
        expectedShapeList.add(new Ellipse(5.0,8.0,9.0,9.0));
        expectedShapeList.add(new Ellipse(100.0,19.0,12.0,17.0));
        expectedShapeList.add(new Line(10.0,10.0,20.0,20.0));
        expectedShapeList.add(new Line(100.0,470.0,300.0,700.0));
        
        for(int i = 0;i<expectedShapeList.size();i++){
            drawEditor.selectedShapeProperty().set(expectedShapeList.get(i));
            assertEquals(expectedShapeList.get(i), drawEditor.selectedShapeProperty().get());
        }
    }

    @Test
    void testSetSelectedShape() {
        List<Shape> expectedShapeList = new LinkedList<>();
        expectedShapeList.add(new Rectangle(31.0,31.0,19.0,14.0));
        expectedShapeList.add(new Rectangle(50.0,150.0,30.0,10.0));
        expectedShapeList.add(new Ellipse(35.0,28.0,19.0,9.0));
        expectedShapeList.add(new Ellipse(100.0,19.0,32.0,17.0));
        expectedShapeList.add(new Line(40.0,10.0,70.0,80.0));
        expectedShapeList.add(new Line(500.0,770.0,200.0,100.0));
        
        for(int i = 0;i<expectedShapeList.size();i++){
            drawEditor.setSelectedShape(expectedShapeList.get(i));
            assertEquals(expectedShapeList.get(i), drawEditor.selectedShapeProperty().get());
        }
    }

    @Test
    void testChangeSelectedShapeBorderColorRequest() {   
        Color borderColorExpected = Color.BLACK;
        Color notEqualTest = Color.WHITE;
        Color resultColor;

        Rectangle rectangle = new Rectangle(8.0, 9.0, 10.0, 11.0);
        rectangle.setStroke(borderColorExpected);
        drawEditor.setSelectedShape(rectangle);
        drawEditor.changeSelectedShapeBorderColorRequest(rectangle.strokeProperty().get(),borderColorExpected);
        resultColor = drawEditor.selectedShapeProperty().get().strokeProperty().get();
        assertEquals(borderColorExpected,resultColor);
        assertNotEquals(notEqualTest, resultColor);
        
        Line line = new Line(11.0, 19.0, 30.0, 15.0);
        rectangle.setStroke(borderColorExpected);
        drawEditor.setSelectedShape(line);
        drawEditor.changeSelectedShapeBorderColorRequest(line.strokeProperty().get(),borderColorExpected);
        resultColor = drawEditor.selectedShapeProperty().get().strokeProperty().get();
        assertEquals(borderColorExpected,resultColor);
        assertNotEquals(notEqualTest, resultColor);
        
        Ellipse ellipse = new Ellipse(58.0, 91.0, 110.0, 1.0);
        rectangle.setStroke(borderColorExpected);
        drawEditor.setSelectedShape(ellipse);
        drawEditor.changeSelectedShapeBorderColorRequest(ellipse.strokeProperty().get(),borderColorExpected);
        resultColor = drawEditor.selectedShapeProperty().get().strokeProperty().get();
        assertEquals(borderColorExpected,resultColor);
        assertNotEquals(notEqualTest, resultColor);
        
    }

    @Test
    void testChangeSelectedShapeFillColorRequest() {
        Color fillColorExpected = Color.BLACK;
        Color notEqualTest = Color.WHITE;
        Color resultColor;

        Rectangle rectangle = new Rectangle(8.0, 9.0, 10.0, 11.0);
        rectangle.setStroke(fillColorExpected);
        drawEditor.setSelectedShape(rectangle);
        drawEditor.changeSelectedShapeBorderColorRequest(rectangle.strokeProperty().get(),fillColorExpected);
        resultColor = drawEditor.selectedShapeProperty().get().strokeProperty().get();
        assertEquals(fillColorExpected,resultColor);
        assertNotEquals(notEqualTest, resultColor);
        
        Line line = new Line(11.0, 19.0, 30.0, 15.0);
        rectangle.setStroke(fillColorExpected);
        drawEditor.setSelectedShape(line);
        drawEditor.changeSelectedShapeBorderColorRequest(line.strokeProperty().get(),fillColorExpected);
        resultColor = drawEditor.selectedShapeProperty().get().strokeProperty().get();
        assertEquals(fillColorExpected,resultColor);
        assertNotEquals(notEqualTest, resultColor);
        
        Ellipse ellipse = new Ellipse(58.0, 91.0, 110.0, 1.0);
        rectangle.setStroke(fillColorExpected);
        drawEditor.setSelectedShape(ellipse);
        drawEditor.changeSelectedShapeBorderColorRequest(ellipse.strokeProperty().get(),fillColorExpected);
        resultColor = drawEditor.selectedShapeProperty().get().strokeProperty().get();
        assertEquals(fillColorExpected,resultColor);
        assertNotEquals(notEqualTest, resultColor);
        
    }

    @Test
    void testDeleteSelectedShapeRequest() {
        List<Shape> shapeList = new LinkedList<>();
        shapeList.add(new Rectangle(54.0,98.0,50.0,0.0));
        shapeList.add(new Rectangle(5.0,0.0,10.0,1.0));
        shapeList.add(new Ellipse(41.0,71.0,5.0,322.0));
        shapeList.add(new Ellipse(0.5,2.0,51.0,15.0));
        shapeList.add(new Line(0.0,5.0,5.0,2.0));
        shapeList.add(new Line(40.0,210.0,370.0,780.0));

        for(int i=0;i<shapeList.size();i++){
            drawEditor.insertShapeRequest(shapeList.get(i));
        }

        Shape s;
        List<Shape> resultShapes;
        for(int i=0;i<shapeList.size();i++){
            s = shapeList.remove(i);
            drawEditor.setSelectedShape(s);
            drawEditor.deleteSelectedShapeRequest();
            resultShapes = drawEditor.getAllShapes();
            for(int j=0;j<resultShapes.size();j++){
                assertFalse(resultShapes.get(i).equals(s));
            }
        }
    }

    @Test
    void testInsertShapeRequest() {
        
        List<Shape> expectedShapeList = new LinkedList<>();
        expectedShapeList.add(new Rectangle(4.0,8.0,90.0,60.0));
        expectedShapeList.add(new Rectangle(500.0,90.0,10.0,15.0));
        expectedShapeList.add(new Ellipse(1.0,71.0,59.0,32.0));
        expectedShapeList.add(new Ellipse(30.0,72.0,51.0,15.0));
        expectedShapeList.add(new Line(10.0,0.0,2.0,2.0));
        expectedShapeList.add(new Line(40.0,70.0,500.0,500.0));

        for(int i=0;i<expectedShapeList.size();i++){
            drawEditor.insertShapeRequest(expectedShapeList.get(i));
        }

        Shape s;
        boolean found;
        List<Shape> resultShapes = drawEditor.getAllShapes();
        for(int i=0;i<expectedShapeList.size();i++){
            s = expectedShapeList.get(i);
            found = false;
            for(int j=0;j<expectedShapeList.size();j++){
                if(resultShapes.get(i).equals(s)){
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }

    }

    @Test
    void testMoveSelectedShapeRequest() {

        Point2D oldTopLeftPointRectangle = new Point2D(8.0, 9.0);
        Point2D newTopLeftPointRectangle = new Point2D(700.0, 900.0);
        double expectedRectangleX = newTopLeftPointRectangle.getX();
        double expectedRectangleY = newTopLeftPointRectangle.getY();
        double resultRectangleX,resultRectangleY;
        Rectangle rectangle = new Rectangle(oldTopLeftPointRectangle.getX(),oldTopLeftPointRectangle.getY(), 10.0, 11.0);
        drawEditor.setSelectedShape(rectangle);
        drawEditor.moveSelectedShapeRequest(oldTopLeftPointRectangle,newTopLeftPointRectangle);
        resultRectangleX = rectangle.x.get();
        resultRectangleY = rectangle.y.get();
        assertEquals(expectedRectangleX, resultRectangleX);
        assertEquals(expectedRectangleY, resultRectangleY);

        Point2D oldTopLeftPointLine = new Point2D(8.0, 9.0);
        Point2D oldEndPointLine = new Point2D(18.0, 19.0);
        Point2D newTopLeftPointLine = new Point2D(800.0, 900.0);
        Point2D newEndPointLine = new Point2D(810.0, 910.0);
        double expectedLineStartX = newTopLeftPointLine.getX();
        double expectedLineStartY = newTopLeftPointLine.getY();
        double expectedLineEndX = newEndPointLine.getX();
        double expectedLineEndY = newEndPointLine.getY();
        double resultLineStartX,resultLineStartY,resultLineEndX,resultLineEndY;
        Line line = new Line(oldTopLeftPointRectangle.getX(),oldTopLeftPointRectangle.getY(), oldEndPointLine.getX(),oldEndPointLine.getY());
        drawEditor.setSelectedShape(line);
        drawEditor.moveSelectedShapeRequest(oldTopLeftPointLine,newTopLeftPointLine);
        resultLineStartX = line.startX.get();
        resultLineStartY = line.startY.get();
        resultLineEndX = line.endX.get();
        resultLineEndY = line.endY.get();
        assertEquals(expectedLineStartX, resultLineStartX);
        assertEquals(expectedLineStartY, resultLineStartY);
        assertEquals(expectedLineEndX, resultLineEndX);
        assertEquals(expectedLineEndY, resultLineEndY);
        
        
        Point2D oldCenterEllipse = new Point2D(30.0, 21.0);
        Point2D newCenterEllipse = new Point2D(910.0, 880.0);
        Point2D ellipseRadius = new Point2D(5.0, 5.0);

        Point2D oldTopLeftPointEllipse = new Point2D(oldCenterEllipse.getX() - ellipseRadius.getX(), oldCenterEllipse.getY() - ellipseRadius.getY());
        Point2D newTopLeftPointEllipse = new Point2D(newCenterEllipse.getX() - ellipseRadius.getX(), newCenterEllipse.getY() - ellipseRadius.getY());

        double expectedEllipseCenterX = newCenterEllipse.getX();
        double expectedEllipseCenterY = newCenterEllipse.getY();
        double expectedEllipseRadiusX = ellipseRadius.getX();
        double expectedEllipseRadiusY = ellipseRadius.getY();
        double resultEllipseCenterX,resultEllipseCenterY,resultEllipseRadiusX,resultEllipseRadiusY;
        Ellipse ellipse = new Ellipse(oldCenterEllipse.getX(),oldCenterEllipse.getY(), ellipseRadius.getX(),ellipseRadius.getY());
        drawEditor.setSelectedShape(ellipse);
        drawEditor.moveSelectedShapeRequest(oldTopLeftPointEllipse,newTopLeftPointEllipse);
        resultEllipseCenterX = ellipse.centerX.get();
        resultEllipseCenterY = ellipse.centerY.get();
        resultEllipseRadiusX = ellipse.radiusX.get();
        resultEllipseRadiusY = ellipse.radiusY.get();
        assertEquals(expectedEllipseCenterX, resultEllipseCenterX);
        assertEquals(expectedEllipseCenterY, resultEllipseCenterY);
        assertEquals(expectedEllipseRadiusX, resultEllipseRadiusX);
        assertEquals(expectedEllipseRadiusY, resultEllipseRadiusY);

  

    }

    @Test
    void testClearAllShapes() {
        int expectedShapeListSize = 0;
        List<Shape> ShapeList = new LinkedList<>();
        ShapeList.add(new Rectangle(4.0,8.0,90.0,60.0));
        ShapeList.add(new Rectangle(500.0,90.0,10.0,15.0));
        ShapeList.add(new Ellipse(1.0,71.0,59.0,32.0));
        ShapeList.add(new Ellipse(30.0,72.0,51.0,15.0));
        ShapeList.add(new Line(10.0,0.0,2.0,2.0));
        ShapeList.add(new Line(40.0,70.0,500.0,500.0));

        for(int i=0;i<ShapeList.size();i++){
            drawing.addShape(ShapeList.get(i));
        }

        assertNotEquals(expectedShapeListSize, drawEditor.getAllShapes().size());
        drawEditor.clearAllShapes();
        assertEquals(expectedShapeListSize, drawEditor.getAllShapes().size());
        
    }

    @Test
    void testClearSelectedShape() throws IllegalArgumentException, IllegalAccessException {
        Rectangle rectangle = new Rectangle(1.0, 1.0, 5.0, 6.0);
        drawEditor.setSelectedShape(rectangle);
        assertNotNull(drawEditor.selectedShapeProperty().get());
        drawEditor.clearSelectedShape();
        assertNull(drawEditor.selectedShapeProperty().get());

        Line line = new Line(5.0, 11.0, 15.0, 9.0);
        drawEditor.setSelectedShape(line);
        assertNotNull(drawEditor.selectedShapeProperty().get());
        drawEditor.clearSelectedShape();
        assertNull(drawEditor.selectedShapeProperty().get());

        Ellipse ellipse = new Ellipse(43.0, 20.0, 2.0, 1.0);
        drawEditor.setSelectedShape(ellipse);
        assertNotNull(drawEditor.selectedShapeProperty().get());
        drawEditor.clearSelectedShape();
        assertNull(drawEditor.selectedShapeProperty().get());

    }

    @Test
    void testInsertAllShapes() {
        int expectedEmpty = 0;
        List<Shape> expectedShapeList = new LinkedList<>();
        expectedShapeList.add(new Rectangle(4.0,8.0,90.0,60.0));
        expectedShapeList.add(new Rectangle(500.0,90.0,10.0,15.0));
        expectedShapeList.add(new Ellipse(1.0,71.0,59.0,32.0));
        expectedShapeList.add(new Ellipse(30.0,72.0,51.0,15.0));
        expectedShapeList.add(new Line(10.0,0.0,2.0,2.0));
        expectedShapeList.add(new Line(40.0,70.0,500.0,500.0));

        assertEquals(expectedEmpty, drawEditor.getAllShapes().size());
        drawEditor.insertAllShapes(expectedShapeList);

        Shape s;
        boolean found;
        List<Shape> resultShapes = drawEditor.getAllShapes();

        for(int i=0;i<expectedShapeList.size();i++){
            s = expectedShapeList.get(i);
            found = false;
            for(int j=0;j<expectedShapeList.size();j++){
                if(resultShapes.get(i).equals(s)){
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
        
    }
    @Test
    void testCopySelectedShapeRequest() throws ClassNotFoundException, IOException, InterruptedException{
        List<Shape> expectedShapeList = new LinkedList<>();

        expectedShapeList.add(new Rectangle(4.0,8.0,90.0,60.0));
        expectedShapeList.add(new Rectangle(500.0,90.0,10.0,15.0));
        expectedShapeList.add(new Ellipse(1.0,71.0,59.0,32.0));
        expectedShapeList.add(new Ellipse(30.0,72.0,51.0,15.0));
        expectedShapeList.add(new Line(10.0,0.0,2.0,2.0));
        expectedShapeList.add(new Line(40.0,70.0,500.0,500.0));

        for(Shape s:expectedShapeList){
            drawEditor.setSelectedShape(s);

            Platform.runLater(()->{
                drawEditor.copySelectedShapeRequest();

                serializedShape = clipboard.getString();
                try {
                    cutCopyPasteshape= (Shape) ClipboardManager.objectFromString(serializedShape);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            waitForRunLater();

            assertEquals(s, cutCopyPasteshape);
        }
    }

    @Test
    void testCutSelectedShapeRequest() throws ClassNotFoundException, IOException, InterruptedException{
        List<Shape> expectedShapeList = new LinkedList<>();

        expectedShapeList.add(new Rectangle(4.0,8.0,90.0,60.0));
        expectedShapeList.add(new Rectangle(500.0,90.0,10.0,15.0));
        expectedShapeList.add(new Ellipse(1.0,71.0,59.0,32.0));
        expectedShapeList.add(new Ellipse(30.0,72.0,51.0,15.0));
        expectedShapeList.add(new Line(10.0,0.0,2.0,2.0));
        expectedShapeList.add(new Line(40.0,70.0,500.0,500.0));

        for(Shape s:expectedShapeList){

            Platform.runLater(()->{
                drawEditor.insertShapeRequest(s);
                drawEditor.setSelectedShape(s);
                drawEditor.cutSelectedShapeRequest();

                serializedShape = clipboard.getString();
                try {
                    cutCopyPasteshape= (Shape) ClipboardManager.objectFromString(serializedShape);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            waitForRunLater();
            
            assertEquals(s, cutCopyPasteshape);

            Rectangle2D bounding= cutCopyPasteshape.boundsProperty().get();
            Point2D offset = new Point2D(bounding.getMinX() + 50, bounding.getMinY() + 50);
            cutCopyPasteshape.moveShape(offset);

            assertEquals(-1, drawing.removeShape(s));
        }


    }

    @Test
    void testPasteSelectedShapeRequest() throws ClassNotFoundException, IOException, IllegalArgumentException, IllegalAccessException, InterruptedException, NoSuchFieldException, SecurityException{
        
        List<Shape> expectedShapeList = new LinkedList<>();

        expectedShapeList.add(new Rectangle(4.0,8.0,90.0,60.0));
        expectedShapeList.add(new Rectangle(500.0,90.0,10.0,15.0));
        expectedShapeList.add(new Ellipse(1.0,71.0,59.0,32.0));
        expectedShapeList.add(new Ellipse(30.0,72.0,51.0,15.0));
        expectedShapeList.add(new Line(10.0,0.0,2.0,2.0));
        expectedShapeList.add(new Line(40.0,70.0,500.0,500.0));

        for(Shape s:expectedShapeList){
            drawEditor.setSelectedShape(s);

            Platform.runLater(()->{
                drawEditor.copySelectedShapeRequest();

                serializedShape = clipboard.getString();
                try {
                    cutCopyPasteshape= (Shape) ClipboardManager.objectFromString(serializedShape);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            waitForRunLater();

            assertEquals(s, cutCopyPasteshape);

            Platform.runLater(()->{
                drawEditor.pasteSelecteShapeRequest();
                serializedShape = clipboard.getString();
                try {
                    cutCopyPasteshape= (Shape) ClipboardManager.objectFromString(serializedShape);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            waitForRunLater();
            
            Rectangle2D bounding= cutCopyPasteshape.boundsProperty().get();
            Point2D offset = new Point2D(bounding.getMinX() + 50, bounding.getMinY() + 50);
            cutCopyPasteshape.moveShape(offset);

            assertNotEquals(-1, drawing.removeShape(cutCopyPasteshape));
        }

    }

    @Test
    void testHandleDrawingEventPressed() {
        Rectangle expectedRectangle = new Rectangle(0.0, 0.0, 10.0 , 10.0);
        Ellipse expectedEllipse;
        Line expectedLine = new Line(100.0, 100.0, 250.0, 250.0);
        
        //Testing the rectangle tool
        drawEditor.setTool(new RectangleTool(drawEditor));
        double x1 = 0.0,y1 = 0.0,x2 = 10.0,y2 = 10.0; 
        MouseEvent eventPressedFirstTime = new MouseEvent(null, x1,y1, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);
        MouseEvent eventPressedSecondTime = new MouseEvent(null, x2,y2, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);

        Drawing.Event eventPressed1 = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(eventPressedFirstTime.getX(),eventPressedFirstTime.getY()),null,null);
        drawEditor.handleDrawingEvent(eventPressed1);
        
        Drawing.Event eventPressed2 = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(eventPressedSecondTime.getX(),eventPressedSecondTime.getY()),null,null);
        drawEditor.handleDrawingEvent(eventPressed2);
        
        assertTrue(drawing.getAllShapes().get(0).equals(expectedRectangle));
        drawing.clearShapes();

        //Testing the ellipse tool
        drawEditor.setTool(new EllipseTool(drawEditor));
        x1 = 10.0;
        y1 = 10.0;
        x2 = 20.0;
        y2 = 20.0;
        double radiusX = (x2-x1)/2,radiusY = (y2-y1)/2,centerX = (x1+x2)/2,centerY = (y1+y2)/2;
        expectedEllipse = new Ellipse(centerX, centerY, radiusX , radiusY);
        eventPressedFirstTime = new MouseEvent(null, x1,y1, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);
        eventPressedSecondTime = new MouseEvent(null, x2,y2, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);

        eventPressed1 = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(eventPressedFirstTime.getX(),eventPressedFirstTime.getY()),null,null);
        drawEditor.handleDrawingEvent(eventPressed1);
        
        eventPressed2 = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(eventPressedSecondTime.getX(),eventPressedSecondTime.getY()),null,null);
        drawEditor.handleDrawingEvent(eventPressed2);
        
        assertTrue(drawing.getAllShapes().get(0).equals(expectedEllipse));
        drawing.clearShapes();

        //Testing the line tool
        drawEditor.setTool(new LineTool(drawEditor));
        x1 = 100.0;
        y1 = 100.0;
        x2 = 250.0;
        y2 = 250.0;
        eventPressedFirstTime = new MouseEvent(null, x1,y1, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);
        eventPressedSecondTime = new MouseEvent(null, x2,y2, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);

        eventPressed1 = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(eventPressedFirstTime.getX(),eventPressedFirstTime.getY()),null,null);
        drawEditor.handleDrawingEvent(eventPressed1);
        
        eventPressed2 = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(eventPressedSecondTime.getX(),eventPressedSecondTime.getY()),null,null);
        drawEditor.handleDrawingEvent(eventPressed2);
        
        assertTrue(drawing.getAllShapes().get(0).equals(expectedLine));
        drawing.clearShapes();
    }

    @Test
    void testHandleDrawingEventMoved() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        double minX, minY, maxX, maxY;
        RectangleTool rectangleTool = new RectangleTool(drawEditor);
        EllipseTool ellipseTool = new EllipseTool(drawEditor);
        LineTool lineTool = new LineTool(drawEditor);

        Shape resultPreviewShape,expectedPreviewShape;
        Rectangle2D resultBoundingBox,expectedBoundingBox;

        Field boundingRectangleField = ShapeInsertTool.class.getDeclaredField("boundingRectangle");
        boundingRectangleField.setAccessible(true);
        
        MouseEvent eventPressedFirstTime, eventMouseMoved;
        Drawing.Event eventPressed,eventMoved;
        //Testing the moving event with the rectangle tool
        drawEditor.setTool(rectangleTool);
        double x1 = 0.0,y1 = 0.0,x2 = 10.0,y2 = 10.0; 
        eventPressedFirstTime = new MouseEvent(null, x1,y1, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);
        eventMouseMoved = new MouseEvent(null, x2,y2, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);

        eventPressed = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(eventPressedFirstTime.getX(),eventPressedFirstTime.getY()),null,null);
        drawEditor.handleDrawingEvent(eventPressed);
        eventMoved = drawing.new Event(EventType.MOUSE_MOVED,MouseButton.NONE,new Point2D(eventMouseMoved.getX(),eventMouseMoved.getY()),null,null);
        drawEditor.handleDrawingEvent(eventMoved);
        
        minX = Double.min(x1,x2);
        minY = Double.min(y1,y2);
        maxX = Double.max(x1,x2);
        maxY = Double.max(y1,y2);
        
        expectedPreviewShape = new Rectangle(minX,minY,maxX-minX,maxY-minY);
        expectedPreviewShape.setOpacity(0.75);
        expectedPreviewShape.setFill(drawEditor.getSelectedFillColor());
        expectedPreviewShape.setStroke(drawEditor.getSelectedBorderColor());
        expectedBoundingBox = new Rectangle2D(minX,minY,maxX-minX,maxY-minY);
        resultPreviewShape = this.drawEditor.getDrawing().getAllPreviews().get(0);
        resultBoundingBox = (Rectangle2D) boundingRectangleField.get((rectangleTool));

        assertTrue(expectedBoundingBox.equals(resultBoundingBox));
        assertTrue(expectedPreviewShape.equals(resultPreviewShape));

        //Testing the moving event with the ellipse tool
        drawEditor.setTool(ellipseTool);
        x1 = 100.0;y1 = 100.0;x2 = 500.0;y2 = 500.0; 
        eventPressedFirstTime = new MouseEvent(null, x1,y1, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);
        eventMouseMoved = new MouseEvent(null, x2,y2, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);

        eventPressed = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(eventPressedFirstTime.getX(),eventPressedFirstTime.getY()),null,null);
        drawEditor.handleDrawingEvent(eventPressed);
        eventMoved = drawing.new Event(EventType.MOUSE_MOVED,MouseButton.NONE,new Point2D(eventMouseMoved.getX(),eventMouseMoved.getY()),null,null);
        drawEditor.handleDrawingEvent(eventMoved);
        
        minX = Double.min(x1,x2);
        minY = Double.min(y1,y2);
        maxX = Double.max(x1,x2);
        maxY = Double.max(y1,y2);
        Rectangle2D boundingRectangle = new Rectangle2D(minX,minY,maxX-minX,maxY-minY);
        expectedPreviewShape = new Ellipse(((boundingRectangle.getMaxX() - boundingRectangle.getMinX() )/2)+ boundingRectangle.getMinX() ,((boundingRectangle.getMaxY() - boundingRectangle.getMinY() )/2)+ boundingRectangle.getMinY() , (boundingRectangle.getMaxX() - boundingRectangle.getMinX() )/2 , (boundingRectangle.getMaxY() - boundingRectangle.getMinY() )/2);
        expectedPreviewShape.setOpacity(0.75);
        expectedPreviewShape.setFill(drawEditor.getSelectedFillColor());
        expectedPreviewShape.setStroke(drawEditor.getSelectedBorderColor());
        expectedBoundingBox = boundingRectangle;
        resultPreviewShape = this.drawEditor.getDrawing().getAllPreviews().get(0);
        resultBoundingBox = (Rectangle2D) boundingRectangleField.get((ellipseTool));

        assertTrue(expectedBoundingBox.equals(resultBoundingBox));
        assertTrue(expectedPreviewShape.equals(resultPreviewShape));

        //Testing the moving event with the line tool
        drawEditor.setTool(lineTool);
        x1 = 700.0;y1 = 700.0;x2 = 1500.0;y2 = 1500.0; 
        eventPressedFirstTime = new MouseEvent(null, x1,y1, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);
        eventMouseMoved = new MouseEvent(null, x2,y2, 0, 0, MouseButton.PRIMARY, 0, false, false, false, false, false, false, false, false, false, false, false, false, null);

        eventPressed = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(eventPressedFirstTime.getX(),eventPressedFirstTime.getY()),null,null);
        drawEditor.handleDrawingEvent(eventPressed);
        eventMoved = drawing.new Event(EventType.MOUSE_MOVED,MouseButton.NONE,new Point2D(eventMouseMoved.getX(),eventMouseMoved.getY()),null,null);
        drawEditor.handleDrawingEvent(eventMoved);
        
        minX = Double.min(x1,x2);
        minY = Double.min(y1,y2);
        maxX = Double.max(x1,x2);
        maxY = Double.max(y1,y2);
        expectedPreviewShape = new Line(x1,y1,x2,y2); 
        expectedPreviewShape.setOpacity(0.75);
        expectedPreviewShape.setFill(drawEditor.getSelectedFillColor());
        expectedPreviewShape.setStroke(drawEditor.getSelectedBorderColor());
        expectedBoundingBox = new Rectangle2D(minX,minY,maxX-minX,maxY-minY);
        resultPreviewShape = this.drawEditor.getDrawing().getAllPreviews().get(0);
        resultBoundingBox = (Rectangle2D) boundingRectangleField.get((lineTool));

        assertTrue(expectedBoundingBox.equals(resultBoundingBox));
        assertTrue(expectedPreviewShape.equals(resultPreviewShape));

    }

    @Test
    void testHandleDrawingEventDragged() {
        Rectangle resultRectangle,expectedRectangle,rectangle = new Rectangle(0.0, 0.0, 10.0 , 10.0);
        Ellipse resultEllipse,expectedEllipse, ellipse = new Ellipse(250.0, 250.0, 5.0, 15.0);
        Line resultLine,expectedLine, line = new Line(0.0, 0.0, 500.0, 500.0);
        
        drawEditor.setTool(new CursorTool(drawEditor));

        //Testing the moving of a rectangle, done with the cursor tool
        double x1 = 50.0,y1 = 50.0;
        drawing.addShape(rectangle);
        drawEditor.setSelectedShape(rectangle);
        Drawing.Event eventPressed = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(x1, y1),rectangle,new Point2D(0, 0));
        drawEditor.handleDrawingEvent(eventPressed);
        Drawing.Event eventDragged = drawing.new Event(EventType.MOUSE_DRAGGED,MouseButton.PRIMARY,new Point2D(x1, y1),rectangle,new Point2D(0, 0));
        drawEditor.handleDrawingEvent(eventDragged);
        resultRectangle = (Rectangle)drawing.getShape(0);
        expectedRectangle = new Rectangle(x1,y1,10.0,10.0);
 
        assertTrue(expectedRectangle.equals(resultRectangle));
        drawing.clearShapes();

        //Testing the moving of an ellipse, done with the cursor tool
        x1 = 400.0;y1 = 400.0;
        drawing.addShape(ellipse);
        drawEditor.setSelectedShape(ellipse);
        eventPressed = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(x1, y1),ellipse,new Point2D(0, 0));
        drawEditor.handleDrawingEvent(eventPressed);
        eventDragged = drawing.new Event(EventType.MOUSE_DRAGGED,MouseButton.PRIMARY,new Point2D(x1, y1),ellipse,new Point2D(0, 0));
        drawEditor.handleDrawingEvent(eventDragged);
        resultEllipse = (Ellipse)drawing.getShape(0);
        expectedEllipse = new Ellipse(400.0+5,400.0+15.0,5.0,15.0);

        assertTrue(expectedEllipse.equals(resultEllipse));
        drawing.clearShapes();

        //Testing the moving of an ellipse, done with the cursor tool
        x1 = 1750.0;y1 = 1750.0;
        drawing.addShape(line);
        drawEditor.setSelectedShape(line);
        eventPressed = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY,new Point2D(x1, y1),line,new Point2D(0, 0));
        drawEditor.handleDrawingEvent(eventPressed);
        eventDragged = drawing.new Event(EventType.MOUSE_DRAGGED,MouseButton.PRIMARY,new Point2D(x1, y1),line,new Point2D(0, 0));
        drawEditor.handleDrawingEvent(eventDragged);
        resultLine = (Line)drawing.getShape(0);
        expectedLine = new Line(x1,y1,2250.0,2250.0);

        assertTrue(expectedLine.equals(resultLine));
        drawing.clearShapes();

    }

    @Test
    void testHandleDrawingEventReleased() {
        //The responsability of this test is to check, given a RELEASE EVENT, if the proper eventHandler is called. 
        //This test do not check all the if branches of the handler because other test cases test that.

        Drawing.Event eventClick,eventDrag,eventRealese;

        Point2D expectedPointAfterMovement,expectedPointAfterRelease;

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

        localPoint = rectangleMouseClickPoint.subtract(firstPoint.getX(), firstPoint.getY());        
        
        //Clicking on the rectangle, so it is selected.
        eventClick = drawing.new Event(EventType.MOUSE_PRESSED,MouseButton.PRIMARY, rectangleMouseClickPoint, rectangle, localPoint);
        drawEditor.handleDrawingEvent(eventClick);

        //Moving the rectangle
        rectangleDraggedPoint = new Point2D(250.0, 250.0);
        expectedPointAfterMovement = rectangleDraggedPoint.subtract(localPoint);

        eventDrag = drawing.new Event(EventType.MOUSE_DRAGGED,MouseButton.PRIMARY, rectangleDraggedPoint, rectangle, null);
        drawEditor.handleDrawingEvent(eventDrag);
        
        //Final test. Checking if the shape moved properly
        assertEquals(expectedPointAfterMovement.getX(),rectangle.x.get());
        assertEquals(expectedPointAfterMovement.getY(),rectangle.y.get());

        //Simulating the releasing of the rectangle after a movement
        rectangleReleasedPoint = rectangleDraggedPoint;
        expectedPointAfterRelease = rectangleReleasedPoint.subtract(localPoint);

        //Launching Releasing event
        eventRealese = drawing.new Event(EventType.MOUSE_RELEASED, MouseButton.PRIMARY, rectangleReleasedPoint, rectangle, null);
        drawEditor.handleDrawingEvent(eventRealese);

        //Checking if the correct handler has been called.
        assertEquals(expectedPointAfterRelease.getX(),rectangle.x.get());
        assertEquals(expectedPointAfterRelease.getY(),rectangle.y.get());
    }

    @Test
    void testHandleDrawingEventShapeRemoved() {
        
        Rectangle rectangle = new Rectangle(0.0, 0.0, 10.0 , 10.0);
        Ellipse ellipse = new Ellipse(24.6, 65.7, 98.3, 34.0);
        Line line = new Line(100.0, 100.0, 250.0, 250.0);
        Point2D mouseClick2 = new Point2D(5.0,5.0),mouseClick1 = new Point2D(24.6, 65.7);
        Shape result;
        drawing.addShape(rectangle);
        drawing.addShape(ellipse);
        drawing.addShape(line);

        //Testing
        drawEditor.setSelectedShape(rectangle);
        result = drawEditor.selectedShapeProperty().get();
        //Clicking on the ellipse but the rectangle is selected
        Drawing.Event event1 = drawing.new Event(EventType.SHAPE_REMOVED,MouseButton.PRIMARY,mouseClick1,ellipse,null);
        assertTrue(rectangle.equals(result));
        assertFalse(ellipse.equals(result));
        drawEditor.handleDrawingEvent(event1);
        result = drawEditor.selectedShapeProperty().get();
        assertNotNull(result);    

        //Clicking on the rectangle and the rectangle is selected
        Drawing.Event event2 = drawing.new Event(EventType.SHAPE_REMOVED,MouseButton.PRIMARY,mouseClick2,rectangle,null);
        drawEditor.handleDrawingEvent(event2);
        result = drawEditor.selectedShapeProperty().get();
        assertNull(result); 
        
    }

    private static boolean almostEqual(Point2D a, Point2D b, double eps){
        return a.distance(b)<eps;
    }

    private static void assertRectangleAlmostEqual(Rectangle a, Rectangle b){
        assertTrue(almostEqual(new Point2D(a.boundsProperty().get().getMinX(), a.boundsProperty().get().getMinY()),new Point2D(b.boundsProperty().get().getMinX(), b.boundsProperty().get().getMinY()),0.000000001));
        assertTrue(almostEqual(new Point2D(a.boundsProperty().get().getMaxX(), a.boundsProperty().get().getMaxY()),new Point2D(b.boundsProperty().get().getMaxX(), b.boundsProperty().get().getMaxY()),0.000000001));
    }

    @Test
    void testResizeSelectedShapeRequest() {
        Rectangle rectangle,expectedResizedRectangle;
        Point2D firstPoint;
        //To perform this test, first of all a shape must be drawn.
        //Drawing a rectangle ...
        firstPoint = new Point2D(1.0, 1.0);
        rectangle = new Rectangle(firstPoint.getX(),firstPoint.getY(),4.0,5.0);
        rectangle.setFill(Color.ANTIQUEWHITE);
        rectangle.setStroke(Color.AQUA);
        drawing.addShape(rectangle);
        
        drawEditor.resizeSelectedShapeRequest(new Point2D(rectangle.boundsProperty().get().getMaxX(),rectangle.boundsProperty().get().getMaxY()), new Point2D(600.0,600.0));
        expectedResizedRectangle = new Rectangle(1.0, 1.0, 599.0, 599.0);
        assertFalse(expectedResizedRectangle.equals(rectangle));

        drawEditor.setSelectedShape(rectangle);
        drawEditor.resizeSelectedShapeRequest(new Point2D(rectangle.boundsProperty().get().getMaxX(),rectangle.boundsProperty().get().getMaxY()), new Point2D(600.0,600.0));
        assertRectangleAlmostEqual(expectedResizedRectangle, rectangle);
    }

    @Test
    void testToBackSelectedShapeRequest() {
        List<Shape> expectedShapes = new ArrayList<>(),resultShapes;
        Line line = new Line(2.2, 3.5, 400.9, 485.1);
        Rectangle rectangle = new Rectangle(0, 0, 150, 150);
        Ellipse ellipse = new Ellipse(25.7, 29.35, 5.47, 3.9);
        
        drawing.addShape(line);
        drawing.addShape(rectangle);
        drawing.addShape(ellipse);

        //Testing that nothing happens
        drawEditor.clearSelectedShape();
        drawEditor.toBackSelectedShapeRequest();
        expectedShapes.clear();
        expectedShapes.add(line);
        expectedShapes.add(rectangle);
        expectedShapes.add(ellipse);
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i), resultShapes.get(i));
        }

        //Testing that the command is executed properly
        drawEditor.setSelectedShape(ellipse);
        drawEditor.toBackSelectedShapeRequest();
        expectedShapes.clear();
        expectedShapes.add(ellipse);
        expectedShapes.add(line);
        expectedShapes.add(rectangle);
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i), resultShapes.get(i));
        }
        assertNull(drawEditor.selectedShapeProperty().get());
    }

    @Test
    void testToBackwardSelectedShapeRequest() {
        List<Shape> expectedShapes = new ArrayList<>(),resultShapes;
        Line line = new Line(2.2, 3.5, 400.9, 485.1);
        Rectangle rectangle = new Rectangle(0, 0, 150, 150);
        Ellipse ellipse = new Ellipse(25.7, 29.35, 5.47, 3.9);
        
        drawing.addShape(line);
        drawing.addShape(rectangle);
        drawing.addShape(ellipse);

        //Testing that nothing happens
        drawEditor.clearSelectedShape();
        drawEditor.toBackwardSelectedShapeRequest();
        expectedShapes.clear();
        expectedShapes.add(line);
        expectedShapes.add(rectangle);
        expectedShapes.add(ellipse);
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i), resultShapes.get(i));
        }

        //Testing that the command is executed properly
        drawEditor.setSelectedShape(rectangle);
        drawEditor.toBackwardSelectedShapeRequest();
        expectedShapes.clear();
        expectedShapes.add(rectangle);
        expectedShapes.add(line);
        expectedShapes.add(ellipse);
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i), resultShapes.get(i));
        }
        assertNull(drawEditor.selectedShapeProperty().get());
    }

    @Test
    void testToForwardSelectedShapeRequest() {
        List<Shape> expectedShapes = new ArrayList<>(),resultShapes;
        Line line = new Line(2.2, 3.5, 400.9, 485.1);
        Rectangle rectangle = new Rectangle(0, 0, 150, 150);
        Ellipse ellipse = new Ellipse(25.7, 29.35, 5.47, 3.9);
        
        drawing.addShape(line);
        drawing.addShape(rectangle);
        drawing.addShape(ellipse);

        //Testing that nothing happens
        drawEditor.clearSelectedShape();
        drawEditor.toForwardSelectedShapeRequest();
        expectedShapes.clear();
        expectedShapes.add(line);
        expectedShapes.add(rectangle);
        expectedShapes.add(ellipse);
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i), resultShapes.get(i));
        }

        //Testing that the command is executed properly
        drawEditor.setSelectedShape(rectangle);
        drawEditor.toForwardSelectedShapeRequest();
        expectedShapes.clear();
        expectedShapes.add(line);
        expectedShapes.add(ellipse);
        expectedShapes.add(rectangle);
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i), resultShapes.get(i));
        }

        assertNull(drawEditor.selectedShapeProperty().get());
    }

    @Test
    void testToFrontSelectedShapeRequest() {
        List<Shape> expectedShapes = new ArrayList<>(),resultShapes;
        Line line = new Line(2.2, 3.5, 400.9, 485.1);
        Rectangle rectangle = new Rectangle(0, 0, 150, 150);
        Ellipse ellipse = new Ellipse(25.7, 29.35, 5.47, 3.9);
        
        drawing.addShape(line);
        drawing.addShape(rectangle);
        drawing.addShape(ellipse);

        //Testing that nothing happens
        drawEditor.clearSelectedShape();
        drawEditor.toFrontSelectedShapeRequest();
        expectedShapes.clear();
        expectedShapes.add(line);
        expectedShapes.add(rectangle);
        expectedShapes.add(ellipse);
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i), resultShapes.get(i));
        }

        //Testing that the command is executed properly
        drawEditor.setSelectedShape(line);
        drawEditor.toFrontSelectedShapeRequest();
        expectedShapes.clear();
        expectedShapes.add(rectangle);
        expectedShapes.add(ellipse);
        expectedShapes.add(line);
        resultShapes = drawing.getAllShapes();
        for(int i=0;i<resultShapes.size();i++){
            assertEquals(expectedShapes.get(i), resultShapes.get(i));
        }
        assertNull(drawEditor.selectedShapeProperty().get());
    }

    @Test
    void testSetGridSize() throws IllegalArgumentException, IllegalAccessException {
        DoubleProperty gridSize = (DoubleProperty) gridSizeField.get(drawing);
        for(double size: List.of(15.5,34.,25.5,775.4,36.,545.5,135.5,7.2)){
            drawEditor.setGridSize(size);
            assertEquals(size, gridSize.get());
        }
    }

    @Test
    void testSetGridVisible() throws IllegalArgumentException, IllegalAccessException {
        BooleanProperty isGridVisible = (BooleanProperty) isGridVisibleField.get(drawing);
        drawEditor.setGridVisible(true);
        assertTrue(isGridVisible.get());
        drawEditor.setGridVisible(false);
        assertFalse(isGridVisible.get());
    }
    
}
