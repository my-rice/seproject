package it.faint.view;

public interface ShapeViewBuilder {
    public abstract void reset();
    public abstract void bindViewToGenericShapeProperties();
    public abstract void bindViewToSpecificShapeProperties();
    public abstract javafx.scene.shape.Shape getResult();
}
