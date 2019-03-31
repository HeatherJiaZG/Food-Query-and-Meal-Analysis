/**
 * Filename: FoodListView.java 
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

/**
 * sets up the window for user to view food list on primary GUI
 * 
 * @param food list
 */
public class FoodListView implements NodeWrapperADT{
	ListView<FoodItem> foodListView;
	List<FoodItem> foodItemList;
	
    /**
     * let user choose food from list
     * 
     * @param boolean indicating item chosen
     */
	public FoodListView(boolean toDelete) {		
		foodListView = new ListView<FoodItem>();
		foodItemList = foodListView.getItems();
		//call helper
		setListViewSelectable();
	}
	
    /**
     * get user selected items
     * 
     * @return selected item from list
     */
	public List<FoodItem> getSelection() {
        return this.foodListView.getSelectionModel().getSelectedItems();
	    
	}
	
	 /**
     * This is a private helper to enable ListView list to be selected.
     */
    private void setListViewSelectable() {
        foodListView.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            Node targetNode = evt.getPickResult().getIntersectedNode();
            // go up from the target node until a list cell is found or it's clear
            // it was not a cell that was clicked
            while (targetNode != null && targetNode != foodListView && !(targetNode instanceof ListCell)) {
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
                    } else {
                        listView.getSelectionModel().select(index);
                    }
                }
            }
        });
    }
	
    /**
     * sort the list of food alphabetically
     */
	public void sortFoodList() {
        Collections.sort(foodListView.getItems(), new Comparator<FoodItem>() {
            @Override
            public int compare(FoodItem a, FoodItem b) {
                // case insensitive
                return a.getName().toUpperCase().compareTo(b.getName().toUpperCase());
            }
        });
    }
	
    /**
     * get the view of food list
     * 
     * @return view of list of food
     */
	public ListView<FoodItem> getView() {
	    foodListView.setItems(FXCollections.observableArrayList());
	    //add to list view
		for(FoodItem item:foodItemList) {
			foodListView.getItems().add(item);
		}
        return foodListView;
	}
	
    /**
     * get food list itself
     * 
     * @return list of food
     */
	public List<FoodItem> getFoodItemList() {
        return foodItemList;
	}
	
    /**
     * node that displays list
     * 
     * @return final view of food list
     */
    public Node getNode() {
    	return foodListView; 	
    }
}
