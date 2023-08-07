package it.faint.model;

import java.util.List;

import it.faint.model.Drawing.Event;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;


public class DrawEditor implements Drawing.Subscriber {
    private Drawing drawing;
    private Tool selectedTool;
    private ObjectProperty<Color> selectedFillColor;
    private ObjectProperty<Color> selectedBorderColor;
    private ObjectProperty<Shape> selectedShape;
    private CommandInvoker commandInvoker;
    
    public DrawEditor(Drawing drawing){
        this.drawing = drawing;
        this.drawing.subscribe(this);
        
        
        this.selectedShape = new SimpleObjectProperty<>(null);
        this.selectedShape.addListener(new SelectedShapeListener());
        
        this.commandInvoker = new CommandInvoker();

        this.selectedFillColor = new SimpleObjectProperty<>(Color.rgb(0, 0, 0));
        this.selectedBorderColor = new SimpleObjectProperty<>(Color.rgb(0, 0, 0));

        this.selectedTool = new CursorTool(this);
    }

    // Properties

    public ObjectProperty<Color> selectedFillColorProperty(){
        return selectedFillColor;
    }

    public ObjectProperty<Color> selectedBorderColorProperty(){
        return selectedBorderColor;
    }

    public ReadOnlyBooleanProperty hasUndoableCommandProperty(){
        return commandInvoker.hasUndoableCommandProperty();
    }
    public ReadOnlyBooleanProperty hasRedoableCommandProperty(){
        return commandInvoker.hasRedoableCommandProperty();
    }

    public ObjectProperty<Shape> selectedShapeProperty(){
        return this.selectedShape;
    }

    // Getters

    public Drawing getDrawing(){
        return drawing;
    }

    public Tool getSelectedTool() {
        return selectedTool;
    }

    public CommandInvoker getCommandInvoker() {
        return commandInvoker;
    }

    public Color getSelectedFillColor() {
        return selectedFillColor.get();
    }

    public Color getSelectedBorderColor() {
        return selectedBorderColor.get();
    }
    
    public List<Shape> getAllShapes(){
        return drawing.getAllShapes();
    }

    // Setters

    public void setTool(Tool tool){
        if(this.selectedTool != null){
            this.selectedTool.dismiss();
        } 
        this.selectedTool = tool;
    }

    public void setSelectedFillColor(Color selectedFillColor) {
        this.selectedFillColor.set(selectedFillColor);
    }    

    public void setSelectedBorderColor(Color selectedBorderColor) {
        this.selectedBorderColor.set(selectedBorderColor);
    }

    // Selected shape operations

    public void setSelectedShape(Shape s){
        this.selectedShape.setValue(s);
        
    }

    public void clearSelectedShape(){
        //Deselected the shape
        this.selectedShape.setValue(null); 
    }

    // Drawing shapes operations

    public void clearAllShapes(){
        clearSelectedShape();
        drawing.clearShapes();
    }

    public void insertAllShapes(List<Shape> list){
        drawing.insertAllShapes(list);
    }

    // Command pattern requests

    public void insertShapeRequest(Shape s) {
        commandInvoker.execute(new InsertCommand(drawing, s));
    }

    public void deleteSelectedShapeRequest() {
        if (selectedShape.isNull().getValue()){
            return;
        }
        commandInvoker.execute(new DeleteCommand(drawing, selectedShape.getValue()));
        this.clearSelectedShape();
    }

    public void toFrontSelectedShapeRequest(){
        if (selectedShape.isNull().getValue()){
            return;
        }
        int size = drawing.getAllShapes().size();
        int offset = size - (drawing.getAllShapes().indexOf(selectedShape.getValue()) + 1);
        commandInvoker.execute(new MoveForwardBackwardCommand(drawing, selectedShape.getValue(),offset));
        this.clearSelectedShape();
        
    }

    public void toBackSelectedShapeRequest(){
        if (selectedShape.isNull().getValue()){
            return;
        }
        int offset = -drawing.getAllShapes().indexOf(selectedShape.getValue());
        
        commandInvoker.execute(new MoveForwardBackwardCommand(drawing, selectedShape.getValue(),offset));
        this.clearSelectedShape();
                
    }

    public void toForwardSelectedShapeRequest(){
        if (selectedShape.isNull().getValue()){
            return;
        }
        commandInvoker.execute(new MoveForwardBackwardCommand(drawing, selectedShape.getValue(),1));
        this.clearSelectedShape();
    }

    public void toBackwardSelectedShapeRequest(){
        if (selectedShape.isNull().getValue()){
            return;
        }
        commandInvoker.execute(new MoveForwardBackwardCommand(drawing, selectedShape.getValue(),-1));
        this.clearSelectedShape();
    }

    public void changeSelectedShapeBorderColorRequest(Color oldColor, Color newColor){
        if (selectedShape.isNull().getValue()){
            return;
        }
        commandInvoker.execute(new ChangeBorderColorCommand(selectedShape.getValue(), oldColor, newColor));
    }

    public void changeSelectedShapeFillColorRequest(Color oldColor, Color newColor){
        if (selectedShape.isNull().getValue()){
            return;
        }
        commandInvoker.execute(new ChangeFillColorCommand(selectedShape.getValue(), oldColor, newColor));
    }

    public void moveSelectedShapeRequest(Point2D oldTopLeft,Point2D newTopLeft){
        if (selectedShape.isNull().getValue()){
            return;
        }
        commandInvoker.execute(new MoveCommand(selectedShape.getValue(), oldTopLeft, newTopLeft));
    }

    public void resizeSelectedShapeRequest(Point2D oldBottomRight,Point2D newBottomRight){
        if (selectedShape.isNull().getValue()){
            return;
        }
        commandInvoker.execute(new ResizeCommand(selectedShape.getValue(), oldBottomRight, newBottomRight));
    }

    public void undoLastCommandRequest(){
        commandInvoker.undoLast();
    }

    public void redoLastCommandRequest() {
        commandInvoker.redoLast();
    }
    // Drawing event handlers

    @Override
    public void handleDrawingEvent(Event e) {
        switch(e.getType()){
            case MOUSE_DRAGGED:
            handleMouseDragged(e);
                break;
            case MOUSE_MOVED:
            handleMouseMoved(e);
                break;
            case MOUSE_PRESSED:
            handleMousePressed(e);
                break;
            case MOUSE_RELEASED:
            handleMouseReleased(e);
                break;   
            case SHAPE_REMOVED:
            handleShapeRemoved(e);
            default:
                break;
        }
    }

    private void handleMouseMoved(Drawing.Event event){
        selectedTool.handleMoved(event);
    }
    private void handleMousePressed(Drawing.Event event){
        selectedTool.handlePressed(event);
    }
    private void handleMouseReleased(Drawing.Event event){
        selectedTool.handleReleased(event);
    }
    private void handleMouseDragged(Drawing.Event event){
        selectedTool.handleDragged(event);
    }

    private void handleShapeRemoved(Drawing.Event event){
        if(event.getTarget().equals(selectedShape.get())){
            clearSelectedShape();
        }
    }

    // Grid
    public void setGridVisible(boolean isVisible){
        if(isVisible){
            drawing.showGrid();
        } else {
            drawing.hideGrid();
        }
    }

    public void setGridSize(double size){
        drawing.setGridSize(size);
    }

    // Selected shape listener

    private class SelectedShapeListener implements ChangeListener<Shape>{
        private ShapeColorListener fillListener, strokeListener;
        @Override
        public void changed(ObservableValue<? extends Shape> selectedShape, Shape oldValue, Shape newValue) {
            if(oldValue != null){
                drawing.clearPreviews();
                //selectedFillColor.unbindBidirectional(oldValue.fillProperty());
                //selectedBorderColor.unbindBidirectional(oldValue.strokeProperty());
                if(fillListener != null){
                    oldValue.fillProperty().removeListener(fillListener);
                    fillListener = null;
                }
                if(strokeListener != null){
                    oldValue.strokeProperty().removeListener(strokeListener);
                    strokeListener = null;
                }
            }
            if(newValue != null){
                drawing.setPreview(newValue.getBoundingBox());
                drawing.addPreview(newValue.getResizeGizmo());
                setSelectedBorderColor(newValue.strokeProperty().getValue());
                setSelectedFillColor(newValue.fillProperty().getValue());
                //selectedFillColor.bindBidirectional(newValue.fillProperty());
                //selectedBorderColor.bindBidirectional(newValue.strokeProperty());
                fillListener = new ShapeColorListener(true);
                strokeListener = new ShapeColorListener(false);
                newValue.fillProperty().addListener(fillListener);
                newValue.strokeProperty().addListener(strokeListener);
            }
        }

        class ShapeColorListener implements ChangeListener<Color>{
            private boolean isFill;
            public ShapeColorListener(boolean isFill){
                this.isFill = isFill;
            }
            @Override
            public void changed(ObservableValue<? extends Color> color, Color oldValue, Color newValue) {
                if(isFill){
                    selectedFillColor.set(newValue);
                } else {
                    selectedBorderColor.set(newValue);
                }
            }
        }
    }

    public void copySelectedShapeRequest(){
        ClipboardManager.getIstance().fillClipboardManager(this.selectedShape.get());
    }

    public void cutSelectedShapeRequest(){
        ClipboardManager.getIstance().fillClipboardManager(this.selectedShape.get());
        this.deleteSelectedShapeRequest();
    }

    public void pasteSelecteShapeRequest(){
        Shape s= ClipboardManager.getIstance().getClipboardContent();
        if(s!=null)
            this.insertShapeRequest(s);
    }

}
