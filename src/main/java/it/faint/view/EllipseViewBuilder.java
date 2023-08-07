package it.faint.view;

import it.faint.model.Ellipse;

public class EllipseViewBuilder extends AbstractShapeViewBuilder{
    private Ellipse model;
    
    public EllipseViewBuilder(Ellipse model){
        super(model);
        this.model = model;
    }

    @Override
    public void reset() {
        result = new javafx.scene.shape.Ellipse();
    }

    @Override
    public void bindViewToSpecificShapeProperties() {
        javafx.scene.shape.Ellipse result = (javafx.scene.shape.Ellipse) this.result;
        result.centerXProperty().bind(model.centerXProperty());
        result.centerYProperty().bind(model.centerYProperty());
        result.radiusXProperty().bind(model.radiusXProperty());
        result.radiusYProperty().bind(model.radiusYProperty());
    }

    public javafx.scene.shape.Ellipse getResult(){
        return (javafx.scene.shape.Ellipse) result;
    }
}
