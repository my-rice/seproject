package it.faint.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public abstract class AbstractShape implements Shape {
    protected DoubleProperty opacity;
    protected ObjectProperty<Color> fill;
    protected ObjectProperty<Color> stroke;
    protected BooleanProperty dashedStroke;
    protected DoubleProperty strokeWidth;
    protected ObjectProperty<Rectangle2D> bounds;
    protected BoundingBox boundingBox;
    protected ResizeGizmo resizeGizmo;
    protected BooleanProperty mouseTransparent; 

    public AbstractShape(){
        opacity = new SimpleDoubleProperty(1);
        fill = new SimpleObjectProperty<>(Color.rgb(0, 0, 0));
        stroke = new SimpleObjectProperty<>(Color.rgb(0, 0, 0));
        strokeWidth = new SimpleDoubleProperty(1.0);
        dashedStroke = new SimpleBooleanProperty(false);
        bounds = new SimpleObjectProperty<>(null);
        this.mouseTransparent = new SimpleBooleanProperty(false);
    }

    public ObjectProperty<Color> fillProperty(){
        return fill;
    }
    public ObjectProperty<Color> strokeProperty(){
        return stroke;
    }
    public DoubleProperty strokeWidthProperty(){
        return strokeWidth;
    }
    public BooleanProperty dashedStrokeProperty(){
        return dashedStroke;
    }
    public DoubleProperty opacityProperty(){
        return opacity;
    }
    public ObjectProperty<Rectangle2D> boundsProperty(){
        return bounds;
    }
    @Override
    public BooleanProperty mouseTransparentProperty(){
        return this.mouseTransparent;
    }

    public void setFill(Color fill){
        this.fill.set(fill);
    }
    public void setStroke(Color stroke){
        this.stroke.set(stroke);
    }
    public void setOpacity(double opacity){
        this.opacity.set(opacity);
    }

    @Override
    public boolean equals(Object obj) {
        Boolean isEqual = true;
        if (obj == this)
            return true;
        if (!(obj instanceof AbstractShape))
            return false;
        AbstractShape other = (AbstractShape) obj;
        isEqual &= this.opacity.getValue().equals(other.opacity.getValue());
        isEqual &= this.fill.getValue().equals(other.fill.getValue());
        isEqual &= this.stroke.getValue().equals(other.stroke.getValue());
        isEqual &= this.dashedStroke.getValue().equals(other.dashedStroke.getValue());
        isEqual &= this.strokeWidth.getValue().equals(other.strokeWidth.getValue());
        return isEqual;
    }

    @Override
    public void resizeShape(Point2D bottomRightCorner) {
        Point2D prevBottomRight = new Point2D(bounds.get().getMaxX(), bounds.get().getMaxY());
        Point2D offset = bottomRightCorner.subtract(prevBottomRight);
        resizeShapeOffset(offset);
    }

    protected abstract void updateBounds();

    protected class BoundsListener implements ChangeListener<Object>{
        @Override
        public void changed(ObservableValue<? extends Object> arg0, Object arg1, Object arg2) {
            AbstractShape.this.updateBounds();
        }
    }
    public abstract Object clone() throws CloneNotSupportedException;
}
