package it.faint.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseButton;

public class Drawing {
    private ObservableList<Shape> shapes;
    private ObservableList<Shape> previews;
    private List<Subscriber> subscribers;
    private DoubleProperty width;
    private DoubleProperty height;
    private BooleanProperty isGridVisible;
    private DoubleProperty gridSize;
    
    public Drawing(){
        width = new SimpleDoubleProperty(1200);
        height = new SimpleDoubleProperty(600);
        shapes = FXCollections.observableArrayList();
        previews = FXCollections.observableArrayList();
        subscribers = new ArrayList<>();
        isGridVisible = new SimpleBooleanProperty(false);
        gridSize = new SimpleDoubleProperty(30.0);
    }

    public ReadOnlyDoubleProperty widthProperty(){
        return DoubleProperty.readOnlyDoubleProperty(width);
    }

    public ReadOnlyDoubleProperty heightProperty(){
        return DoubleProperty.readOnlyDoubleProperty(height);
    }

    public ReadOnlyBooleanProperty isGridVisibleProperty(){
        return ReadOnlyBooleanProperty.readOnlyBooleanProperty(isGridVisible);
    }

    public ReadOnlyDoubleProperty gridSizeProperty(){
        return ReadOnlyDoubleProperty.readOnlyDoubleProperty(gridSize);
    }

    public void showGrid(){
        isGridVisible.set(true);
    }

    public void hideGrid(){
        isGridVisible.set(false);
    }

    public void setGridSize(double size){
        gridSize.set(size);
    }

    public int addShape(Shape s){
        shapes.add(s);
        return shapes.size();
    }

    public int addShape(int index, Shape s){
        shapes.add(index, s);
        return index;
    }

    public int removeShape(Shape s){
        int index = shapes.indexOf(s);
        if(index != -1){
            shapes.remove(index);
            fireEvent(new Event(EventType.SHAPE_REMOVED, null, null, s, null));
        }
        return index;
    }

    public Shape removeShape(int index){
        Shape s = shapes.remove(index);
        if(s != null){
            fireEvent(new Event(EventType.SHAPE_REMOVED, null, null, s, null));
        }
        return s;
    }

    public Shape getShape(int index){
        if(index < shapes.size()){
            return shapes.get(index);
        } else {
            return null;
        }
    }

    public int getShapeIndex(Shape shape){
        return shapes.indexOf(shape);
    }

    public void addShapesListener(ListChangeListener<Shape> listener){
        shapes.addListener(listener);
    }

    public List<Shape> getAllShapes(){
        return new ArrayList<>(shapes);
    }

    public void insertAllShapes(List<? extends Shape> l){
        shapes.addAll(l);
    }

    public void clearShapes(){
        shapes.clear();
    }

    public void setPreview(Shape preview){
        this.previews.clear();
        this.previews.add(preview);
        //this.preview.set(null);
        //this.preview.set(preview);
    }

    public void addPreview(Shape preview){
        this.previews.add(preview);
    }

    public int removePreview(Shape s){
        int index = previews.indexOf(s);
        if(index != -1){
            previews.remove(index);
        }
        return index;
    }

    public List<Shape> getAllPreviews(){
        return new ArrayList<>(previews);
        //return this.preview.get();
    }

    public Shape getPreview(int index){
        if(index < previews.size()){
            return previews.get(index);
        } else {
            return null;
        }
    }
    
    public void clearPreviews(){
        this.previews.clear();
    }

    public void addPreviewListener(ListChangeListener<Shape> listener){
        this.previews.addListener(listener);
    }

    public void subscribe(Subscriber s){
        subscribers.add(s);
    }

    public void unsubscribe(Subscriber s){
        subscribers.remove(s);
    }

    public void fireEvent(Event e){
        for(Subscriber s: subscribers){
            s.handleDrawingEvent(e);
        }
    }

    public enum EventType {
        MOUSE_PRESSED,
        MOUSE_RELEASED,
        MOUSE_MOVED,
        MOUSE_DRAGGED,
        SHAPE_REMOVED,
        MOUSE_SCROLLED
    };

    public class Event{
        private Point2D point;
        private Shape target;
        private MouseButton button;
        private EventType type;
        private Point2D localPoint;

        public Event(EventType type, MouseButton button, Point2D point){
            this.type = type;
            this.button = button;
            this.point = point;
        }
        public Event(EventType type, MouseButton button,Point2D point, Shape s,Point2D localPoint){
            this.type = type;
            this.button = button;
            this.point = point;
            this.target = s;
            this.localPoint = localPoint;
        }

        public Point2D getPoint(){
            return point;
        }
        public Shape getTarget(){
            return target;
        }
        public double getX(){
            return point.getX();
        }
        public double getY(){
            return point.getY();
        }
        public MouseButton getButton(){
            return button;
        }
        public EventType getType(){
            return type;
        }
        public Point2D getLocalPoint(){
            return localPoint;
        }
    }

    public interface Subscriber{
        public abstract void handleDrawingEvent(Event e);
    }
}
