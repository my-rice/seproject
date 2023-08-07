package it.faint.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Line extends AbstractShape{
    protected DoubleProperty startX;
    protected DoubleProperty startY;
    protected DoubleProperty endX;
    protected DoubleProperty endY;
    private BooleanProperty isOnMainDiagonal; //True if Line is on the main diagonal of it's bounding box

    public Line() {
        super();
        startX = new SimpleDoubleProperty(0);
        startY = new SimpleDoubleProperty(0);
        endX = new SimpleDoubleProperty(0);
        endY = new SimpleDoubleProperty(0);
        this.strokeWidth.set(2.0);
        bindIsOnMainDiagonal();
        
        addBoundsListenerLine();
        updateBounds();
    }

    public Line(Double startX, Double startY, Double endX, Double endY) {
        super();
        this.startX = new SimpleDoubleProperty(startX);
        this.startY = new SimpleDoubleProperty(startY);
        this.endX = new SimpleDoubleProperty(endX);
        this.endY = new SimpleDoubleProperty(endY);
        this.strokeWidth.set(2.0);

        bindIsOnMainDiagonal();

        addBoundsListenerLine();
        updateBounds();
    }

    private void addBoundsListenerLine(){
        this.startX.addListener(this.new BoundsListener());
        this.startY.addListener(this.new BoundsListener());
        this.endX.addListener(this.new BoundsListener());
        this.endY.addListener(this.new BoundsListener());
    }

    private void bindIsOnMainDiagonal(){
        isOnMainDiagonal = new SimpleBooleanProperty(true);
        isOnMainDiagonal.bind(
            startX.greaterThanOrEqualTo(endX).and(startY.greaterThanOrEqualTo(endY)).or(
                endX.greaterThanOrEqualTo(startX).and(endY.greaterThanOrEqualTo(startY))
            )
        );
    }

    public DoubleProperty startXProperty() {
        return startX;
    }
    public DoubleProperty startYProperty() {
        return startY;
    }
    public DoubleProperty endXProperty() {
        return endX;
    }
    public DoubleProperty endYProperty() {
        return endY;
    }
    public BooleanProperty isOnMainDiagonalProperty(){
        return isOnMainDiagonal;
    }


    @Override
    public BoundingBox getBoundingBox() {
        if(boundingBox == null){
            this.boundingBox = new BoundingBox(this);
        }
        return boundingBox;
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        try {
            out.writeDouble(this.startX.get());
            out.writeDouble(this.startY.get());
            out.writeDouble(this.endX.get());
            out.writeDouble(this.endY.get());
            out.writeUTF(this.fill.get().toString()); // Fill color
            out.writeUTF(this.stroke.get().toString());   // Stroke color
            //out.writeDouble(this.getStrokeWidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        try {
            this.startX.set(in.readDouble());
            this.startY.set(in.readDouble());
            this.endX.set(in.readDouble());
            this.endY.set(in.readDouble());
            this.fill.set((Color)Paint.valueOf(in.readUTF()));
            this.stroke.set((Color)Paint.valueOf(in.readUTF()));
            //this.setStrokeWidth(in.readDouble());
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void moveShape(Point2D topLeftCorner){
        Point2D start = new Point2D(startX.get(), startY.get());
        Point2D end =  new Point2D(endX.get(), endY.get());
        Rectangle2D r = computeBoundingRectangle(start,end);
        Point2D prevTopLeftCorner = new Point2D(r.getMinX(), r.getMinY());
        Point2D vector = topLeftCorner.subtract(prevTopLeftCorner);
        
        this.startX.set(start.add(vector).getX());
        this.startY.set(start.add(vector).getY());
        this.endX.set(end.add(vector).getX());
        this.endY.set(end.add(vector).getY());
    }

    @Override
    public boolean equals(Object obj) {
        Boolean isEqual = super.equals(obj);
        if (obj == this)
            return true;
        if (!(obj instanceof Line))
            return false;
        Line other = (Line) obj;
        isEqual &= this.startX.getValue().equals(other.startX.getValue());
        isEqual &= this.startY.getValue().equals(other.startY.getValue());
        isEqual &= this.endX.getValue().equals(other.endX.getValue());
        isEqual &= this.endY.getValue().equals(other.endY.getValue());
        return isEqual;
    }

    @Override
    public void resizeShapeOffset(Point2D offset) {
        if(bounds.get().getMaxX() + offset.getX() < bounds.get().getMinX() ||
           bounds.get().getMaxY() + offset.getY() < bounds.get().getMinY()){
            throw new RuntimeException("Invalid resize: " + new Point2D(bounds.get().getMaxX(), bounds.get().getMaxY()) + " to " + new Point2D(bounds.get().getMaxX() + offset.getX(), bounds.get().getMaxY() + offset.getY()) + " Top left is: " + new Point2D(bounds.get().getMinX(),bounds.get().getMinY()));
        }
        if(isOnMainDiagonal.get()){
            resizeShapeOffsetMainDiag(offset);
        } else {
            resizeShapeOffsetSecondaryDiag(offset);
        }
    }

    private void resizeShapeOffsetMainDiag(Point2D offset){
        DoubleProperty bottomRightX, bottomRightY;
        if(startX.get() > endX.get() || startY.get() > endY.get()){ //If start point is the rightmost one or lowest one
            bottomRightX = startX;
            bottomRightY = startY;
        } else { //else end point is the rightmost one or lowest one
            bottomRightX = endX;
            bottomRightY = endY;
        }
        
        bottomRightX.set(bottomRightX.get() + offset.getX());
        bottomRightY.set(bottomRightY.get() + offset.getY());
    }

    private void resizeShapeOffsetSecondaryDiag(Point2D offset){
        DoubleProperty bottomLeftY, topRightX;
        if(startY.get() > endY.get()){ //If start point is the bottom one
            bottomLeftY = startY;
            topRightX = endX;
        } else { //else end point is the bottom one
            bottomLeftY = endY;
            topRightX = startX;
        }
        bottomLeftY.set(bottomLeftY.get() + offset.getY());
        topRightX.set(topRightX.get() + offset.getX());
    }

    @Override
    protected void updateBounds() {
        Rectangle2D r = computeBoundingRectangle(new Point2D(startX.get(), startY.get()),new Point2D(endX.get(), endY.get()));
        this.bounds.set(r);
    }
    
    public ResizeGizmo getResizeGizmo() {
        if(this.resizeGizmo == null){
            this.resizeGizmo = new ResizeGizmo(this);
        }
        return this.resizeGizmo;
    }
    public Object clone() throws CloneNotSupportedException {
        Line line = new Line(this.startX.doubleValue(), this.startY.doubleValue(), this.endX.doubleValue(), this.endY.doubleValue());
        line.setStroke(this.stroke.get());
        line.setFill(this.fill.get());
        line.setOpacity(this.opacity.get());
    
        return line;
    }
        
        
}
