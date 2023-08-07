package it.faint.model;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Ellipse extends AbstractShape{
    protected DoubleProperty centerX;
    protected DoubleProperty centerY;
    protected DoubleProperty radiusX;
    protected DoubleProperty radiusY;

    public Ellipse() {
        super();
        centerX = new SimpleDoubleProperty(0);
        centerY = new SimpleDoubleProperty(0);
        radiusX = new SimpleDoubleProperty(0);
        radiusY = new SimpleDoubleProperty(0);

        addBoundsListenerEllipse();
        updateBounds();
    }

    public Ellipse(Double centerX, Double centerY, Double radiusX, Double radiusY) {
        super();
        if(radiusX < 0 || radiusY < 0){
            throw new RuntimeException("Radius must be >= 0");
        }
        this.centerX = new SimpleDoubleProperty(centerX);
        this.centerY = new SimpleDoubleProperty(centerY);
        this.radiusX = new SimpleDoubleProperty(radiusX);
        this.radiusY = new SimpleDoubleProperty(radiusY);

        addBoundsListenerEllipse();
        updateBounds();
    }

    private void addBoundsListenerEllipse(){
        this.centerX.addListener(this.new BoundsListener());
        this.centerY.addListener(this.new BoundsListener());
        this.radiusX.addListener(this.new BoundsListener());
        this.radiusY.addListener(this.new BoundsListener());
    }
    public DoubleProperty centerXProperty() {
        return centerX;
    }
    public DoubleProperty centerYProperty() {
        return centerY;
    }
    public DoubleProperty radiusXProperty() {
        return radiusX;
    }
    public DoubleProperty radiusYProperty() {
        return radiusY;
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
            out.writeDouble(this.centerX.get());
            out.writeDouble(this.centerY.get());
            out.writeDouble(this.radiusX.get());
            out.writeDouble(this.radiusY.get());
            out.writeUTF(this.fill.get().toString()); // Fill color
            out.writeUTF(this.stroke.get().toString()); // Stroke color
            //out.writeDouble(this.getStrokeWidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        try {
            this.centerX.set(in.readDouble());
            this.centerY.set(in.readDouble());
            this.radiusX.set(in.readDouble());
            this.radiusY.set(in.readDouble());
            this.fill.set((Color)Paint.valueOf(in.readUTF()));
            this.stroke.set((Color)Paint.valueOf(in.readUTF()));
            //this.setStrokeWidth(in.readDouble());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveShape(Point2D topLeftCorner){
        centerX.set(topLeftCorner.getX() + this.radiusX.get());
        centerY.set(topLeftCorner.getY() + this.radiusY.get());
    }

    @Override
    public boolean equals(Object obj) {
        Boolean isEqual = super.equals(obj);
        if (obj == this)
            return true;
        if (!(obj instanceof Ellipse))
            return false;
        Ellipse other = (Ellipse) obj;
        isEqual &= this.centerX.getValue().equals(other.centerX.getValue());
        isEqual &= this.centerY.getValue().equals(other.centerY.getValue());
        isEqual &= this.radiusX.getValue().equals(other.radiusX.getValue());
        isEqual &= this.radiusY.getValue().equals(other.radiusY.getValue());
        return isEqual;
    }

    @Override
    public void resizeShapeOffset(Point2D offset) {
        if(bounds.get().getMaxX() + offset.getX() < bounds.get().getMinX() ||
           bounds.get().getMaxY() + offset.getY() < bounds.get().getMinY()){
            throw new RuntimeException("Invalid resize: " + new Point2D(bounds.get().getMaxX(), bounds.get().getMaxY()) + " to " + new Point2D(bounds.get().getMaxX() + offset.getX(), bounds.get().getMaxY() + offset.getY()) + " Top left is: " + new Point2D(bounds.get().getMinX(),bounds.get().getMinY()));
        }
        centerX.set((bounds.get().getMinX() + bounds.get().getMaxX() + offset.getX())/2);
        centerY.set((bounds.get().getMinY() + bounds.get().getMaxY() + offset.getY())/2);
        radiusX.set((bounds.get().getWidth() + offset.getX())/2);
        radiusY.set((bounds.get().getHeight() + offset.getY())/2);
    }

    @Override
    protected void updateBounds() {
        Point2D topLeft = new Point2D(this.centerX.get() - this.radiusX.get(), this.centerY.get() - this.radiusY.get());
        Rectangle2D r = new Rectangle2D(topLeft.getX(), topLeft.getY(), radiusX.get()*2, radiusY.get()*2);
        this.bounds.set(r);
    }
    
    public ResizeGizmo getResizeGizmo() {
        if(this.resizeGizmo == null){
            this.resizeGizmo = new ResizeGizmo(this);
        }
        return this.resizeGizmo;
    }
    
    public Object clone() throws CloneNotSupportedException {
        Ellipse ellipse = new Ellipse(this.centerX.doubleValue(), this.centerY.doubleValue(), this.radiusX.doubleValue(), this.radiusY.doubleValue());
        ellipse.setStroke(this.stroke.get());
        ellipse.setFill(this.fill.get());
        ellipse.setOpacity(this.opacity.get());
    
        return ellipse;
    }
        
}