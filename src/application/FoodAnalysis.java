/**
 * Filename: FoodAnalysis.java 
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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * display a pie chart of meal nutrition and value when clicked
 * 
 * @param list of food, pane, data, mouse event
 */
public class FoodAnalysis {
	//fields
    private Double sum = 0.0;
    private ObservableList<PieChart.Data> pieChartData;
    private PieChart chart;
    
	/**
	 * display pie chart
	 */
    public FoodAnalysis() {
     // to be replaced by real nutrition analysis data
        pieChartData =  FXCollections.observableArrayList(
                        new PieChart.Data("Calories", 0.0),
                        new PieChart.Data("Fat", 0.0),
                        new PieChart.Data("Carbohydrates", 0.0),
                        new PieChart.Data("Fiber", 0.0),
                        new PieChart.Data("Protein", 0.0));
        chart = new PieChart(pieChartData);
        chart.setTitle("Meal Analysis");
    }
	
    /**
     * This is private helper to enable pop-up nutrition chart
     * 
     * @param stage the stage to show
     * @return pie chart
     */
    public PieChart getPieChart(List<FoodItem> foodItems, final Pane dialogPane){
        System.out.println(foodItems);
        final Label caption = new Label("");
        //instruction
        Label help = new Label("Click different regions to see the nutrient value and proportion.");
        //position
        help.setLayoutX(50);
        help.setLayoutY(32);
        dialogPane.getChildren().addAll(help,chart,caption);
        pieChartData.forEach((data) -> data.setPieValue(0));
        for (FoodItem food : foodItems) {
        	//to display nutrition facts of pie chart
            HashMap <String, Double> nutrientPair = food.getNutrients();
            Collection<Double> values = nutrientPair.values();
            //individual nutrients
            pieChartData.get(0).setPieValue(pieChartData.get(0).getPieValue()+nutrientPair.get("calories"));
            pieChartData.get(1).setPieValue(pieChartData.get(1).getPieValue()+nutrientPair.get("fat"));
            pieChartData.get(2).setPieValue(pieChartData.get(2).getPieValue()+nutrientPair.get("carbohydrate"));
            pieChartData.get(3).setPieValue(pieChartData.get(3).getPieValue()+nutrientPair.get("fiber"));
            pieChartData.get(4).setPieValue(pieChartData.get(4).getPieValue()+nutrientPair.get("protein"));
        }
        //sums up individual food nutrients
        getSum(pieChartData);
        
        // to move the label around as mouse clicked on the chart
        // with the data updated based on where mouse clicked
        for (final PieChart.Data data : pieChartData) {
        	//when clicked, display nutrients
            data.getNode().setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent e) {
                	//display format as specified
                    System.out.println(String.valueOf(data.getPieValue()) + "%");
                    caption.setTranslateX(e.getSceneX());
                    caption.setTranslateY(e.getSceneY());
                    caption.setText(String.valueOf(data.getPieValue() +" g ("+String.format("%.2f", (100*data.getPieValue()/sum))) + "%)");  
                 }
            });  
        }
        return chart;     
    }
    
	/**
	 * private method to sum up nutrient values and display total
	 * 
	 * @param data from pie chart
	 */
    private void getSum(ObservableList<PieChart.Data> pieChartData){
        pieChartData.forEach((data) -> {sum += data.getPieValue();
        });
        return ;
    }
	
	
	
	
}
