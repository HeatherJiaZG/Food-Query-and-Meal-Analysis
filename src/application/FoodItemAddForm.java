/**
 * Filename: FoodItemAddForm.java 
 * 
 * Project: p5 
 * 
 * Course: cs400 
 * 
 * Authors: Alex Yang lyang298@wisc.edu 
 * Libin Zhou lzhou228@wisc.edu
 * Yao Yao yyao69@wisc.edu
 * York Li yli875@wisc.edu
 * Heather Jia yjia42@wisc.edu
 * 
 * Due Date: 12/12/2018
 *
 * Additional credits: N/A
 *
 * Bugs or other notes: no known bugs
 */

package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * class that lets user manually add food item
 * 
 * @param user input
 */
public class FoodItemAddForm implements NodeWrapperADT{
	//fields
    private FoodItem newFood;
    private Stage addStage;
    private FoodDataADT foodData;
    private FoodListView foodList;
    
    /**
     * input stage that prompts for input
     * 
     * @param food data, food list, stage
     */
    public FoodItemAddForm(Stage addStage, FoodDataADT foodData, FoodListView foodList) {
        this.addStage = addStage;
        addStage.setTitle("Add Food");
        this.foodData = foodData;
        this.foodList = foodList;
    }

    /**
     * sets up the window for user to manually add food item
     * 
     * @param food item that is being added
     */
    public BorderPane setUpAddWindow(ObservableList<FoodItem> selectedFoodItems,Label counterLabel) {
        BorderPane root = new BorderPane();
        HBox idBox = new HBox();
        //format of food ID input
        Label idLabel = new Label("ID:          ");
        idBox.setSpacing(27);
        TextField idField = new TextField("Please Enter a unique ID");
        idBox.getChildren().addAll(idLabel,idField);
        //format of food name input
        HBox nameBox = new HBox();
        Label nameLabel = new Label("Food Name: ");
        TextField nameField = new TextField("Please Enter a name");
        nameBox.getChildren().addAll(nameLabel,nameField);
        //format of calories input
        HBox caloriesBox = new HBox();
        Label caloriesLabel = new Label("Calories (g):      ");
        caloriesBox.setSpacing(14);
        TextField caloriesField = new TextField(""); 
        caloriesBox.getChildren().addAll(caloriesLabel,caloriesField);
        //format of fat input
        HBox fatBox = new HBox();
        Label fatLabel = new Label("Fat (g):           ");
        fatBox.setSpacing(26);
        TextField fatField = new TextField("");
        fatBox.getChildren().addAll(fatLabel,fatField);
        //format of carbohydrate input
        HBox carboHBox = new HBox();
        Label carboHLabel = new Label("Carbohydrate (g): ");
        TextField carboHField = new TextField("");
        carboHBox.getChildren().addAll(carboHLabel,carboHField);
        //format of fiber input
        HBox fiberBox = new HBox();
        Label fiberLabel = new Label("Fiber (g):         ");
        fiberBox.setSpacing(23);
        TextField fiberField = new TextField("");
        fiberBox.getChildren().addAll(fiberLabel,fiberField);
        //format of protein input
        HBox proteinBox = new HBox();
        Label proteinLabel = new Label("Protein (g):       ");
        proteinBox.setSpacing(18);
        TextField proteinField = new TextField("");
        proteinBox.getChildren().addAll(proteinLabel,proteinField);
        //final submission
        Button ok = new Button("Submit");
        HBox padding1 = new HBox(); HBox padding3 = new HBox();
        HBox padding2 = new HBox(); HBox padding4 = new HBox();
        //update after submission
        ok.setOnAction(e -> {
        	//can't be empty
        	if(!idField.getText().equals("")&&!nameField.getText().equals("")
        			&&!caloriesField.getText().equals("")&&!fatField.getText().equals("")
        			&&!carboHField.getText().equals("")&&!fiberField.getText().equals("")
        			&&!proteinField.getText().equals("")) {
        		//nutrient must be int value
        		if(isNumber(caloriesField.getText())&&isNumber(fatField.getText())
        				&&isNumber(carboHField.getText())&&isNumber(fiberField.getText())
        						&&isNumber(proteinField.getText())) {
        			selectedFoodItems.clear();
        			//put the new food into current list
        			this.foodList.foodListView.getSelectionModel().clearSelection();
        				//format the new food to match others
        				newFood = new FoodItem(idField.getText(),nameField.getText());
        				newFood.addNutrient("calories", Double.parseDouble(caloriesField.getText()));
        				newFood.addNutrient("fat", Double.parseDouble(fatField.getText()));
        				newFood.addNutrient("carbohydrate", Double.parseDouble(carboHField.getText()));
        				newFood.addNutrient("fiber", Double.parseDouble(fiberField.getText()));
        				newFood.addNutrient("protein", Double.parseDouble(proteinField.getText()));
        				foodData.addFoodItem(newFood);
        				//add new food
        				foodList.foodListView.getItems().add(newFood);
        				foodList.sortFoodList();
        				counterLabel.setText("Total food count: "+ foodList.foodListView.getItems().size());
        				//close the window
        				addStage.close();
        		}
        		//if not desired format, display warning and doesn't take input
        		else {
        			Alert alert = new Alert(AlertType.INFORMATION);
            		alert.setTitle("Incorrect Input");
            		alert.setContentText("The input of nutrients must be a non-negative number");
            		alert.showAndWait();
        		}
            }
        	//if not desired format, display warning and doesn't take input
        	else {
        		Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Incorrect Input");
        		alert.setContentText("The input cannot be blank!");
        		alert.showAndWait();
        	}
        });
        //these are just setup numbers for the window
        VBox vbox1 = new VBox();
        vbox1.getChildren().addAll(padding1,padding3,idBox,nameBox);
        vbox1.setSpacing(10);
        VBox vbox2 = new VBox();
        vbox2.getChildren().addAll(padding2,padding4,caloriesBox,fatBox,carboHBox,fiberBox,proteinBox);//yxj
        vbox2.setSpacing(10);
        HBox centerBox = new HBox();
        centerBox.setSpacing(50);
        centerBox.getChildren().addAll(vbox1,vbox2);
        centerBox.setAlignment(Pos.CENTER);
        BorderPane.setMargin(ok, new Insets(0,100,50,270));
        root.setCenter(centerBox);
        root.setBottom(ok);
        return root;
    }
    
    /**
     * helper method that checks if is a number
     * 
     * @param input string
     */
    private boolean isNumber(String string) {
    	String reg = "^[0-9]+(.[0-9]+)?$";
		System.out.println(string);
		return string.matches(reg);
    }

    /**
     * node for display
     * 
     * @return null
     */
    @Override
    public Node getNode() {
        return null;
    }
}
