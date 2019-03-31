/**
 * Filename: FilterRule.java 
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
import java.util.List;

/**
 * class that set up rules for filter
 * 
 * @param rule string, rule list
 */
public class FilterRule {
    // The substring to be used for filtering .
    private String nameSubstr;

    // List of nutrient based filter rules.
    private List<String> ruleList;

	/**
	 * get the rules
	 * 
	 * @param rule string, rule list
	 */
    public FilterRule(String nameSubstr,List<String> ruleList) {
        this.nameSubstr = nameSubstr;
        this.ruleList = ruleList;
    }
    
	/**
	 * get string input
	 * 
	 * @return substring from input
	 */
    public String getSubStr() {
        return nameSubstr;
    }
    
	/**
	 * get list of rules
	 * 
	 * @return list or rules
	 */
    public List<String> getRuleList(){
        return ruleList;
    }
    
}
