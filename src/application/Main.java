/**
 * Filename: Main.java 
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
	

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;


/**
 * main class that launches meal analysis GUI
 * 
 * @param run command
 */
public class Main extends Application {
	//fields
	FoodDataADT foodData;
	PrimaryGUI primaryGUI;
	
    /**
     * start the application
     * 
     * @param primary GUI stage
     */
	@Override
	public void start(Stage primaryStage) {
		try {
			//get the necessary fields
			foodData = new FoodData();
			primaryGUI= new PrimaryGUI(foodData,primaryStage); 
			//show GUI
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * launch by running this main class
     * 
     * @param input args "run" command
     */
	public static void main(String[] args) {
		launch(args);
	}
}
