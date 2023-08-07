package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class RectangleTest {
    private List<Rectangle> rectangles;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        rectangles = List.of(
            new Rectangle(-10.5, -7.5, 5.8, 6.9),
            new Rectangle(321., 3.2, 5.34, 4.32),
            new Rectangle(7.35, -73.5, 65.87, 12.9),
            new Rectangle(-23.8, 44.0, 43.76, 97.23),
            new Rectangle(43., 4.6, 25.4, 10.3),
            new Rectangle(98., -7.5, 0.8, 3.85),
            new Rectangle(-39.5, 23.5, 556.8, 443.9),
            new Rectangle(0., 0., 0.1, 0.1)
        );
    }

    private static boolean almostEqual(Point2D a, Point2D b, double eps){
        return a.distance(b)<eps;
    }

    private Rectangle cloneRectangle(Rectangle r){
        Rectangle clone = new Rectangle(r.xProperty().get(), r.yProperty().get(), r.widthProperty().get(), r.heightProperty().get());
        clone.opacityProperty().set(r.opacityProperty().get());
        clone.strokeWidthProperty().set(r.strokeWidthProperty().get());
        clone.fillProperty().set(r.fillProperty().get());
        clone.dashedStrokeProperty().set(r.dashedStrokeProperty().get());
        clone.strokeProperty().set(r.strokeProperty().get());
        return clone;
    }

    private void testEqualsHelper(Rectangle r){
        List<Rectangle> clones = new ArrayList<>();
        for(int i=0; i<9; i++){
            clones.add(cloneRectangle(r));
            assertTrue(r.equals(clones.get(i)));
        }
        clones.get(0).opacityProperty().set(0.);
        clones.get(1).strokeWidthProperty().set(0.);
        clones.get(2).fillProperty().set(Color.BLANCHEDALMOND);
        clones.get(3).dashedStrokeProperty().set(true);
        clones.get(4).strokeProperty().set(Color.BLANCHEDALMOND);
        clones.get(5).xProperty().set(999.);
        clones.get(6).yProperty().set(999.);
        clones.get(7).widthProperty().set(999.);
        clones.get(8).heightProperty().set(999.);
        for(int i=0; i<9; i++){
            assertFalse(r.equals(clones.get(i)));
        }
    }

    @Test
    void testEquals() {
        for(Rectangle rectangle: rectangles){
            testEqualsHelper(rectangle);
        }
    }

    @Test
    void testGetBoundingBox() {
        for(Rectangle rectangle: rectangles){
            assertEquals(rectangle.getBoundingBox(), rectangle.getBoundingBox());
        }
    }

    @Test
    void testGetResizeGizmo() {
        for(Rectangle rectangle: rectangles){
            assertEquals(rectangle.getResizeGizmo(), rectangle.getResizeGizmo());
        }
    }

    private void testMoveShapeHelper(Rectangle rectangle){
        List<Point2D> newTopLefts = List.of(new Point2D(132.2,2.45), new Point2D(-46.2,24.3), new  Point2D(-4.,-5.6), new Point2D(9.3,-4.2));
        for(Point2D newTopLeft: newTopLefts){
            rectangle.moveShape(newTopLeft);
            Point2D topLeft = new Point2D(rectangle.xProperty().get(), rectangle.yProperty().get());
            assertTrue(almostEqual(newTopLeft, topLeft, 0.000000001));
        }
    }

    @Test
    void testMoveShape() {
        for(Rectangle rectangle: rectangles){
            testMoveShapeHelper(rectangle);
        }
    }

    private void testResizeShapeHelper(Rectangle rectangle){
        List<Point2D> newBottomRights = List.of(new Point2D(900.2,875.45), new Point2D(4978.2,5274.3), new  Point2D(985.4,555.6), new Point2D(1000.35,774.2));
        Point2D bottomRight;
        for(Point2D newBottomRight: newBottomRights){
            rectangle.resizeShape(newBottomRight);
            bottomRight = new Point2D(rectangle.xProperty().get() + rectangle.widthProperty().get(), rectangle.yProperty().get() + rectangle.heightProperty().get());
            assertTrue(almostEqual(newBottomRight , bottomRight, 0.00000000001));
        }
    }

    @Test
    void testResizeShape() {
        for(Rectangle rectangle: rectangles){
            testResizeShapeHelper(rectangle);
        }
    }

    private void testResizeShapeOffsetHelper(Rectangle rectangle){
        List<Point2D> offsets = List.of(new Point2D(132.2,2.45), new Point2D(-3.3,-1.3), new  Point2D(55.4,5.6), new Point2D(42.35, -2.2));
        Point2D bottomRight, prevBottomRight;
        for(Point2D offset: offsets){
            prevBottomRight = new Point2D(rectangle.xProperty().get() + rectangle.widthProperty().get(), rectangle.yProperty().get() + rectangle.heightProperty().get());
            rectangle.resizeShapeOffset(offset);
            bottomRight = new Point2D(rectangle.xProperty().get() + rectangle.widthProperty().get(), rectangle.yProperty().get() + rectangle.heightProperty().get());
            assertTrue(almostEqual(prevBottomRight.add(offset), bottomRight, 0.00000000001));
        }
    }

    @Test
    void testResizeShapeOffset() {
        for(Rectangle rectangle: rectangles){
            testResizeShapeOffsetHelper(rectangle);
        }
    }
    
    @Test
    void testClone() throws CloneNotSupportedException {

        Shape expected=new Rectangle();
        Shape result=new Rectangle();
        
        List<javafx.geometry.Rectangle2D> rectangleList = new ArrayList<>();
        rectangleList.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 10.0, 5.0));
        rectangleList.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 5.0, 10.0));
        rectangleList.add(new javafx.geometry.Rectangle2D(0.0, -2.0, 100.0, 5.0));
        rectangleList.add(new javafx.geometry.Rectangle2D(1.0, 2.0, 1.0, 0.0));
        rectangleList.add(new javafx.geometry.Rectangle2D(-1.0, -2.0, 0.0, 1.0));

        LinkedList<Color> colorList = new LinkedList<>();
        colorList.add(new Color(0.46,0.23,0.13,0.8)); 
        colorList.add(Color.DARKCYAN);
        colorList.add(Color.BISQUE);
        colorList.add(Color.SEASHELL);
        colorList.add(Color.GAINSBORO);
        colorList.add(Color.CORNFLOWERBLUE);
        colorList.add(Color.PAPAYAWHIP);
        colorList.add(Color.TURQUOISE);
        
        for(javafx.geometry.Rectangle2D r: rectangleList){
            expected= new Rectangle(r.getMinX(),r.getMinY(),r.getWidth(),r.getHeight());
            for(Color c: colorList){
                expected.setFill(c);
                expected.setStroke(c);
                expected.setOpacity(Math.random());
                result= (Shape) expected.clone();
                assertEquals(expected, result);
            }
        }

        /*
        Rectangle rectangle =new Rectangle();
        assertThrows(java.lang.CloneNotSupportedException.class, ()->{
            rectangle.clone();
        });
        */
    }

    @Test
    void testReadExternal() throws IOException, ClassNotFoundException {
        //Manually write the fields to a raw byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        objectOutput.writeDouble(671.2); //x
        objectOutput.writeDouble(-32.1); //y
        objectOutput.writeDouble(78.4); //width
        objectOutput.writeDouble(22); //height
        objectOutput.writeUTF(Color.DARKRED.toString()); //fill
        objectOutput.writeUTF(Color.MINTCREAM.toString()); //stroke
        objectOutput.flush();
        //Read the shape from the raw byte array
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        Rectangle resultRectangle = new Rectangle();
        resultRectangle.readExternal(in);
        //Construct the expected shape
        Rectangle expectedRectangle = new Rectangle(671.2, -32.1, 78.4, 22.);
        expectedRectangle.setFill(Color.DARKRED);
        expectedRectangle.setStroke(Color.MINTCREAM);
        //Check if read shape is equal to the expected one
        assertTrue(resultRectangle.equals(expectedRectangle));
    }

    @Test
    void testWriteExternal() throws IOException {
        for(Rectangle r: rectangles){
            //Write shape to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(byteArrayOutputStream);
            r.writeExternal(out);
            out.flush();
            //Check if fields written in the byte array are correct
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            assertEquals(r.xProperty().get(), in.readDouble());
            assertEquals(r.yProperty().get(), in.readDouble());
            assertEquals(r.widthProperty().get(), in.readDouble());
            assertEquals(r.heightProperty().get(), in.readDouble());
            assertEquals(r.fillProperty().get().toString(), in.readUTF());
            assertEquals(r.strokeProperty().get().toString(), in.readUTF());
        }
    }

    @Test
    void testUpdateBounds() {
        for(Rectangle r: rectangles){
            Rectangle2D bounds = r.boundsProperty().get();
            Point2D bottomRight = new Point2D(bounds.getMaxX(), bounds.getMaxY());
            Point2D newBottomRight = bottomRight.add(new Point2D(45.,86.));
            r.resizeShape(newBottomRight);
            Rectangle2D expectedBounds = new Rectangle2D(bounds.getMinX(), bounds.getMinY(), newBottomRight.getX()-bounds.getMinX(), newBottomRight.getY()-bounds.getMinY());
            Rectangle2D resultBounds = r.boundsProperty().get();
            //assertEquals(expectedBounds, e.boundsProperty().get());
            assertEquals(expectedBounds.getMinX(), resultBounds.getMinX(), 0.000000001);
            assertEquals(expectedBounds.getMinY(), resultBounds.getMinY(), 0.000000001);
            assertEquals(expectedBounds.getMaxX(), resultBounds.getMaxX(), 0.000000001);
            assertEquals(expectedBounds.getMaxY(), resultBounds.getMaxY(), 0.000000001);
        }
    }
}
