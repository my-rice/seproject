package it.faint.model;

public class InsertCommand extends AbstractCommand{
    private Drawing reciever;
    private Shape shape;

    public InsertCommand(Drawing reciever,Shape shape){
        super();
        this.reciever = reciever;
        this.shape = shape;
    }

    @Override
    public void execute() {
        super.execute();
        this.reciever.addShape(this.shape);
    }

    @Override
    public void undo() {
        super.undo();
        this.reciever.removeShape(this.shape);
    }
    
}
