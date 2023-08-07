package it.faint.view;

import it.faint.model.Line;

public class LineViewBuilder extends AbstractShapeViewBuilder{
    private Line model;
    
    public LineViewBuilder(Line model){
        super(model);
        this.model = model;
    }

    @Override
    public void reset() {
        result = new javafx.scene.shape.Line();
    }

    @Override
    public void bindViewToSpecificShapeProperties() {
        javafx.scene.shape.Line result = (javafx.scene.shape.Line) this.result;
        result.startXProperty().bind(model.startXProperty());
        result.startYProperty().bind(model.startYProperty());
        result.endXProperty().bind(model.endXProperty());
        result.endYProperty().bind(model.endYProperty());
    }

    public javafx.scene.shape.Line getResult(){
        return (javafx.scene.shape.Line) result;
    }
}
