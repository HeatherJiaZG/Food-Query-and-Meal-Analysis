/**
 * Filename: FoodData.java 
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javafx.stage.FileChooser;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
 */
public class FoodData implements FoodDataADT<FoodItem> {
    // List of all the food items.
    private List<FoodItem> foodItemList;
    private List<FoodItem> temptItemList;
    
    /**
     * private helper to keep food list sorted in alphanumeric order
     * and case insensitive
     */
    private void sortFoodList() {
        Collections.sort(foodItemList, new Comparator<FoodItem>() {
            @Override
            public int compare(FoodItem a, FoodItem b) {
                // case insensitive
                return a.getName().toUpperCase().compareTo(b.getName().toUpperCase());
            }
        });
    }
    
    // Map of nutrients and their corresponding index
    private HashMap<String, BPTree<Double, FoodItem>> indexes;
    
    
    /**
     * Public constructor
     */
    public FoodData() {
        foodItemList = new ArrayList <FoodItem>();
        indexes = new HashMap<String,BPTree<Double, FoodItem>>();
        temptItemList = new ArrayList <FoodItem>();
    }
    
    
    /**
     * Load food item data from a file chosen by the user.
     * @param filePath to the file
     */
    @Override
    public void loadFoodItems(String filePath) {
        // Multiple loads will not concatenate food items
        foodItemList = new ArrayList <FoodItem>();
		BufferedReader br = null;
		try {
			//read buffer
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
		    return;
		}
		String line ="";
		String eachLine = "";
		try {
			List<String> allStrings = new ArrayList<>();
			while((line=br.readLine())!=null)
			{
				eachLine = line;
				//split by , to get individual value
				String[] splitStrings = eachLine.split(",");
				if(splitStrings.length!=12) 
					continue;
				//ID and name
				FoodItem foodItem = new FoodItem(splitStrings[0],splitStrings[1]);
				//name of food and nutrient
				for(int i = 0; i < 9 ;i = i+2) {
					foodItem.addNutrient(splitStrings[i+2].toLowerCase(), Double.parseDouble(splitStrings[i+3]));
				}
				//name of food
				allStrings.add(eachLine.split(",")[1]);
				foodItemList.add(foodItem);
			}
			sortFoodList();
		}catch(IOException e)
		{
		}
		//load food item
		temptItemList = foodItemList;
    }

	/**
	 * Save current list of food to a file
	 * 
	 * @param user file name
	 */
    @Override
    public void saveFoodItems(String filename) {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(filename));
            // store each element in foodItemList
            for (FoodItem food : foodItemList) {
            	//format
                String text = food.getID() + "," + food.getName();              
                HashMap<String, Double> nutrients = food.getNutrients();
                // store each nutrition and value                            
                text = text +  "," + "calories" + "," + nutrients.get("calories").toString();
                text = text +  "," + "fat" + "," + nutrients.get("fat").toString();
                text = text +  "," + "carbohydrate" + "," + nutrients.get("carbohydrate").toString();
                text = text +  "," + "fiber" + "," + nutrients.get("fiber").toString();
                text = text +  "," + "protein" + "," + nutrients.get("protein").toString();               
                text = text + "\n";
                writer.write(text);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            try {
                writer.flush();
                //close writer
                writer.close();  
            }catch(Exception e) {
            }
        }
        
    }

    
    /**
     * Filter the whole food item list by the substring provided
     * and it is case-insensitive
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
        List<FoodItem> filteredList = new ArrayList<FoodItem>();
        //no filter
        if(substring.length()==0) {
            temptItemList = foodItemList;
            return temptItemList;
        }
        //match name
        for(FoodItem item:foodItemList) {  // always start over from all food list
            if(item.getName().toLowerCase().contains(substring.toLowerCase())) 
                filteredList.add(item);
        }
        temptItemList = filteredList;
        return filteredList;
    }

    /**
     * Filter the filtered list (the food item list firstly filtered by the
     * substring) by the nutrient rules provided
     * @param list of nutrient rules
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
        List<FoodItem> filteredList = new ArrayList<FoodItem>();
        // always filter from the temptItemList
        BPTreeADT<Double, FoodItem> caloTree;
        BPTreeADT<Double, FoodItem> fatTree;
        BPTreeADT<Double, FoodItem> carboTree;
        BPTreeADT<Double, FoodItem> fiberTree;
        BPTreeADT<Double, FoodItem> proteinTree;
        for(String rule: rules) {
            //split
            String[] ruleArray = rule.trim().split("\\s+");
            //filter calories
            if(ruleArray[0].equals("Calories")) {
                caloTree = new BPTree<Double,FoodItem>(3);
                //loop to find each item that matches requirement
                for(FoodItem foodItem: temptItemList ) 
                    caloTree.insert(foodItem.getNutrientValue("calories"), foodItem);
                //range search
                temptItemList = caloTree.rangeSearch(Double.parseDouble(ruleArray[2]), ruleArray[1]);}
            //filter fat
            if(ruleArray[0].equals("Fat")) {
                fatTree = new BPTree<Double,FoodItem>(3);
                //loop to find each item that matches requirement
                for(FoodItem foodItem: temptItemList ) 
                    fatTree.insert(foodItem.getNutrientValue("fat"), foodItem);
                //range search
                temptItemList = fatTree.rangeSearch(Double.parseDouble(ruleArray[2]), ruleArray[1]);}
            //filter carbohydrate
            if(ruleArray[0].equals("Carbohydrate")) {
                carboTree = new BPTree<Double,FoodItem>(3);
                //loop to find each item that matches requirement
                for(FoodItem foodItem: temptItemList ) 
                    carboTree.insert(foodItem.getNutrientValue("carbohydrate"), foodItem);
                //range search
                temptItemList = carboTree.rangeSearch(Double.parseDouble(ruleArray[2]), ruleArray[1]);}
            //filter fiber
            if(ruleArray[0].equals("Fiber")) {
                fiberTree = new BPTree<Double,FoodItem>(3);
                //loop to find each item that matches requirement
                for(FoodItem foodItem: temptItemList ) 
                    fiberTree.insert(foodItem.getNutrientValue("fiber"), foodItem);
                //range search
                temptItemList = fiberTree.rangeSearch(Double.parseDouble(ruleArray[2]), ruleArray[1]);}
            //filter protein
            if(ruleArray[0].equals("Protein")) {
                proteinTree = new BPTree<Double,FoodItem>(3);
                //loop to find each item that matches requirement
                for(FoodItem foodItem: temptItemList ) 
                    proteinTree.insert(foodItem.getNutrientValue("protein"), foodItem);
                //range search
                temptItemList = proteinTree.rangeSearch(Double.parseDouble(ruleArray[2]), ruleArray[1]);
            }       
        }
        return temptItemList;
    }

    /**
     * To add new food to the whole food item list
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
        foodItemList.add(foodItem);
        //sort after adding
        sortFoodList();
    }

    /**
     * The public getter for the foodItemList
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return foodItemList;
    }

}