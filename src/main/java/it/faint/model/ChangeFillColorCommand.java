package it.faint.model;
import javafx.scene.paint.Color;

public class ChangeFillColorCommand extends AbstractCommand{
    private Shape shape;
    private Color fillColor;
    private Color prevFillColor;

    public ChangeFillColorCommand(Shape shape, Color oldColor, Color newColor) {
        super();
        this.shape = shape;
        this.fillColor = newColor;
        this.prevFillColor = oldColor;
    }

    @Override
    public void execute() {
        super.execute();
        shape.setFill(fillColor);
    }

    @Override
    public void undo() {
        super.undo();
        shape.setFill(prevFillColor);
    }
    
}
