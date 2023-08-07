package it.faint.model;

import javafx.scene.paint.Color;

public class ChangeBorderColorCommand extends AbstractCommand{
    private Shape shape;
    private Color borderColor;
    private Color prevBorderColor;

    public ChangeBorderColorCommand(Shape shape, Color oldColor, Color newColor) {
        super();
        this.shape = shape;
        this.borderColor = newColor;
        this.prevBorderColor = oldColor; 
    }

    @Override
    public void execute() {
        super.execute();
        shape.setStroke(borderColor);
    }

    @Override
    public void undo() {
        super.undo();
        shape.setStroke(prevBorderColor);
    }
    
}
