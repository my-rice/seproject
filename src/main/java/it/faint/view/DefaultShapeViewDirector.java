package it.faint.view;

public class DefaultShapeViewDirector {
    private ShapeViewBuilder builder;
    private static DefaultShapeViewDirector instance;

    private DefaultShapeViewDirector(){
    }

    public static DefaultShapeViewDirector getInstance(){
        if(instance == null){
            instance = new DefaultShapeViewDirector();
        }
        return instance;
    }

    public void changeBuilder(ShapeViewBuilder builder){
        this.builder = builder;
    }

    public void make(){
        if(builder == null){
            throw new RuntimeException("No builder passed to the director.");
        }
        builder.reset();
        //builder.bindModelToGenericViewProperties();
        builder.bindViewToGenericShapeProperties();
        builder.bindViewToSpecificShapeProperties();
    }
}
