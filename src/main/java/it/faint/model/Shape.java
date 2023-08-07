package it.faint.model;

import java.io.Externalizable;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.paint.Color;

public interface Shape extends Externalizable, Cloneable{
    public abstract BoundingBox getBoundingBox();
    public abstract ResizeGizmo getResizeGizmo();
    public abstract void moveShape(Point2D topLeftCorner);
    public abstract void resizeShape(Point2D bottomRightCorner);
    public abstract void resizeShapeOffset(Point2D offset);
    
    public abstract ObjectProperty<Rectangle2D> boundsProperty();
    public abstract DoubleProperty strokeWidthProperty();
    public abstract BooleanProperty dashedStrokeProperty();
    public abstract ObjectProperty<Color> fillProperty();
    public abstract ObjectProperty<Color> strokeProperty();
    public abstract DoubleProperty opacityProperty();
    public abstract BooleanProperty mouseTransparentProperty();

    public abstract void setFill(Color c);
    public abstract void setStroke(Color c);
    public abstract void setOpacity(double opacity);
    public Object clone() throws CloneNotSupportedException;
    
}
