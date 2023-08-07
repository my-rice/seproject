package it.faint.model;

public class DeleteCommand extends AbstractCommand{
    private Drawing reciever;
    private Shape shape;
    private Integer index;

    public DeleteCommand(Drawing reciever, Shape shape) {
        super();
        this.reciever = reciever;
        this.shape = shape;
    }

    @Override
    public void execute() {
        super.execute();
        int index=reciever.removeShape(shape);
        if(index==-1)
            throw new RuntimeException("Deletion failed because the shape was not found");
        this.index = index;
    }

    @Override
    public void undo() {
        super.undo();
        reciever.addShape(index, shape);
    }
    
}
