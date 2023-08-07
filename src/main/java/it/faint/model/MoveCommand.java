package it.faint.model;

import javafx.geometry.Point2D;

public class MoveCommand extends AbstractCommand{
    
    private Shape shape;
    private Point2D oldTopLeftPoint;
    private Point2D newTopLeftPoint;

    public MoveCommand(Shape selectedShape, Point2D oldTopLeftPoint, Point2D newTopLeftPoint) {
        super();
        this.shape = selectedShape;
        this.oldTopLeftPoint = oldTopLeftPoint;
        this.newTopLeftPoint = newTopLeftPoint;
    }

    @Override
    public void execute() {
        super.execute();
        shape.moveShape(newTopLeftPoint);
    }

    @Override
    public void undo() {
        super.undo();
        shape.moveShape(oldTopLeftPoint);
    }
    
}
