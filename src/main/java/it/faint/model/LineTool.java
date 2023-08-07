package it.faint.model;

public class LineTool extends ShapeInsertTool{

    public LineTool(DrawEditor editor) {
        super(editor);
    }

    @Override
    public Shape getShape() {
        Shape s = new Line(firstPoint.getX(), firstPoint.getY(), secondPoint.getX(), secondPoint.getY());
        return s;
    }

    @Override
    public void updateShape(Shape shape) {
        if (! (shape instanceof Line) ){
            throw new java.lang.IllegalArgumentException("Argument must be a Line");
        }
        Line l = (Line) shape;
        l.startXProperty().set(firstPoint.getX());
        l.startYProperty().set(firstPoint.getY());
        l.endXProperty().set(secondPoint.getX());
        l.endYProperty().set(secondPoint.getY());
    }

}