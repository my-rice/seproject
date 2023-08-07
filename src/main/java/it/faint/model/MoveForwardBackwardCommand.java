package it.faint.model;


public class MoveForwardBackwardCommand extends AbstractCommand{
    private Drawing reciever;
    private Shape shape;
    private int nLevel;
    private int oldIndex;
    private int newIndex;

    public MoveForwardBackwardCommand(Drawing reciever, Shape shape,int nLevel) {
        this.reciever = reciever;
        this.shape = shape;
        this.nLevel = nLevel;
    }

    @Override
    public void execute() {
        super.execute();
        int index,size,increment=1;

        if(nLevel < 0){
            increment = -1;
        }

        this.oldIndex = reciever.getAllShapes().indexOf(shape);
        this.newIndex = this.oldIndex + nLevel;
        
        index = this.oldIndex;
  
        while(index != this.newIndex){
            size = reciever.getAllShapes().size();
            if( (index == 0 && increment<0) || (index == (size-1) && increment>=0) ){
                //If in this iteration involves the shape in the first level or the last, this action is not allowed. That shape cannot be swapped because does not exist another level.
                this.newIndex = index;
                System.out.println("if newIndex: " + newIndex + " oldIndex: "+oldIndex+" index: "+index + " nLevel: " + this.nLevel);
                return;
            }
            if(reciever.removeShape(shape) == -1){
                System.out.println("Error! Something bad happened");
                throw new RuntimeException();
            }

            reciever.addShape(index+increment, shape);
            index += increment;
            
        }
        System.out.println("newIndex: " + newIndex + " oldIndex: "+oldIndex+" index: "+index + " nLevel: " + this.nLevel);
        
    }

    @Override
    public void undo() {
        super.undo();
        reciever.removeShape(newIndex);
        reciever.addShape(oldIndex,shape);
    }
    
}


