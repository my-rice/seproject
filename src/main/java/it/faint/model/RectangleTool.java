package it.faint.model;

public class RectangleTool extends ShapeInsertTool{

    public RectangleTool(DrawEditor editor) {
        super(editor);
    }

    @Override
    public Shape getShape() {
        Shape s = new Rectangle(boundingRectangle.getMinX(), boundingRectangle.getMinY(), boundingRectangle.getWidth(), boundingRectangle.getHeight());
        return s;
    }

    @Override
    public void updateShape(Shape shape) {
        if (! (shape instanceof Rectangle) ){
            throw new java.lang.IllegalArgumentException("Argument must be a Rectangle");
        }
        Rectangle r = (Rectangle) shape;
        r.xProperty().set(boundingRectangle.getMinX());
        r.yProperty().set(boundingRectangle.getMinY());
        r.widthProperty().set(boundingRectangle.getWidth());
        r.heightProperty().set(boundingRectangle.getHeight());
    }

}
