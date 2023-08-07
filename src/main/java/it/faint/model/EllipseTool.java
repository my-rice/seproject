package it.faint.model;

public class EllipseTool extends ShapeInsertTool{
    
    public EllipseTool(DrawEditor editor) {
        super(editor);
    }

    @Override
    public Shape getShape() {
        Shape s = new Ellipse( ((boundingRectangle.getMaxX() - boundingRectangle.getMinX() )/2)+ boundingRectangle.getMinX() ,((boundingRectangle.getMaxY() - boundingRectangle.getMinY() )/2)+ boundingRectangle.getMinY() , (boundingRectangle.getMaxX() - boundingRectangle.getMinX() )/2 , (boundingRectangle.getMaxY() - boundingRectangle.getMinY() )/2 );
        return s;
    }

    @Override
    public void updateShape(Shape shape) {
        if (! (shape instanceof Ellipse) ){
            throw new java.lang.IllegalArgumentException("Argument must be a Ellipse");
        }
        Ellipse e= (Ellipse) shape;
        e.centerXProperty().set( ((boundingRectangle.getMaxX() - boundingRectangle.getMinX() )/2)+ boundingRectangle.getMinX());
        e.centerYProperty().set(((boundingRectangle.getMaxY() - boundingRectangle.getMinY() )/2)+ boundingRectangle.getMinY());
        e.radiusXProperty().set((boundingRectangle.getMaxX() - boundingRectangle.getMinX() )/2);
        e.radiusYProperty().set((boundingRectangle.getMaxY() - boundingRectangle.getMinY() )/2 );
    }

}