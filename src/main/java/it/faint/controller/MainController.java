package it.faint.controller;

import java.io.File;

import java.util.Optional;

import it.faint.model.ClipboardManager;
import it.faint.model.CursorTool;
import it.faint.model.DrawEditor;
import it.faint.model.EllipseTool;
import it.faint.model.FileManager;
import it.faint.model.LineTool;
import it.faint.model.RectangleTool;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.converter.NumberStringConverter;
import javafx.scene.paint.Color;

public class MainController {
    @FXML
    private Button deleteButton;
    @FXML
    private Button copyButton;
    @FXML
    private Button cutButton;
    @FXML
    private Button pasteButton;
    @FXML
    private ToggleButton cursorTool;
    @FXML
    private ToggleButton rectangleTool;
    @FXML
    private ToggleButton lineTool;
    @FXML
    private ToggleButton ellipseTool;
    @FXML
    private ColorPicker contourColorPicker;
    @FXML
    private ColorPicker fillColorPicker;
    @FXML
    private VBox clipboardGroup;
    @FXML
    private VBox historyGroup;
    @FXML
    private VBox layerGroup;
    @FXML
    private Pane canvas;
    @FXML
    private CanvasController canvasController;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private Button backwardButton;
    @FXML
    private Button forwardButton;
    @FXML
    private Button toFrontButton;
    @FXML
    private Button toBackButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private CheckMenuItem gridMenuItem;

    private DrawEditor editor;
    private FileManager fileManager;

    public MainController(){
    }

    //Initialize method is called after @FXML fields are populated.
    @FXML
    public void initialize() {
        ToggleGroup tg = new ToggleGroup();
        cursorTool.setToggleGroup(tg);
        rectangleTool.setToggleGroup(tg);
        lineTool.setToggleGroup(tg);
        ellipseTool.setToggleGroup(tg);

        cursorTool.setSelected(true);
        contourColorPicker.getStyleClass().add("button");
        fillColorPicker.getStyleClass().add("button");
        
        
        // Change the default behaviour of the colorPickers so they do nothing when mouse right-click occurs  
        contourColorPicker.addEventFilter(MouseEvent.MOUSE_CLICKED, ev -> {
            if (ev.getButton() == MouseButton.SECONDARY) {
                contourColorPicker.hide();
            }
        });
        fillColorPicker.addEventFilter(MouseEvent.MOUSE_CLICKED, ev -> {
            if (ev.getButton() == MouseButton.SECONDARY) {
                fillColorPicker.hide();
            }
        });
        
        
        // Set the default value of the contourColorPicker and fillColorPicker
        contourColorPicker.setValue(Color.BLACK);
        fillColorPicker.setValue(Color.GAINSBORO);
    }
    
    public void initModel(DrawEditor drawEditor){
        this.editor = drawEditor;
        canvasController.initModel(editor.getDrawing());
        // Set the default value of the contour and interior color of the shape
        editor.setSelectedBorderColor(contourColorPicker.getValue());
        editor.setSelectedFillColor(fillColorPicker.getValue());

        // Bind the color pickers to the editor attributes
        fillColorPicker.valueProperty().bindBidirectional(editor.selectedFillColorProperty());
        contourColorPicker.valueProperty().bindBidirectional(editor.selectedBorderColorProperty());
        
        fillColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(editor.selectedShapeProperty().isNull().get()){
                    return;
                }
                editor.changeSelectedShapeFillColorRequest(editor.selectedShapeProperty().get().fillProperty().get(), fillColorPicker.getValue());
            }
        });

        contourColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(editor.selectedShapeProperty().isNull().get()){
                    return;
                }
                editor.changeSelectedShapeBorderColorRequest(editor.selectedShapeProperty().get().strokeProperty().get(), contourColorPicker.getValue());
            }
        });

        deleteButton.disableProperty().bind(editor.selectedShapeProperty().isNull());
        // This bind is for disabling the copy button when no shape is selected
        copyButton.disableProperty().bind(editor.selectedShapeProperty().isNull());

        // This bind is for disabling the cut button when no shape is selected
        cutButton.disableProperty().bind(editor.selectedShapeProperty().isNull());

        // This bind is for disabling the paste button when the clipboard is empty
        pasteButton.disableProperty().bind(ClipboardManager.getIstance().getIsEmptyClipboard());      
        // Undo button is enabled if there are undoable commands
        undoButton.disableProperty().bind(Bindings.not(editor.hasUndoableCommandProperty()));
        // Redo button is enabled if there are redoable commands
        redoButton.disableProperty().bind(Bindings.not(editor.hasRedoableCommandProperty()));
        
        // This bind is for disabling all the moving shape forward and backward button when no shape is selected
        backwardButton.disableProperty().bind(editor.selectedShapeProperty().isNull());
        forwardButton.disableProperty().bind(editor.selectedShapeProperty().isNull());
        toFrontButton.disableProperty().bind(editor.selectedShapeProperty().isNull());
        toBackButton.disableProperty().bind(editor.selectedShapeProperty().isNull());
    }

    @FXML
    private void onLoad(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.faint"));
        File file = new FileChooser().showOpenDialog(null);
        if(file!=null){
            fileManager=new FileManager(file, editor.getDrawing());
            editor.clearAllShapes();
            fileManager.loadShapes();
        }
    }

    @FXML
    private void onSave(){  
        // If the drawing has never been saved before, then will be call onSaveAs in order to choose the name and location of the file to be saved
        if(fileManager==null){
            this.onSaveAs();
        }
        else{ //else save the drawing in the file
            fileManager.saveShapes();
        }
    }

    @FXML
    private void onSaveAs(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.faint"));
        File file = new FileChooser().showSaveDialog(null);
        if(file!=null){
            fileManager=new FileManager(file, editor.getDrawing());
            fileManager.saveShapes();
        }
    }

    @FXML
    private void onExit(){  // If the drawing is empty, the app is closed directly; else appear a message box to save or not the drawing
        if(editor.getAllShapes().isEmpty()){
            Platform.exit();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Current project is modified");
            alert.setHeaderText("You are closing the application");
            alert.setContentText("Save?");
            ButtonType okButton = new ButtonType("Yes", ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonData.NO);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(okButton, noButton, cancelButton);
            alert.showAndWait().ifPresent(type -> {
                if (type == okButton) { 
                    this.onSave();
                    Platform.exit();
                } 
                else if (type == noButton) {
                    Platform.exit();
                } 
                else {
                    // do nothing
                }
            });
        }
    }

    @FXML
    private void onCursorTool(){
        cursorTool.setSelected(true);
        this.editor.setTool(new CursorTool(editor));
    }

    @FXML
    private void onRectangleTool(){
        rectangleTool.setSelected(true);
        this.editor.setTool(new RectangleTool(editor));
    }

    @FXML
    private void onEllipseTool(){
        ellipseTool.setSelected(true);
        this.editor.setTool(new EllipseTool(editor));
    }

    @FXML
    private void onLineTool(){
        lineTool.setSelected(true);
        this.editor.setTool(new LineTool(editor));
    }

    @FXML
    private void onContourColorChange(){
        //Set the current border color in the system
        //editor.setSelectedBorderColor(contourColorPicker.getValue());
        //contourColorPicker.valueProperty()
    }

    @FXML
    private void onInnerColorChange(){
        //Set the current inner color in the system
        //editor.setSelectedFillColor(fillColorPicker.getValue());
    }

    @FXML
    private void onCopy(){
        editor.copySelectedShapeRequest();
    }

    @FXML
    private void onCut(){
        editor.cutSelectedShapeRequest();
    }

    @FXML
    private void onPaste(){
        editor.pasteSelecteShapeRequest();
    }

    @FXML
    private void onUndo(){
        editor.undoLastCommandRequest();
    }

    @FXML
    private void onRedo(){
        editor.redoLastCommandRequest();
    }

    @FXML
    private void onDelete(){
        editor.deleteSelectedShapeRequest();
    }
    
    @FXML
    private void handleGridToggle(){
        editor.setGridVisible(gridMenuItem.isSelected());
    }

    @FXML
    private void onToFront(){
        //
        editor.toFrontSelectedShapeRequest();
    }

    @FXML
    private void onToBack(){
        //
        editor.toBackSelectedShapeRequest();
    }
    @FXML
    private void onToForward(){
        //
        editor.toForwardSelectedShapeRequest();
    }

    @FXML
    private void onToBackward(){
        //
        editor.toBackwardSelectedShapeRequest();
    }

    @FXML
    private void onGridSettings(){
        DoubleProperty selectedGridSize = new SimpleDoubleProperty(editor.getDrawing().gridSizeProperty().getValue());
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Grid settings");
        dialog.setContentText("Grid size");
        dialog.getEditor().textProperty().bindBidirectional(selectedGridSize, new NumberStringConverter());
        dialog.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*\\.?\\d*")) {
                    dialog.getEditor().setText(newValue.replaceAll("[^\\d\\.]", ""));
                }
            }
        });
        dialog.getDialogPane().getChildren().remove(0);
        Optional<String> result = dialog.showAndWait();
        if(result.isPresent()){
            editor.setGridSize(selectedGridSize.get());
        }
    }
}
