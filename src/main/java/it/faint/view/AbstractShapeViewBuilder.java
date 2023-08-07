package it.faint.view;

import it.faint.model.Shape;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public abstract class AbstractShapeViewBuilder implements ShapeViewBuilder{
    private Shape model;
    protected javafx.scene.shape.Shape result;

    public AbstractShapeViewBuilder(Shape model){
        this.model = model;
    }

    
    @Override
    public void bindViewToGenericShapeProperties() {
        result.opacityProperty().bind(model.opacityProperty());
        result.fillProperty().bind(model.fillProperty());
        result.strokeProperty().bind(model.strokeProperty());
        result.strokeWidthProperty().bind(model.strokeWidthProperty());
        result.mouseTransparentProperty().bind(model.mouseTransparentProperty());
        if(model.dashedStrokeProperty().getValue()){
            result.getStrokeDashArray().addAll(10.0,10.0,10.0);
        }
        model.dashedStrokeProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> dashedStroke, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    result.getStrokeDashArray().addAll(10.0,10.0,10.0);
                } else {
                    result.getStrokeDashArray().clear();
                }
            }
        });
    }
}
