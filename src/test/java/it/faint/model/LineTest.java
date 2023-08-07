package it.faint.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class LineTest {

    private List<Line> horizontalLines, verticalLines, mainDiagLines, secondaryDiagLines, specialLines;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        List<Rectangle2D> bounds = List.of(
            new Rectangle2D(-10.5, -7.5, 5.8, 6.9),
            new Rectangle2D(321, 32, 534, 432),
            new Rectangle2D(7.35, -73.5, 65.87, 12.9),
            new Rectangle2D(-23.8, 44.0, 43.76, 97.23),
            new Rectangle2D(43, 4.6, 25.4, 10.3),
            new Rectangle2D(98, -7.5, 0.8, 3.85),
            new Rectangle2D(-39.5, 23.5, 556.8, 443.9),
            new Rectangle2D(0, 0, 0.1, 0.1)
        );

        horizontalLines = new ArrayList<>();
        verticalLines = new ArrayList<>();
        mainDiagLines = new ArrayList<>();
        secondaryDiagLines = new ArrayList<>();
        
        for(Rectangle2D bound: bounds){
            horizontalLines.add(new Line(bound.getMinX(), bound.getMinY(), bound.getMinX() + bound.getWidth(), bound.getMinY()));
            horizontalLines.add(new Line(bound.getMinX(), bound.getMaxY(), bound.getMinX() + bound.getWidth(), bound.getMaxY()));
            horizontalLines.add(new Line(bound.getMinX() + bound.getWidth(), bound.getMinY(), bound.getMinX(), bound.getMinY()));
            horizontalLines.add(new Line(bound.getMinX() + bound.getWidth(), bound.getMaxY(), bound.getMinX(), bound.getMaxY()));

            verticalLines.add(new Line(bound.getMinX(), bound.getMinY(), bound.getMinX(), bound.getMinY() + bound.getHeight()));
            verticalLines.add(new Line(bound.getMaxX(), bound.getMinY(), bound.getMaxX(), bound.getMinY() + bound.getHeight()));
            verticalLines.add(new Line(bound.getMinX(), bound.getMinY() + bound.getHeight(), bound.getMinX(), bound.getMinY()));
            verticalLines.add(new Line(bound.getMaxX(), bound.getMinY() + bound.getHeight(), bound.getMaxX(), bound.getMinY()));

            mainDiagLines.add(new Line(bound.getMinX(), bound.getMinY(), bound.getMaxX(), bound.getMaxY()));
            mainDiagLines.add(new Line(bound.getMaxX(), bound.getMaxY(), bound.getMinX(), bound.getMinY()));

            secondaryDiagLines.add(new Line(bound.getMinX(), bound.getMaxY(), bound.getMaxX(), bound.getMinY()));
            secondaryDiagLines.add(new Line(bound.getMaxX(), bound.getMinY(), bound.getMinX(), bound.getMaxY()));
        }

        specialLines = List.of(
            new Line(0.,0.,0.,0.),
            new Line(4.,4.,4.,4.),
            new Line(-2.2,-2.2,-2.2,-2.2),
            new Line(-42.,-42.,-42., -42.)
        );
    }

    private BooleanProperty extractIsOnMainDiagonalProperty(Line l) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException{
        Field isOnMainDiagonalField = Line.class.getDeclaredField("isOnMainDiagonal");
        isOnMainDiagonalField.setAccessible(true);
        return (BooleanProperty) isOnMainDiagonalField.get(l);
    }

    @Test
    void testBindIsOnMainDiagonalProperty() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        for(Line line: mainDiagLines){
            assertTrue(extractIsOnMainDiagonalProperty(line).get());
        }
        for(Line line: secondaryDiagLines){
            assertFalse(extractIsOnMainDiagonalProperty(line).get());
        }
        for(Line line: verticalLines){
            assertTrue(extractIsOnMainDiagonalProperty(line).get());
        }
        for(Line line: horizontalLines){
            assertTrue(extractIsOnMainDiagonalProperty(line).get());
        }
        for(Line line: specialLines){
            assertTrue(extractIsOnMainDiagonalProperty(line).get());
        }
    }

    private Line cloneLine(Line l){
        Line clone = new Line(l.startXProperty().get(), l.startYProperty().get(), l.endXProperty().get(), l.endYProperty().get());
        clone.opacityProperty().set(l.opacityProperty().get());
        clone.strokeWidthProperty().set(l.strokeWidthProperty().get());
        clone.fillProperty().set(l.fillProperty().get());
        clone.dashedStrokeProperty().set(l.dashedStrokeProperty().get());
        clone.strokeProperty().set(l.strokeProperty().get());
        return clone;
    }

    private void testEqualsHelper(Line line){
        List<Line> clones = new ArrayList<>();
        for(int i=0; i<9; i++){
            clones.add(cloneLine(line));
            assertTrue(line.equals(clones.get(i)));
        }
        clones.get(0).opacityProperty().set(0.);
        clones.get(1).strokeWidthProperty().set(0.);
        clones.get(2).fillProperty().set(Color.BLANCHEDALMOND);
        clones.get(3).dashedStrokeProperty().set(true);
        clones.get(4).strokeProperty().set(Color.BLANCHEDALMOND);
        clones.get(5).startXProperty().set(999.);
        clones.get(6).startYProperty().set(999.);
        clones.get(7).endXProperty().set(999.);
        clones.get(8).endYProperty().set(999.);
        for(int i=0; i<9; i++){
            assertFalse(line.equals(clones.get(i)));
        }
    }

    @Test
    void testEquals() {
        for(Line line: mainDiagLines){
            testEqualsHelper(line);
        }
        for(Line line: secondaryDiagLines){
            testEqualsHelper(line);
        }
        for(Line line: verticalLines){
            testEqualsHelper(line);
        }
        for(Line line: horizontalLines){
            testEqualsHelper(line);
        }
        for(Line line: specialLines){
            testEqualsHelper(line);
        }

    }

    @Test
    void testGetBoundingBox() {
        for(Line line: mainDiagLines){
            assertEquals(line.getBoundingBox(), line.getBoundingBox());
        }
        for(Line line: secondaryDiagLines){
            assertEquals(line.getBoundingBox(), line.getBoundingBox());
        }
        for(Line line: verticalLines){
            assertEquals(line.getBoundingBox(), line.getBoundingBox());
        }
        for(Line line: horizontalLines){
            assertEquals(line.getBoundingBox(), line.getBoundingBox());
        }
        for(Line line: specialLines){
            assertEquals(line.getBoundingBox(), line.getBoundingBox());
        }
    }
    
    @Test
    void testGetResizeGizmo() {
        for(Line line: mainDiagLines){
            assertEquals(line.getResizeGizmo(), line.getResizeGizmo());
        }
        for(Line line: secondaryDiagLines){
            assertEquals(line.getResizeGizmo(), line.getResizeGizmo());
        }
        for(Line line: verticalLines){
            assertEquals(line.getResizeGizmo(), line.getResizeGizmo());
        }
        for(Line line: horizontalLines){
            assertEquals(line.getResizeGizmo(), line.getResizeGizmo());
        }
        for(Line line: specialLines){
            assertEquals(line.getResizeGizmo(), line.getResizeGizmo());
        }
    }

    private Rectangle2D computeBoundingRectangle(Point2D firstPoint,Point2D secondPoint){
        double minX, minY, maxX, maxY;
        minX = Double.min(firstPoint.getX(), secondPoint.getX());
        minY = Double.min(firstPoint.getY(), secondPoint.getY());
        maxX = Double.max(firstPoint.getX(), secondPoint.getX());
        maxY = Double.max(firstPoint.getY(), secondPoint.getY());
        return new Rectangle2D(minX, minY, maxX-minX, maxY-minY);
    }

    private static boolean almostEqual(Point2D a, Point2D b, double eps){
        return a.distance(b)<eps;
    }

    private void testMoveShapeHelper(Line line){
        List<Point2D> newTopLefts = List.of(new Point2D(132.2,2.45), new Point2D(-46.2,24.3), new  Point2D(-4.,-5.6), new Point2D(9.3,-4.2));
        for(Point2D newTopLeft: newTopLefts){
            line.moveShape(newTopLeft);
            Point2D start = new Point2D(line.startXProperty().get(), line.startYProperty().get());
            Point2D end =  new Point2D(line.endXProperty().get(), line.endYProperty().get());
            Rectangle2D b = computeBoundingRectangle(start,end);
            Point2D topLeft = new Point2D(b.getMinX(), b.getMinY());
            assertTrue(almostEqual(newTopLeft, topLeft, 0.000000001));
        }
    }

    @Test
    void testMoveShape() {
        for(Line line: mainDiagLines){
            testMoveShapeHelper(line);
        }
        for(Line line: secondaryDiagLines){
            testMoveShapeHelper(line);
        }
        for(Line line: verticalLines){
            testMoveShapeHelper(line);
        }
        for(Line line: horizontalLines){
            testMoveShapeHelper(line);
        }
        for(Line line: specialLines){
            testMoveShapeHelper(line);
        }
    }

    private void testResizeShapeOffsetHelper(Line line){
        List<Point2D> offsets = List.of(new Point2D(132.2,2.45), new Point2D(-3.3,-1.3), new  Point2D(55.4,5.6), new Point2D(42.35, -2.2));
        Point2D start, end, bottomRight, prevBottomRight;
        Rectangle2D b;
        for(Point2D offset: offsets){
            start = new Point2D(line.startXProperty().get(), line.startYProperty().get());
            end = new Point2D(line.endXProperty().get(), line.endYProperty().get());
            b = computeBoundingRectangle(start,end);
            prevBottomRight = new Point2D(b.getMaxX(), b.getMaxY());

            line.resizeShapeOffset(offset);

            start = new Point2D(line.startXProperty().get(), line.startYProperty().get());
            end = new Point2D(line.endXProperty().get(), line.endYProperty().get());
            b = computeBoundingRectangle(start,end);
            bottomRight = new Point2D(b.getMaxX(), b.getMaxY());
            assertTrue(almostEqual(prevBottomRight.add(offset), bottomRight, 0.00000000001));
        }
    }

    @Test
    void testResizeShapeOffset() {
        //Valid resizes
        for(Line line: mainDiagLines){
            testResizeShapeOffsetHelper(line);
        }
        for(Line line: secondaryDiagLines){
            testResizeShapeOffsetHelper(line);
        }
        for(Line line: verticalLines){
            testResizeShapeOffsetHelper(line);
        }
        for(Line line: horizontalLines){
            testResizeShapeOffsetHelper(line);
        }
        for(Line line: specialLines){
            testResizeShapeOffsetHelper(line);
        }
        //Invalid resizes
        for(Line line: mainDiagLines){
            assertThrows(RuntimeException.class, ()->{line.resizeShapeOffset(new Point2D(-999.23, -26332.3));});
        }
        for(Line line: secondaryDiagLines){
            assertThrows(RuntimeException.class, ()->{line.resizeShapeOffset(new Point2D(-654689.23, 2.3));});
        }
        for(Line line: verticalLines){
            assertThrows(RuntimeException.class, ()->{line.resizeShapeOffset(new Point2D(6.35, -388882.3));});
        }
        for(Line line: horizontalLines){
            assertThrows(RuntimeException.class, ()->{line.resizeShapeOffset(new Point2D(9872., -26332.3));});
        }
        for(Line line: specialLines){
            assertThrows(RuntimeException.class, ()->{line.resizeShapeOffset(new Point2D(-999.23, 17.));});
        }
    }

    private void testResizeShapeHelper(Line line){
        List<Point2D> newBottomRights = List.of(new Point2D(900.2,875.45), new Point2D(4978.2,5274.3), new  Point2D(985.4,555.6), new Point2D(1000.35,774.2));
        Point2D start, end, bottomRight;
        Rectangle2D b;
        for(Point2D newBottomRight: newBottomRights){
            line.resizeShape(newBottomRight);

            start = new Point2D(line.startXProperty().get(), line.startYProperty().get());
            end = new Point2D(line.endXProperty().get(), line.endYProperty().get());
            b = computeBoundingRectangle(start,end);
            bottomRight = new Point2D(b.getMaxX(), b.getMaxY());
            assertTrue(almostEqual(newBottomRight, bottomRight, 0.00000000001));
        }
    }

    @Test
    void testResizeShape() {
        //Valid resizes
        for(Line line: mainDiagLines){
            testResizeShapeHelper(line);
        }
        for(Line line: secondaryDiagLines){
            testResizeShapeHelper(line);
        }
        for(Line line: verticalLines){
            testResizeShapeHelper(line);
        }
        for(Line line: horizontalLines){
            testResizeShapeHelper(line);
        }
        for(Line line: specialLines){
            testResizeShapeHelper(line);
        }
        //Invalid resizes
        for(Line line: mainDiagLines){
            assertThrows(RuntimeException.class, ()->{line.resizeShape(new Point2D(-999.23, -26332.3));});
        }
        for(Line line: secondaryDiagLines){
            assertThrows(RuntimeException.class, ()->{line.resizeShape(new Point2D(-654689.23, 2.3));});
        }
        for(Line line: verticalLines){
            assertThrows(RuntimeException.class, ()->{line.resizeShape(new Point2D(6.35, -388882.3));});
        }
        for(Line line: horizontalLines){
            assertThrows(RuntimeException.class, ()->{line.resizeShape(new Point2D(9872., -26332.3));});
        }
        for(Line line: specialLines){
            assertThrows(RuntimeException.class, ()->{line.resizeShape(new Point2D(-999.23, 17.));});
        }
    }
    @Test
    void testClone() throws CloneNotSupportedException {

        Shape expected=new Line();
        Shape result=new Line();
        

        List<Point2D[]> pointList = new ArrayList<>();
        pointList.add(new Point2D[]{new Point2D(1.0, 1.0), new Point2D(100.0, 100.0)});
        pointList.add(new Point2D[]{new Point2D(-1.0, 1.0), new Point2D(-100.0, 100.0)});
        pointList.add(new Point2D[]{new Point2D(1.0, -1.0), new Point2D(100.0, -100.0)});
        pointList.add(new Point2D[]{new Point2D(-1.0,-1.0), new Point2D(-100.0, -100.0)});
        pointList.add(new Point2D[]{new Point2D(100.0, 100.0), new Point2D(1.0, 1.0)});
        pointList.add(new Point2D[]{new Point2D(-100.0, -100.0), new Point2D(-1.0, -1.0)});

        LinkedList<Color> colorList = new LinkedList<>();
        colorList.add(new Color(0.46,0.23,0.13,0.8)); 
        colorList.add(Color.DARKCYAN);
        colorList.add(Color.BISQUE);
        colorList.add(Color.SEASHELL);
        colorList.add(Color.GAINSBORO);
        colorList.add(Color.CORNFLOWERBLUE);
        colorList.add(Color.PAPAYAWHIP);
        colorList.add(Color.TURQUOISE);
        
        for(Point2D[] p: pointList){
            expected= new Line(p[0].getX(), p[0].getY(), p[1].getX(), p[1].getY());
            for(Color c: colorList){
                expected.setFill(c);
                expected.setStroke(c);
                expected.setOpacity(Math.random());
                result= (Shape) expected.clone();
                assertEquals(expected, result);
            }
        }

        /*
        Line line=new Line();
        assertThrows(java.lang.CloneNotSupportedException.class, ()->{
            line.clone();
        });
        */
    }

    @Test
    void testReadExternal() throws IOException, ClassNotFoundException {
        //Manually write the fields to a raw byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        objectOutput.writeDouble(766.2); //startX
        objectOutput.writeDouble(33.1); //startY
        objectOutput.writeDouble(-3.4); //endX
        objectOutput.writeDouble(23.4); //endY
        objectOutput.writeUTF(Color.AZURE.toString()); //fill
        objectOutput.writeUTF(Color.BEIGE.toString()); //stroke
        objectOutput.flush();
        //Read the shape from the raw byte array
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        Line resultLine = new Line();
        resultLine.readExternal(in);
        //Construct the expected shape
        Line expectedLine = new Line(766.2, 33.1, -3.4, 23.4);
        expectedLine.setFill(Color.AZURE);
        expectedLine.setStroke(Color.BEIGE);
        //Check if read shape is equal to the expected one
        assertTrue(resultLine.equals(expectedLine));
    }

    @Test
    void testWriteExternal() throws IOException {
        //Write shape to a byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(byteArrayOutputStream);
        Line line = new Line(766.2, 33.1, -3.4, 23.4);
        line.setFill(Color.AZURE);
        line.setStroke(Color.BEIGE);
        line.writeExternal(out);
        out.flush();
        //Check if fields written in the byte array are correct
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        assertEquals(766.2, in.readDouble());
        assertEquals(33.1, in.readDouble());
        assertEquals(-3.4, in.readDouble());
        assertEquals(23.4, in.readDouble());
        assertEquals(Color.AZURE.toString(), in.readUTF());
        assertEquals(Color.BEIGE.toString(), in.readUTF());
    }

    @Test
    void testUpdateBounds() {
        Line l = mainDiagLines.get(0);
        Rectangle2D bounds = l.boundsProperty().get();
        l.resizeShape(new Point2D(505.5, 3.32));
        Rectangle2D expectedBounds = new Rectangle2D(bounds.getMinX(), bounds.getMinY(), 505.5-bounds.getMinX(), 3.32-bounds.getMinY());
        assertEquals(expectedBounds, l.boundsProperty().get());
    }

    
}

    
