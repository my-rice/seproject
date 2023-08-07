package it.faint.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

public class CursorTool extends AbstractTool{
    private Boolean isMoving;
    private Color prevSelectedFillColor;
    private Color prevSelectedContourColor;
    private BooleanProperty isSelected;
    private Point2D displacement;
    private Point2D prevBottomRight;

    public CursorTool(DrawEditor editor) {
        super(editor);
        isSelected= new SimpleBooleanProperty(false); 
        isSelected.bind(editor.selectedShapeProperty().isNotNull());
        prevSelectedContourColor = editor.getSelectedBorderColor();
        prevSelectedFillColor = editor.getSelectedFillColor();
        isMoving = false;
    }

    @Override
    public void handlePrimaryPressed(Drawing.Event event){
        Shape eventTarget = event.getTarget();
        if(eventTarget == null){ //The user clicked on a blank space on the canvas
            if(isSelected.getValue()){ //If a shape was previously selected..
                editor.clearSelectedShape(); //...un-select the shape
                //Reset the previous border and fill colors
                editor.setSelectedBorderColor(prevSelectedContourColor);
                editor.setSelectedFillColor(prevSelectedFillColor);
            }
            this.prevBottomRight=null; //Reset the prevBottomRight point
            return;
        }
        // A shape has been clicked

        // If no shape was selected before, save the previous colors in the color pickers before they are bound to the shape
        if(isSelected.not().getValue()){
            prevSelectedFillColor = editor.getSelectedFillColor();
            prevSelectedContourColor = editor.getSelectedBorderColor();
        }
        
        displacement = event.getLocalPoint();
        if(!(eventTarget instanceof ResizeGizmo)){ // We select the shape only if it is not a gizmo
            this.editor.setSelectedShape(eventTarget); 
        } else { //If we clicked a gizmo, we want to save the prevBottomRight of the shape
            ResizeGizmo gizmo = (ResizeGizmo) eventTarget;
            Rectangle2D bounds = gizmo.getTarget().boundsProperty().get();
            prevBottomRight = new Point2D(bounds.getMaxX(),bounds.getMaxY());
        }
        
        this.firstPoint = event.getPoint();
            
    }

    @Override
    public void handleReleased(Drawing.Event event){
        if(!isMoving){ //If no shape was moving, we ignore the event
            return;
        }
        this.isMoving = false;
        Shape eventTarget = event.getTarget();
        if(eventTarget == null){ //If event target is null, we ignore the event
            return;
        }

        //We calculate the selected shape's old and new top left point
        Point2D newTopLeft = new Point2D(eventTarget.boundsProperty().get().getMinX(),eventTarget.boundsProperty().get().getMinY());
        Point2D oldTopLeft = this.firstPoint.subtract(this.displacement);
        if(newTopLeft.equals(oldTopLeft)){ //We don't send a move/resize request if the shape didn't move
            return;
        }
        //If the event target is a gizmo, a resize request should be sent instead of a move request
        if(eventTarget instanceof ResizeGizmo){
            ResizeGizmo gizmo=(ResizeGizmo) eventTarget;  
            Point2D newBottomRight = new Point2D(gizmo.centerX.get(),gizmo.centerY.get());
            this.editor.resizeSelectedShapeRequest(prevBottomRight, newBottomRight);
        }
        else{ 
            this.editor.moveSelectedShapeRequest(oldTopLeft, newTopLeft);
        }
    }

    @Override
    public void handleSecondaryPressed(Drawing.Event event){
        //event.getTarget().resizeShapeOffset(new Point2D(20, 20));
    }
    @Override
    public void handleDragged(Drawing.Event event){
        if(event.getButton() != MouseButton.PRIMARY){
            return;
        }
        Shape selected = event.getTarget();
        if(selected != null){
            this.isMoving = true;
            selected.moveShape(event.getPoint().subtract(displacement));   
        }
    }

    @Override
    public void dismiss(){
        super.dismiss();
        editor.clearSelectedShape();
    }

    @Override
    public void updatePreview() {
        //Useless here
    }

}
