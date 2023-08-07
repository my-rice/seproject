package it.faint.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class Circle extends Ellipse {
    protected DoubleProperty radius;

    public Circle(){
        super();
        this.radius=new SimpleDoubleProperty(0);
        this.radiusX.bind(this.radius);
        this.radiusY.bind(this.radius);
    }

    public Circle(Double centerX, Double centerY, Double radius){
        super(centerX, centerY, radius, radius);
        this.radius=new SimpleDoubleProperty(radius);
        this.radiusX.bind(this.radius);
        this.radiusY.bind(this.radius);
    }

    public DoubleProperty radiusProperty() {
        return this.radius;
    }
}
