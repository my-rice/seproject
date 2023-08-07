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

public class Rectangle extends AbstractShape{
    protected DoubleProperty x;
    protected DoubleProperty y;
    protected DoubleProperty width;
    protected DoubleProperty height;
    

    public Rectangle(){
        super();
        x = new SimpleDoubleProperty(0);
        y = new SimpleDoubleProperty(0);
        width = new SimpleDoubleProperty(0);
        height = new SimpleDoubleProperty(0);

        addBoundsListenerRectangle();
        updateBounds();
    }

    public Rectangle(double x, double y, double width, double height){
        super();
        this.x = new SimpleDoubleProperty(x);
        this.y = new SimpleDoubleProperty(y);
        this.width = new SimpleDoubleProperty(width);
        this.height = new SimpleDoubleProperty(height);

        addBoundsListenerRectangle();
        updateBounds();

    }

    private void addBoundsListenerRectangle(){
        this.x.addListener(this.new BoundsListener());
        this.y.addListener(this.new BoundsListener());
        this.height.addListener(this.new BoundsListener());
        this.width.addListener(this.new BoundsListener());
    }

    public DoubleProperty xProperty() {
        return x;
    }
    public DoubleProperty yProperty() {
        return y;
    }
    public DoubleProperty widthProperty() {
        return width;
    }
    public DoubleProperty heightProperty() {
        return height;
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
            out.writeDouble(this.x.get());
            out.writeDouble(this.y.get());
            out.writeDouble(this.width.get());
            out.writeDouble(this.height.get());
            out.writeUTF(this.fill.get().toString()); // Fill color
            out.writeUTF(this.stroke.get().toString());   // Stroke color
            //out.writeDouble(this.getStrokeWidth());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void moveShape(Point2D topLeftCorner){
        this.x.set(topLeftCorner.getX());
        this.y.set(topLeftCorner.getY());
    }
    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        try {
            this.x.set(in.readDouble());
            this.y.set(in.readDouble());
            this.width.set(in.readDouble());
            this.height.set(in.readDouble());
            this.fill.set((Color)Paint.valueOf(in.readUTF()));
            this.stroke.set((Color)Paint.valueOf(in.readUTF()));
            //this.setStrokeWidth(in.readDouble());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        Boolean isEqual = super.equals(obj);
        if (obj == this)
            return true;
        if (!(obj instanceof Rectangle))
            return false;
        Rectangle other = (Rectangle) obj;
        isEqual &= this.x.getValue().equals(other.x.getValue());
        isEqual &= this.y.getValue().equals(other.y.getValue());
        isEqual &= this.width.getValue().equals(other.width.getValue());
        isEqual &= this.height.getValue().equals(other.height.getValue());
        return isEqual;
    }

    @Override
    public void resizeShapeOffset(Point2D offset) {
        if(bounds.get().getMaxX() + offset.getX() < bounds.get().getMinX() ||
           bounds.get().getMaxY() + offset.getY() < bounds.get().getMinY()){
            throw new RuntimeException("Invalid resize: " + new Point2D(bounds.get().getMaxX(), bounds.get().getMaxY()) + " to " + new Point2D(bounds.get().getMaxX() + offset.getX(), bounds.get().getMaxY() + offset.getY()) + " Top left is: " + new Point2D(bounds.get().getMinX(),bounds.get().getMinY()));
        }
        width.set(width.get() + offset.getX());
        height.set(height.get() + offset.getY());
    }

    @Override
    protected void updateBounds() {
        Rectangle2D r = new Rectangle2D(this.x.get(), this.y.get(),this.width.get(), this.height.get());
        this.bounds.set(r); 
    }
    
    public ResizeGizmo getResizeGizmo() {
        if(this.resizeGizmo == null){
            this.resizeGizmo = new ResizeGizmo(this);
        }
        return this.resizeGizmo;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Rectangle rectangle=new Rectangle(this.x.get(), this.y.get(), this.width.get(), this.height.get());
        rectangle.setStroke(this.stroke.get());
        rectangle.setFill(this.fill.get());
        rectangle.setOpacity(this.opacity.get());
    
        return rectangle;
    }
    
}
