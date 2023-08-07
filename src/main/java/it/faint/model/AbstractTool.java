package it.faint.model;

import javafx.geometry.Point2D;

public abstract class AbstractTool implements Tool {
    protected DrawEditor editor;
    protected Point2D firstPoint;
    

    public AbstractTool(DrawEditor editor) {
        this.editor = editor;
    }

    public void handlePressed(Drawing.Event event){
        switch(event.getButton()){
            case PRIMARY:
                handlePrimaryPressed(event);
                break;
            case SECONDARY:
                handleSecondaryPressed(event);
                break;
            default:
                break;
        }
    }
    public abstract void handlePrimaryPressed(Drawing.Event event);
    public abstract void handleSecondaryPressed(Drawing.Event event);

    public void handleReleased(Drawing.Event event){

    }

    public void handleDragged(Drawing.Event event){

    }

    @Override
    public void handleMoved(Drawing.Event event) {
        
    }

    public void dismiss(){
        editor.getDrawing().clearPreviews();
    }
    
    public abstract void updatePreview();

}
