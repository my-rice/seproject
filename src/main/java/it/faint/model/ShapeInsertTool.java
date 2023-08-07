package it.faint.model;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;


public abstract class ShapeInsertTool extends AbstractTool{
    protected boolean isInserting;
    protected Rectangle2D boundingRectangle;
    protected Point2D secondPoint;
    private Shape preview;

    public ShapeInsertTool(DrawEditor editor) {
        super(editor);
        isInserting = false;
        preview = null;
    }

    public void handleMoved(Drawing.Event event){
        if(!isInserting)
            return;
        secondPoint = new Point2D(event.getX(),event.getY());
        updateBoundingRectangle();
        updatePreview();
    }

    public void handleSecondaryPressed(Drawing.Event event){
        this.isInserting=false;
        editor.getDrawing().clearPreviews();
        preview = null;
    }

    protected void updateBoundingRectangle(){
        double minX, minY, maxX, maxY;
        minX = Double.min(firstPoint.getX(), secondPoint.getX());
        minY = Double.min(firstPoint.getY(), secondPoint.getY());
        maxX = Double.max(firstPoint.getX(), secondPoint.getX());
        maxY = Double.max(firstPoint.getY(), secondPoint.getY());
        boundingRectangle = new Rectangle2D(minX, minY, maxX-minX, maxY-minY);
    }

    public void handlePrimaryPressed(Drawing.Event event){
        if(!isInserting){
            this.firstPoint = new Point2D(event.getX(), event.getY());
            this.isInserting = true;
        } else {
            secondPoint = new Point2D(event.getX(), event.getY());
            updateBoundingRectangle();
            this.insert();
            this.isInserting = false;
            editor.getDrawing().removePreview(preview);
            preview = null;
        }
    }

    private void insert(){
        updateBoundingRectangle();
        Shape s = getShape();
        s.setFill(editor.getSelectedFillColor());
        s.setStroke(editor.getSelectedBorderColor());
        this.editor.insertShapeRequest(s);
    }

    @Override
    public void updatePreview(){
        if(preview == null){
            Shape s=getShape();
            s.setOpacity(0.75);
            s.setFill(editor.getSelectedFillColor());
            s.setStroke(editor.getSelectedBorderColor());
            this.editor.getDrawing().setPreview(s);
            preview = s;
        }
        this.updateShape(preview);
    }

    public abstract Shape getShape();
    public abstract void updateShape(Shape shape);

}
