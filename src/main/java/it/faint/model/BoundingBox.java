package it.faint.model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public class BoundingBox extends Rectangle {
    public BoundingBox(Shape shape){
        super();
        Rectangle2D bounds = shape.boundsProperty().getValue();

        this.setStroke(Color.CORNFLOWERBLUE);
        this.setFill(Color.TRANSPARENT);
        this.dashedStroke.set(true);
        this.strokeWidth.set(3.0);
        this.mouseTransparent.set(true);

        x.set(bounds.getMinX());
        y.set(bounds.getMinY());
        width.set(bounds.getWidth());
        height.set(bounds.getHeight());

        shape.boundsProperty().addListener( new ChangeListener<Rectangle2D>() {

            @Override
            public void changed(ObservableValue<? extends Rectangle2D> observable, Rectangle2D oldValue, Rectangle2D newValue) {
                x.set(newValue.getMinX());
                y.set(newValue.getMinY());
                width.set(newValue.getWidth());
                height.set(newValue.getHeight());
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

}
