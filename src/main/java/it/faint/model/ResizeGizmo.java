package it.faint.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class ResizeGizmo extends Circle{
    private Shape target;

    public ResizeGizmo(Shape shape){
        super();
        this.target = shape;
        Rectangle2D bounds = shape.boundsProperty().getValue();

        this.setStroke(Color.CORNFLOWERBLUE);
        this.setFill(Color.CORNFLOWERBLUE);
        this.strokeWidth.set(1.0);

        centerX.set(bounds.getMaxX());
        centerY.set(bounds.getMaxY());
        radius.set(7.5);

        shape.boundsProperty().addListener( new ChangeListener<Rectangle2D>() {

            @Override
            public void changed(ObservableValue<? extends Rectangle2D> observable, Rectangle2D oldValue, Rectangle2D newValue) {
                centerX.set(newValue.getMaxX());
                centerY.set(newValue.getMaxY());
            }
            
        });
    }

    @Override
    public BoundingBox getBoundingBox(){
        try {
            throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Shape getTarget(){
        return target;
    }

    @Override
    public void moveShape(Point2D topLeftCorner) {
        if(topLeftCorner.getX() <= target.boundsProperty().get().getMinX() ||
           topLeftCorner.getY() <= target.boundsProperty().get().getMinY()){
            return;
        }
        super.moveShape(topLeftCorner);
        target.resizeShape(new Point2D(this.centerX.get(), this.centerY.get()));
    }
    
}
