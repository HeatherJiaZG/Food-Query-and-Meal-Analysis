/**
 * Filename: FilterRulesForm.java 
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

import java.util.List;
import javafx.scene.Node;

/**
 * class that enables filter 
 * 
 * @param rule, food data
 */
public class FilterRulesForm implements NodeWrapperADT{
    FilterRule filterRule;
    FoodDataADT foodData;
    
	/**
	 * no node needed
	 */
    public Node getNode() {return null;}
	
	/**
	 * input rules and food data
	 * 
	 * @param filter rule, food data
	 */
	public FilterRulesForm(FilterRule filterRule, FoodDataADT foodData) {
	    this.filterRule = filterRule;
	    this.foodData = foodData;
	}

	/**
	 * filter the list 
	 * 
	 * @return filtered list of food
	 */
	public List<FoodItem> getFiltered(){
		//filter by name
	    foodData.filterByName(filterRule.getSubStr());
	    //filter by nutrients
	    return foodData.filterByNutrients(filterRule.getRuleList());
	}
}
