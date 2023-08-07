package it.faint.model;

import javafx.geometry.Point2D;

public class ResizeCommand extends AbstractCommand{
    private Shape shape;
    private Point2D oldBottomRightPoint;
    private Point2D newBottomRightPoint;

    public ResizeCommand(Shape shape, Point2D oldBottomRightPoint, Point2D newBottomRightPoint) {
        super();
        this.shape = shape;
        this.oldBottomRightPoint = oldBottomRightPoint;
        this.newBottomRightPoint = newBottomRightPoint;
    }

    @Override
    public void execute() {
        super.execute();
        shape.resizeShape(newBottomRightPoint);
    }

    @Override
    public void undo() {
        super.undo();
        shape.resizeShape(oldBottomRightPoint);
    }
    
}
