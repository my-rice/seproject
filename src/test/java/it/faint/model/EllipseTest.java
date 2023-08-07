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

public class EllipseTest {

    private List<Ellipse> ellipses;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        ellipses = List.of(
            new Ellipse(-10.5, -7.5, 5.8, 6.9),
            new Ellipse(321., 3.2, 5.34, 4.32),
            new Ellipse(7.35, -73.5, 65.87, 12.9),
            new Ellipse(-23.8, 44.0, 43.76, 97.23),
            new Ellipse(43., 4.6, 25.4, 10.3),
            new Ellipse(98., -7.5, 0.8, 3.85),
            new Ellipse(-39.5, 23.5, 556.8, 443.9),
            new Ellipse(0., 0., 0.1, 0.1)
        );
    }

    private static boolean almostEqual(Point2D a, Point2D b, double eps){
        return a.distance(b)<eps;
    }

    private Ellipse cloneEllipse(Ellipse e){
        Ellipse clone = new Ellipse(e.centerXProperty().get(), e.centerYProperty().get(), e.radiusXProperty().get(), e.radiusYProperty().get());
        clone.opacityProperty().set(e.opacityProperty().get());
        clone.strokeWidthProperty().set(e.strokeWidthProperty().get());
        clone.fillProperty().set(e.fillProperty().get());
        clone.dashedStrokeProperty().set(e.dashedStrokeProperty().get());
        clone.strokeProperty().set(e.strokeProperty().get());
        return clone;
    }

    private void testEqualsHelper(Ellipse e){
        List<Ellipse> clones = new ArrayList<>();
        for(int i=0; i<9; i++){
            clones.add(cloneEllipse(e));
            assertTrue(e.equals(clones.get(i)));
        }
        clones.get(0).opacityProperty().set(0.);
        clones.get(1).strokeWidthProperty().set(0.);
        clones.get(2).fillProperty().set(Color.BLANCHEDALMOND);
        clones.get(3).dashedStrokeProperty().set(true);
        clones.get(4).strokeProperty().set(Color.BLANCHEDALMOND);
        clones.get(5).centerXProperty().set(999.);
        clones.get(6).centerYProperty().set(999.);
        clones.get(7).radiusXProperty().set(999.);
        clones.get(8).radiusYProperty().set(999.);
        for(int i=0; i<9; i++){
            assertFalse(e.equals(clones.get(i)));
        }
    }

    @Test
    void testEquals() {
        for(Ellipse ellipse: ellipses){
            testEqualsHelper(ellipse);
        }
    }

    @Test
    void testGetBoundingBox() {
        for(Ellipse ellipse: ellipses){
            assertEquals(ellipse.getBoundingBox(), ellipse.getBoundingBox());
        }
    }

    @Test
    void testGetResizeGizmo() {
        for(Ellipse ellipse: ellipses){
            assertEquals(ellipse.getResizeGizmo(), ellipse.getResizeGizmo());
        }
    }

    private void testMoveShapeHelper(Ellipse ellipse){
        List<Point2D> newTopLefts = List.of(new Point2D(132.2,2.45), new Point2D(-46.2,24.3), new  Point2D(-4.,-5.6), new Point2D(9.3,-4.2));
        for(Point2D newTopLeft: newTopLefts){
            ellipse.moveShape(newTopLeft);
            Point2D topLeft = new Point2D(ellipse.centerXProperty().get() - ellipse.radiusXProperty().get(), ellipse.centerYProperty().get() - ellipse.radiusYProperty().get());
            assertTrue(almostEqual(newTopLeft, topLeft, 0.000000001));
        }
    }

    @Test
    void testMoveShape() {
        for(Ellipse ellipse: ellipses){
            testMoveShapeHelper(ellipse);
        }
    }

    private void testResizeShapeOffsetHelper(Ellipse ellipse){
        List<Point2D> offsets = List.of(new Point2D(132.2,2.45), new Point2D(-3.3,-1.3), new  Point2D(55.4,5.6), new Point2D(42.35, -2.2));
        Point2D bottomRight, prevBottomRight;
        for(Point2D offset: offsets){
            prevBottomRight = new Point2D(ellipse.centerXProperty().get() + ellipse.radiusXProperty().get(), ellipse.centerYProperty().get() + ellipse.radiusYProperty().get());
            ellipse.resizeShapeOffset(offset);
            bottomRight = new Point2D(ellipse.centerXProperty().get() + ellipse.radiusXProperty().get(), ellipse.centerYProperty().get() + ellipse.radiusYProperty().get());
            assertTrue(almostEqual(prevBottomRight.add(offset), bottomRight, 0.00000000001));
        }
    }

    @Test
    void testResizeShapeOffset() {
        //Valid resizes
        for(Ellipse ellipse: ellipses){
            testResizeShapeOffsetHelper(ellipse);
        }
    }

    private void testResizeShapeHelper(Ellipse ellipse){
        List<Point2D> newBottomRights = List.of(new Point2D(900.2,875.45), new Point2D(4978.2,5274.3), new  Point2D(985.4,555.6), new Point2D(1000.35,774.2));
        Point2D bottomRight;
        for(Point2D newBottomRight: newBottomRights){
            ellipse.resizeShape(newBottomRight);
            bottomRight = new Point2D(ellipse.centerXProperty().get() + ellipse.radiusXProperty().get(), ellipse.centerYProperty().get() + ellipse.radiusYProperty().get());
            assertTrue(almostEqual(newBottomRight , bottomRight, 0.00000000001));
        }
    }

    @Test
    void testResizeShape() {
        //Valid resizes
        for(Ellipse ellipse: ellipses){
            testResizeShapeHelper(ellipse);
        }
    }

    @Test
    void testClone() throws CloneNotSupportedException {

        Shape expected=new Ellipse();
        Shape result=new Ellipse();
        
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
            expected= new Ellipse( ((r.getMaxX() - r.getMinX() )/2)+ r.getMinX() ,((r.getMaxY() - r.getMinY() )/2)+ r.getMinY() , (r.getMaxX() - r.getMinX() )/2 , (r.getMaxY() - r.getMinY() )/2 );
            for(Color c: colorList){
                expected.setFill(c);
                expected.setStroke(c);
                expected.setOpacity(Math.random());
                result= (Shape) expected.clone();
                assertEquals(expected, result);
            }
        }

        /*
        Ellipse ellipse =new Ellipse();
        assertThrows(java.lang.CloneNotSupportedException.class, ()->{
            ellipse.clone();
        });
        */
    }

    @Test
    void testReadExternal() throws IOException, ClassNotFoundException {
        //Manually write the fields to a raw byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        objectOutput.writeDouble(671.2); //centerX
        objectOutput.writeDouble(-32.1); //centerY
        objectOutput.writeDouble(78.4); //radiusX
        objectOutput.writeDouble(22); //radiusY
        objectOutput.writeUTF(Color.DARKRED.toString()); //fill
        objectOutput.writeUTF(Color.MINTCREAM.toString()); //stroke
        objectOutput.flush();
        //Read the shape from the raw byte array
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        Ellipse resultEllipse = new Ellipse();
        resultEllipse.readExternal(in);
        //Construct the expected shape
        Ellipse expectedEllipse = new Ellipse(671.2, -32.1, 78.4, 22.);
        expectedEllipse.setFill(Color.DARKRED);
        expectedEllipse.setStroke(Color.MINTCREAM);
        //Check if read shape is equal to the expected one
        assertTrue(resultEllipse.equals(expectedEllipse));
    }

    @Test
    void testWriteExternal() throws IOException {
        for(Ellipse e: ellipses){
            //Write shape to a byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(byteArrayOutputStream);
            e.writeExternal(out);
            out.flush();
            //Check if fields written in the byte array are correct
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
            assertEquals(e.centerXProperty().get(), in.readDouble());
            assertEquals(e.centerYProperty().get(), in.readDouble());
            assertEquals(e.radiusXProperty().get(), in.readDouble());
            assertEquals(e.radiusYProperty().get(), in.readDouble());
            assertEquals(e.fillProperty().get().toString(), in.readUTF());
            assertEquals(e.strokeProperty().get().toString(), in.readUTF());
        }
    }

    @Test
    void testUpdateBounds() {
        for(Ellipse e: ellipses){
            Rectangle2D bounds = e.boundsProperty().get();
            Point2D bottomRight = new Point2D(bounds.getMaxX(), bounds.getMaxY());
            Point2D newBottomRight = bottomRight.add(new Point2D(-0.2,86.));
            e.resizeShape(newBottomRight);
            Rectangle2D expectedBounds = new Rectangle2D(bounds.getMinX(), bounds.getMinY(), newBottomRight.getX()-bounds.getMinX(), newBottomRight.getY()-bounds.getMinY());
            Rectangle2D resultBounds = e.boundsProperty().get();
            //assertEquals(expectedBounds, e.boundsProperty().get());
            assertEquals(expectedBounds.getMinX(), resultBounds.getMinX(), 0.000000001);
            assertEquals(expectedBounds.getMinY(), resultBounds.getMinY(), 0.000000001);
            assertEquals(expectedBounds.getMaxX(), resultBounds.getMaxX(), 0.000000001);
            assertEquals(expectedBounds.getMaxY(), resultBounds.getMaxY(), 0.000000001);
        }
    }
}
