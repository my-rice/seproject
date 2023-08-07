package it.faint.view;

import it.faint.model.Rectangle;

public class RectangleViewBuilder extends AbstractShapeViewBuilder{
    private Rectangle model;
    
    public RectangleViewBuilder(Rectangle model){
        super(model);
        this.model = model;
    }

    @Override
    public void reset() {
        result = new javafx.scene.shape.Rectangle();
    }

    @Override
    public void bindViewToSpecificShapeProperties() {
        javafx.scene.shape.Rectangle result = (javafx.scene.shape.Rectangle) this.result;
        result.xProperty().bind(model.xProperty());
        result.yProperty().bind(model.yProperty());
        result.widthProperty().bind(model.widthProperty());
        result.heightProperty().bind(model.heightProperty());
    }

    public javafx.scene.shape.Rectangle getResult(){
        return (javafx.scene.shape.Rectangle) result;
    }
    
}
