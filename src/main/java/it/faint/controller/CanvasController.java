package it.faint.controller;

import java.util.List;

import it.faint.model.Drawing;
import it.faint.model.Ellipse;
import it.faint.model.Line;
import it.faint.model.Rectangle;
import it.faint.model.Shape;
import it.faint.view.AbstractShapeViewBuilder;
import it.faint.view.DefaultShapeViewDirector;
import it.faint.view.EllipseViewBuilder;
import it.faint.view.LineViewBuilder;
import it.faint.view.RectangleViewBuilder;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class CanvasController {
    @FXML
    private Pane canvas;
    @FXML
    private Canvas grid;

    private List<Node> children;
    private Drawing drawing;
    private static int previewPivot = 0;
    private Scale scale;
    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = 1;

    public CanvasController() {
        scale = new Scale();
    }

    @FXML
    public void initialize() {
        canvas.setOnMousePressed(this::handleMousePressed);
        canvas.setOnMouseReleased(this::handleMouseReleased);
        canvas.setOnMouseDragged(this::handleMouseDragged);
        canvas.setOnMouseMoved(this::handleMouseMoved);
        canvas.setOnScroll(this::handleMouseScrolled);
        children = canvas.getChildren();

        canvas.getParent().getTransforms().add(scale);
    }
    
    public void initModel(Drawing drawing){
        this.drawing = drawing;
        canvas.prefWidthProperty().bind(drawing.widthProperty());
        canvas.prefHeightProperty().bind(drawing.heightProperty());
        canvas.clipProperty().set(new javafx.scene.shape.Rectangle(0,0,drawing.widthProperty().get(), drawing.heightProperty().get()));
        canvas.prefWidthProperty().addListener((prefWidth, oldValue, newValue) -> canvas.clipProperty().set(new javafx.scene.shape.Rectangle(0,0,newValue.doubleValue(),drawing.heightProperty().get())));
        canvas.prefHeightProperty().addListener((prefWidth, oldValue, newValue) -> canvas.clipProperty().set(new javafx.scene.shape.Rectangle(0,0,drawing.widthProperty().get(),newValue.doubleValue())));
        drawing.addShapesListener(new ShapesListChangeListener(false));
        drawing.addPreviewListener(new ShapesListChangeListener(true));
        drawing.isGridVisibleProperty().addListener((a,b,c)->redrawGrid());
        drawing.gridSizeProperty().addListener((a,b,c)->redrawGrid());
    }

    @FXML
    private void redrawGrid(){
        if(drawing == null){
            return;
        }
        GraphicsContext gc=grid.getGraphicsContext2D();
        double w=grid.getWidth();
        double h=grid.getHeight();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, w, h);
        if(!drawing.isGridVisibleProperty().get()){
            return;
        }
        Double steps = drawing.gridSizeProperty().get();
        gc.setStroke(Color.GAINSBORO);
        // Make vertical lines
        for(double i=0; i < w; i+=steps){
            gc.strokeLine(i, 0, i, h);
        }

        // Make horizontal lines
        for(double i=0; i < h; i+=steps){
            gc.strokeLine(0, i, w, i);
        }

    }

    private void handleMouseEvent(InputEvent event, Drawing.EventType type) {
        Drawing.Event e = null;
        // Check if mouse is inside bounds
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            Point2D point = new Point2D(mouseEvent.getX(), mouseEvent.getY());
            if (!canvas.boundsInParentProperty().get().contains(point)) {
                return;
            }
            if (event.getTarget().equals(canvas)) { // || event.getTarget().equals(previewView)){ //Canvas must not be attached to events
                // System.out.println("Event without target generated");
                // System.out.println("Event without target generated: type=" + type + " event=" + event + " point=" + point);
                e = drawing.new Event(type, mouseEvent.getButton(), point);
            } else {
                // System.out.println(event.getTarget());
                if (!(event.getTarget() instanceof javafx.scene.shape.Shape)) {
                    return;
                }
                javafx.scene.shape.Shape targetView = (javafx.scene.shape.Shape) event.getTarget();
                int index = children.indexOf(targetView);
                // System.out.println(index);
                Shape targetModel;
                if (index < previewPivot) { // event target is not a preview
                    targetModel = drawing.getShape(index);
                    // System.out.println("Shape selected");
                } else { // event target is a preview
                    targetModel = drawing.getPreview(index - previewPivot);
                    // System.out.println(targetModel);
                }
                // System.out.println("Target: " + targetView+ "bounds: " +
                // targetView.boundsInParentProperty().get().getMinX());
                e = drawing.new Event(type, mouseEvent.getButton(), point, targetModel, point.subtract(targetView.boundsInParentProperty().get().getMinX(), targetView.boundsInParentProperty().get().getMinY()));
            }
           
        } else {
            //System.out.println("Scroll");
            ScrollEvent scrollEvent = (ScrollEvent) event;
            
            double s = scale.getX(); // currently we only use Y, same value is used for X
            s *= Math.pow(1.01, scrollEvent.getDeltaY()/10.0);

            if (s <= MIN_SCALE) {
                s = MIN_SCALE;
            } else if (s >= MAX_SCALE) {
                s = MAX_SCALE;
            }
            
            //if(sEvent.isControlDown() && (scale.getX()+sEvent.getDeltaY()/1000.0)>=1 && (scale.getY()+sEvent.getDeltaY()/1000.0)>=1)
            if(scrollEvent.isControlDown()){     //The user can zoom only with CTRL+scroll up/down (e non può dezoomare più della grandezza del canvas)
                scale.setX(s);
                scale.setY(s);
                // scale.setPivotX(sEvent.getX());
                // scale.setPivotY(sEvent.getY()); 
            }
            e = drawing.new Event(type, MouseButton.MIDDLE, new Point2D(scrollEvent.getDeltaX(), scrollEvent.getDeltaY()));
        }
        drawing.fireEvent(e);
    }

    public void handleMousePressed(MouseEvent event){
        handleMouseEvent(event, Drawing.EventType.MOUSE_PRESSED);
    }

    public void handleMouseReleased(MouseEvent event){
        handleMouseEvent(event, Drawing.EventType.MOUSE_RELEASED);
    }

    public void handleMouseDragged(MouseEvent event){
        handleMouseEvent(event, Drawing.EventType.MOUSE_DRAGGED);
    }

    public void handleMouseMoved(MouseEvent event){
        handleMouseEvent(event, Drawing.EventType.MOUSE_MOVED);
    }

    public void handleMouseScrolled(ScrollEvent event) {
        handleMouseEvent(event, Drawing.EventType.MOUSE_SCROLLED);
    }

    private class ShapesListChangeListener implements ListChangeListener<Shape> {
        private boolean isPreview;

        ShapesListChangeListener(boolean isPreview) {
            this.isPreview = isPreview;
        }

        private void handleRemoved(Change<? extends Shape> c) {
            for (int i = c.getFrom() + c.getRemovedSize() - 1; i >= c.getFrom(); i--) {
                if (isPreview) {
                    children.remove(i + previewPivot);
                } else {
                    children.remove(i);
                    previewPivot--;
                }
            }
        }

        private void handleAdded(Change<? extends Shape> c) {
            DefaultShapeViewDirector director = DefaultShapeViewDirector.getInstance();
            for (int i = c.getFrom(); i < c.getTo(); i++) {
                Shape s = isPreview ? drawing.getPreview(i) : drawing.getShape(i);
                AbstractShapeViewBuilder builder;
                if (s instanceof Rectangle) {
                    Rectangle model = (Rectangle) s;
                    builder = new RectangleViewBuilder(model);
                } else if (s instanceof Ellipse) {
                    Ellipse model = (Ellipse) s;
                    builder = new EllipseViewBuilder(model);
                } else if (s instanceof Line) {
                    Line model = (Line) s;
                    builder = new LineViewBuilder(model);
                } else {
                    throw new RuntimeException("Shape not supported");
                }
                director.changeBuilder(builder);
                director.make();
                if (isPreview) {
                    children.add(i + previewPivot, builder.getResult());
                } else {
                    children.add(i, builder.getResult());
                    previewPivot++;
                }
            }
        }

        @Override
        public void onChanged(Change<? extends Shape> c) {
            while (c.next()) {
                if (c.wasPermutated()) {
                    throw new UnsupportedOperationException("Permutations not yet supported");
                } else if (c.wasUpdated()) {
                    throw new UnsupportedOperationException("Updates not yet supported");
                } else { // Add and/or remove operations performed
                    if (c.wasRemoved() && !c.wasAdded()) {
                        handleRemoved(c);
                    } else if (c.wasRemoved()) {
                        throw new UnsupportedOperationException("Simultaneous remove and add not yet supported");
                    }
                    if (c.wasAdded()) {
                        handleAdded(c);
                    }
                }
            }
        }
    }
}
