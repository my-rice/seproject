package it.faint.model;

public interface Tool {
    public abstract void handleMoved(Drawing.Event event);
    public abstract void handleDragged(Drawing.Event event);
    public abstract void handlePressed(Drawing.Event event);
    public abstract void handleReleased(Drawing.Event event);
    public abstract void dismiss();
}
