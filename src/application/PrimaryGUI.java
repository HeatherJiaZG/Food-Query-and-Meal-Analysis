/**
 * Filename: PrimaryGUI.java 
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

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * This is the main GUI class which creates the primary screen and sets
 * up the scene.
 */
public class PrimaryGUI {
	//fields
    FoodDataADT foodData;
    FoodItemAddForm foodItemAddForm; 
    FilterRulesForm filterRulesForm;
    MenuBarWrapper menuBar;
    FoodListView foodList;
    FoodListView mealList;
    Button addFood;
    Button selectFood;
    Button filterFoodList;
    Button showAllFood;
    Button analyze;
    
    /**
     * This is the initial Graphical User Interface when the whole program runs.
     * It contains all the UI components necessary, including buttons, labels,
     * lists, menu bars and panes.
     * 
     * @param foodData: The foodData to be loaded
     * @param stage: the primary stage
     */
    public PrimaryGUI(FoodDataADT foodData, Stage stage){
        stage.setTitle("Food Query & Meal Analysis");
        // Setup All the Buttons and menu bars
        this.foodData = foodData;
        Label counterLabel = new Label();
        ObservableList<FoodItem> selectedFoodItems = FXCollections.observableArrayList();
        this.menuBar = new MenuBarWrapper(this.foodData,stage,counterLabel,selectedFoodItems);
        //this.foodData.setAllFoodList(this.menuBar.foodList.foodItemList);
        this.foodList = menuBar.foodList;
        this.mealList = new FoodListView(false);
        Node menuBarNode = menuBar.getNode();
        //label texts
        addFood = new Button("Add Food");
        selectFood = new Button("Select Food");
        filterFoodList = new Button("Apply Rules");
        showAllFood = new Button("Show All Food");
        analyze = new Button ("Analyze");
        Button removeFromMealList = new Button("Remove");
        // Arrange Placements
        GridPane ButtonGrid = new GridPane();
        ButtonGrid.setPadding(new Insets(20,0,0,12));
        ButtonGrid.setHgap(6);
        ButtonGrid.setVgap(5);
        GridPane.setConstraints(addFood,0,0);
        GridPane.setConstraints(selectFood,10,0);
        GridPane.setConstraints(filterFoodList,20,0);
        GridPane.setConstraints(showAllFood,30,0);
        GridPane.setConstraints(removeFromMealList,40,0);
        GridPane.setConstraints(analyze,50,0);
        GridPane.setConstraints(menuBarNode,0,10);
        // Prepare for the first column for FoodList
        VBox foodBox = new VBox();
        setUpFoodColumn(foodBox,counterLabel);
        showAllFood.setOnAction(e -> {
            selectedFoodItems.clear();
            ObservableList<FoodItem> allFoodList = FXCollections.observableArrayList();
            allFoodList.addAll(foodData.getAllFoodItems());
            foodList.foodListView.setItems(FXCollections.observableArrayList());
            foodList.foodListView.setItems(allFoodList);
            //tracks food count
            counterLabel.setText("Total food count: "+ foodList.foodListView.getItems().size());
        });
        // MealList
        ObservableList<FoodItem> meals = FXCollections.observableArrayList();
        this.mealList.foodItemList = meals;
        this.mealList.foodListView.setItems(meals);
        // Add to MealList
        selectFood.setOnAction(e -> {
        selectedFoodItems.setAll(this.foodList.getSelection());
        meals.addAll(selectedFoodItems);
        this.foodList.foodListView.getSelectionModel().clearSelection();
        this.mealList.sortFoodList();
        });
        // Set Help Massages when mouse hover over the buttons
        selectFood.setTooltip(new Tooltip("Add selected food to Meal List"));
        filterFoodList.setTooltip(new Tooltip("Apply the Name Filter and all the nutrient rules in the Rules List"));
        showAllFood.setTooltip(new Tooltip("Restore the Food List"));
        addFood.setTooltip(new Tooltip("Add new food to Food List"));
        analyze.setTooltip(new Tooltip("Analyze All Food in Meal List"));
        removeFromMealList.setTooltip(new Tooltip("Remove selected food From Meal List"));
        // Prepare for the second column for Filter Rules
        VBox filterBox = new VBox();
        setUpFilterColumn(filterBox,selectedFoodItems,counterLabel);
        //set up the scene
        addFood.setOnAction(e -> {
            Stage addStage = new Stage();
            FoodItemAddForm addForm = new FoodItemAddForm(addStage,this.foodData,this.foodList); 
            BorderPane root = addForm.setUpAddWindow(selectedFoodItems,counterLabel);
            Scene scene = new Scene(root,650,300);
            addStage.setScene(scene);
            addStage.show();
        });
        // Prepare for the third column for MealList
        VBox mealBox = new VBox();
        setUpMealColumn(mealBox,meals,removeFromMealList);
        setUpDialogChart(stage);  // Nutrition Analysis pop-up Chart
        HBox integrateBox = new HBox();  // integrate three columns
        integrateBox.getChildren().addAll(foodBox,filterBox,mealBox);
        integrateBox.setSpacing(50);
        BorderPane pane = new BorderPane();  // integrate everything
        pane.setCenter(integrateBox);
        //format
        pane.setMargin(integrateBox, new Insets(15,15,15,25));
        pane.setBottom(ButtonGrid);
        pane.setMargin(ButtonGrid, new Insets(0,15,15,25));
        ButtonGrid.getChildren().addAll(addFood,selectFood,filterFoodList,
                        showAllFood,removeFromMealList,analyze);
        pane.setTop(menuBarNode);
        //size of window
        Scene scene = new Scene(pane,910,1000);
        stage.setScene(scene);
    }
    
    /**
     * This is a private helper class to set up the whole food column.
     * 
     * @param foodBox: the placeholder to hold the whole food column
     * @param selectedItems: the list of food items selected by user
     */
    private void setUpFoodColumn(VBox foodBox,Label counterLabel) {
        // Setup the pane to hold the food list
        ScrollPane foodPane = new ScrollPane();
        foodPane.setFitToHeight(true);
        foodPane.setFitToWidth(true);
        foodPane.setPrefViewportHeight(1000);
        foodPane.setPrefWidth(268);
        // Setup column label and counter label
        Label foodListLabel = new Label("Available Food");
        foodListLabel.setFont(Font.font("Arial",FontWeight.BOLD,16));
        counterLabel.setText("Total food count: "+ foodList.foodListView.getItems().size());
        // Organize
        foodBox.getChildren().addAll(foodListLabel,counterLabel,foodPane);
        foodBox.setMargin(foodListLabel, new Insets(10,55,5,65));
        foodBox.setMargin(counterLabel, new Insets(0,0,5,100));
        foodBox.setFillWidth(true);
        // Setup food list 
        foodList.foodListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        foodPane.setContent(foodList.foodListView);
        
        
    }
    
    /**
     * This is a private helper class to set up the whole Filter column.
     * 
     * @param filterBox: the placeholder to hold the whole column
     */
    private void setUpFilterColumn(VBox filterBox,ObservableList<FoodItem> selectedItems2,Label counterLabel) {
        // Setup the box to enter input for filters with nutrient constraints
        Label filterLabel = new Label("Nutrient Filter");
        filterLabel.setFont(Font.font("Arial",FontWeight.BOLD,16));
        // Choose the type of nutrient to filter
        ChoiceBox<String> nutrient = new ChoiceBox();
        nutrient.getItems().addAll("  Calories  ","  Carbohydrate  ","  Fat  ","  Fiber  ","  Protein  ");
        nutrient.setValue("  Calories  ");
        nutrient.setTooltip(new Tooltip("Select Nutrient"));
        nutrient.setPrefWidth(125);
        // Choose the comparator in the filter
        ChoiceBox<String> comparator = new ChoiceBox();
        comparator.getItems().addAll("<=  ", ">=  ", "==  ");
        comparator.setValue("<=  ");
        comparator.setTooltip(new Tooltip("Select Comparator"));
        comparator.setPrefWidth(125);
        // Choose the limiting value in the filter
        TextField value = new TextField();
        value.setMaxWidth(67);
        value.setText(" value");
        value.setTooltip(new Tooltip("Please enter a number"));
        // Add to the list of rules
        Button add = new Button("Add to Rules");
        add.setTooltip(new Tooltip("Add the nutrient rule to Rules List before applying the filter"));
        add.setPrefWidth(125);
        ObservableList<String> rules = FXCollections.observableArrayList();
        ObservableList<String> selectedRules = FXCollections.observableArrayList();
        add.setOnAction(e -> {
        	if(!value.getText().isEmpty()) {
        		//trim using regular expression
        		if(value.getText().trim().matches("^[0-9]+(.[0-9]+)?$")) {
        			rules.addAll(nutrient.getValue()+comparator.getValue()+value.getText());
        			selectedRules.clear();
        		}
        		else {
        			//value needs to be int
        			Alert alert = new Alert(AlertType.INFORMATION);
            		alert.setTitle("Incorrect Input");
            		alert.setContentText("The input must be a non-negative number!");
            		alert.showAndWait();
        		}
        	}
        	else {
        		//value needs to be int
        		Alert alert = new Alert(AlertType.INFORMATION);
        		alert.setTitle("Incorrect Input");
        		alert.setContentText("The input cannot be blank!");
        		alert.showAndWait();
        	}
            
        });
        // Organize the nutrient filter box
        VBox nutrientBox = new VBox();
        nutrientBox.getChildren().addAll(nutrient,comparator,value,add);
        //labels
        nutrientBox.setMargin(nutrient, new Insets(13,30,0,60));
        nutrientBox.setMargin(comparator, new Insets(0,30,0,60));
        nutrientBox.setMargin(value, new Insets(0,30,0,60));
        nutrientBox.setMargin(add, new Insets(10,30,10,60));
        nutrientBox.setSpacing(7);
        //scroll bar
        ScrollPane nutrientFilterPane = new ScrollPane();
        nutrientFilterPane.setPrefViewportHeight(350);
        nutrientFilterPane.setFitToWidth(true);
        nutrientFilterPane.setContent(nutrientBox);
        //drop down
        VBox nutrientFilterBox = new VBox();
        nutrientFilterBox.getChildren().addAll(filterLabel,nutrientFilterPane);
        nutrientFilterBox.setMargin(filterLabel, new Insets(10,50,12,70));
        // Setup the box to enter input for filters with name constraints
        ScrollPane nameFilterPane = new ScrollPane();
        nameFilterPane.setPrefViewportHeight(205);
        nameFilterPane.setFitToWidth(true);
        nameFilterPane.setContent(nutrientBox);
        //format
        Label nameFilterLabel = new Label("Name Filter");
        nameFilterLabel.setFont(Font.font("Arial",FontWeight.BOLD,16));
        VBox nameBox = new VBox();
        nameBox.getChildren().addAll(nameFilterLabel,nameFilterPane);
        nameBox.setMargin(nameFilterLabel, new Insets(10,50,12,75));
        TextField nameToFilter = new TextField("");
        // The help massage when mouse hovers over the TextField
        nameToFilter.setTooltip(new Tooltip("Please Enter Keyword in the Name."));
        // Organize the name filter box
        VBox nameFilterBox = new VBox();
        nameFilterBox.getChildren().addAll(nameToFilter);
        nameFilterBox.setMargin(nameToFilter, new Insets(13,30,0,28));
        //nameFilterBox.setMargin(addNameFilter, new Insets(0,30,0,28));
        nameFilterBox.setSpacing(10);
        nameFilterPane.setContent(nameFilterBox);
        // Setup List of Rules
        Label rulesLabel = new Label("Rules");
        rulesLabel.setFont(Font.font("Arial",FontWeight.BOLD,16));
        ListView filterList = new ListView();
        //label name
        Button removeRules = new Button("Remove Rules");
        removeRules.setTooltip(new Tooltip("Remove selected rules from the Rules list."));
        removeRules.setOnAction(e -> {
        selectedRules.setAll(filterList.getSelectionModel().getSelectedItems());
        selectedRules.forEach((food) -> rules.remove(food));
        selectedRules.clear();
        filterList.getSelectionModel().clearSelection();
        });
        HBox ruleHBox = new HBox();
        ruleHBox.getChildren().addAll(rulesLabel,removeRules);
        ruleHBox.setSpacing(30);
        filterList.setItems(rules);
        //can select multiple at once
        filterList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setListViewSelectabel(filterList,selectedRules);  // enable user select
        filterFoodList.setOnAction(e -> {
            FilterRule filterRule = new FilterRule(nameToFilter.getText(),rules);
            FilterRulesForm filterRuleForm = new FilterRulesForm(filterRule,this.foodData);
            ObservableList<FoodItem> filteredFoodItems = FXCollections.observableArrayList();
            filteredFoodItems.addAll(filterRuleForm.getFiltered());
            this.foodList.foodListView.setItems(filteredFoodItems);
            this.foodList.sortFoodList();
        counterLabel.setText("Total food count: "+ foodList.foodListView.getItems().size());
        selectedItems2.clear();
        });
        // integrate altogether
        ScrollPane filter = new ScrollPane();
        filter.setFitToHeight(true);
        filter.setFitToWidth(true);
        filter.setPrefViewportHeight(600);
        filter.setPrefViewportWidth(215);
        filter.setContent(filterList);
        VBox ruleBox = new VBox();
        ruleBox.getChildren().addAll(ruleHBox,filter);
        ruleBox.setMargin(ruleHBox, new Insets(5,10,13,25));
        filterBox.getChildren().addAll(nutrientFilterBox,nameBox,ruleBox);
        filterBox.setSpacing(10);
    }
    
    /**
     * This is a private helper class to set up the whole Meal List column.
     * 
     * @param mealBox: The placeholder to hold the whole column
     * @param meals: the list of meals added from the food lists
     * @param removeFromMealList: the button to enable deletion from meal list
     */
    private void setUpMealColumn(VBox mealBox,ObservableList<FoodItem> meals,Button removeFromMealList) {
        // Setup the pane to hold the meal list
        ScrollPane MealPane = new ScrollPane();
        MealPane.setFitToHeight(true);
        MealPane.setFitToWidth(true);
        MealPane.setPrefViewportHeight(1000);
        MealPane.setPrefWidth(250);
        // Setup the column title
        Label MealListLabel = new Label("Meal List");
        MealListLabel.setFont(Font.font("Arial",FontWeight.BOLD,16));
        Button clearMeals = new Button("Clear Meal");
        clearMeals.setTooltip(new Tooltip("Remove all food in the Meal List"));
        HBox clearHBox = new HBox();
        clearHBox.setSpacing(25);
        clearHBox.getChildren().addAll(MealListLabel,clearMeals);
        // Organize
        mealBox.getChildren().addAll(clearHBox,MealPane);
        mealBox.setMargin(clearHBox, new Insets(5,10,13,25));
        
        // Setup the meal list
        ListView<FoodItem> mealList = this.mealList.foodListView;
        MealPane.setContent(mealList);
        mealList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<FoodItem> selectedMeal = FXCollections.observableArrayList();
        //can remove from meal list as wishes
        removeFromMealList.setOnAction(e -> {
            selectedMeal.setAll(this.mealList.getSelection());
            selectedMeal.forEach((food) -> meals.remove(food));
            mealList.getSelectionModel().clearSelection();
            selectedMeal.clear();});
        clearMeals.setOnAction(e -> {
        meals.clear();
        selectedMeal.clear();
        });
    }
          
    /**
     * This is private helper to enable pop-up nutrition chart
     * 
     * @param stage the stage to show
     */
    private void setUpDialogChart(Stage stage) {
        FoodAnalysis analysis = new FoodAnalysis();
        analyze.setOnAction(e -> {
            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            final Pane dialogPane = new Pane();
            //chart size
            final Scene dialogScene = new Scene(dialogPane, 500, 400);
            dialog.setTitle("Nutrition Chart");
            PieChart chart = analysis.getPieChart(mealList.foodListView.getItems(),dialogPane);
            dialog.setScene(dialogScene);
            dialog.show();
        } );
    }
    
    /**
     * This is a private helper to enable ListView list to be selected.
     * @param list: the list to be selected
     * @param selectedList: the selected list of items
     */
    private void setListViewSelectabel(ListView<String> list, ObservableList<String> selectedList) {
        list.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            Node targetNode = evt.getPickResult().getIntersectedNode();
            // go up from the target node until a list cell is found or it's clear
            // it was not a cell that was clicked
            while (targetNode != null && targetNode != list && !(targetNode instanceof ListCell)) {
                targetNode = targetNode.getParent();
            }
            // if is part of a cell or the cell,
            // handle event instead of using standard handling
            if (targetNode instanceof ListCell) {
                evt.consume();
                ListCell listCell = (ListCell) targetNode;
                ListView listView = listCell.getListView();
                listView.requestFocus();
                if (!listCell.isEmpty()) {
                    // handle selection for non-empty cells
                    int index = listCell.getIndex();
                    if (listCell.isSelected()) {
                        listView.getSelectionModel().clearSelection(index);
                    } 
                    else {
                        listView.getSelectionModel().select(index);
                    }
                }
            }
        });
    }
}
