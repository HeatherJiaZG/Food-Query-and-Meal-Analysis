/**
 * Filename: NodeWrapperADT.java 
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

import javafx.scene.Node;

/**
 * interface that helps map implementation of NodeWrapper
 */
public interface NodeWrapperADT {
	
	/**
	 * display the view
	 * 
	 * @return null
	 */
	public Node getNode();
	}
