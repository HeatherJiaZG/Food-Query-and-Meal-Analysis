/**
 * Filename: MenuBarWrapper.java 
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * class that sets up the menu bar
 */
public class MenuBarWrapper implements NodeWrapperADT {
	//fields
	MenuBar menubar;
	FoodDataADT foodData;
	Stage ownerStage;
	FoodListView foodList;
	private Label counterLabel;
	private ObservableList<FoodItem> selectedFoodItems;
	
	/**
	 * display the menu bar and let user interact with the three buttons
	 * 
	 * @param food data, primary GUI stage, selected food, counter label
	 */
	public MenuBarWrapper(FoodDataADT foodData, Stage primaryStage,Label counterLabel,ObservableList<FoodItem> selectedFoodItems) {
		this.foodData = foodData;
		this.counterLabel = counterLabel;
		this.selectedFoodItems = selectedFoodItems;
		ownerStage = new Stage();
		foodList = new FoodListView(false);
		menubar = new MenuBar();
		//text on buttons
		Menu menuLoad = new Menu("Load");
		Menu menuSave = new Menu("Save");
		Menu help = new Menu("HELP ! ! !");
		menubar.getMenus().addAll(menuLoad,menuSave,help);
		//set up the HELP!!! select item instructions
        MenuItem helpSelect = new MenuItem("How to select food in the list");
        helpSelect.setOnAction(e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            //instructions
            alert.setTitle("How to select food in the list");
            alert.setContentText("Use mouse to click the items you want to select. (no need to press Ctrl)");
            alert.showAndWait();
        });
		//set up the HELP!!! add to meal instructions
		MenuItem helpSelectFood = new MenuItem("How to add food to MealList");
		helpSelectFood.setOnAction(e -> {
		    Alert alert = new Alert(AlertType.INFORMATION);
		    //instructions
            alert.setTitle("How to add food to MealList");
            alert.setContentText("Click Select Food button, all selected food will be moved to Meal List.");
            alert.showAndWait();
		});
		//set up the HELP!!! filter instructions
	    MenuItem helpFilter = new MenuItem("How to apply and remove filters");
	    helpFilter.setOnAction(e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            //instructions
            alert.setTitle("How to apply and remove filters");
            alert.setContentText("Nutrient filters have to be add to the rule list first (by clicking Add to Rules).\n"
                            + "Click Apply Rules button, all rules in the Rules List will be applied, including the name filtering if any.\r\n" + 
                            "To remove rules, clicking Remove Rules will remove all selected rules.");
            alert.showAndWait();
        });
	    //set up the HELP!!! meal analysis function
	    MenuItem helpAnalyze = new MenuItem("How to analyze and remove meals");
	    helpAnalyze.setOnAction(e -> {
            Alert alert = new Alert(AlertType.INFORMATION);
            //instructions
            alert.setTitle("How to analyze and remove meals");
            alert.setContentText("Click Analyze button, all food in the Meal List will be analyzed. \r\n" + 
                            "To remove food from the meal, select foods to be removed and then click Remove.");
            alert.showAndWait();
        });
	    help.getItems().addAll(helpSelect,helpSelectFood,helpFilter,helpAnalyze);
	    //load and save functions
		MenuItem loadMenuItem = new MenuItem("Choose a file");
		MenuItem saveMenuItem = new MenuItem("Create a file");
        menuLoad.getItems().add(loadMenuItem);
        menuSave.getItems().add(saveMenuItem);
        //load to view list
        loadMenuItem.setOnAction(e -> {setUpFoodListView();});
        //save list to file
        saveMenuItem.setOnAction(e -> {saveFoodListView(primaryStage);});
	}
	
	/**
	 * display menu bar
	 * 
	 * @return menubar view
	 */
	public Node getNode() {return menubar;}
	
	/**
	 * load read file into list view
	 */
	public void setUpFoodListView() {
		FileChooser fileChooser = new FileChooser();
        File csv = fileChooser.showOpenDialog(ownerStage);
        //do nothing on null input list
        if (csv == null) {
            return;
        } 
        //call loadFoodItem on the path given
        foodData.loadFoodItems(csv.getAbsolutePath());
        foodList.foodItemList = foodData.getAllFoodItems();
        foodList.getView();
        //tracks total food count
        this.counterLabel.setText("Total food count: "+ foodList.foodListView.getItems().size());
        this.selectedFoodItems.clear();
        return;
	}
	
	/**
	 * save current list to a file
	 * 
	 * @param current food list
	 */
    public void saveFoodListView(Stage primaryStage) {
         FileChooser fileChooser = new FileChooser();
         //Set extension filter
         //.csv format
         FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
         fileChooser.getExtensionFilters().add(extFilter);
         //Show save file dialog
         File file = fileChooser.showSaveDialog(primaryStage);
         if(file != null){
        	 //call saveFoodItems
             foodData.saveFoodItems(file.getAbsolutePath());
         }
    }
}
