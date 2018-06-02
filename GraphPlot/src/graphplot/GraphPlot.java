package graphplot;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphPlot extends ApplicationFrame {

    private static List<String> lines = new ArrayList<String>();
    private double[] array = new double[500];

    public GraphPlot(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "ITERATIONS", "BEST COST PER ITERATION",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        setContentPane(chartPanel);
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int i = 0;
        for (String s : lines) {
            array[i] = Double.parseDouble(s);
            i++;
        }
        for (int j = 0; j < array.length; j++) {
            dataset.addValue(array[j], "Best Cost Value",""+j);
        }
        return dataset;
    }

    public static void main(String[] args) {

        try {
            lines = Files.readAllLines(Paths.get("C:\\Users\\kotic\\Desktop\\CODE OUTPUT\\SET_OUTPUT.txt"));
        } catch (IOException ex) {
            Logger.getLogger(GraphPlot.class.getName()).log(Level.SEVERE, null, ex);
        }

        GraphPlot chart = new GraphPlot(
                "Iterations Vs Best Cost",
                "Best Cost in Each Iteration Vs Iterations");

        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }
}
