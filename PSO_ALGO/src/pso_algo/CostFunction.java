package pso_algo;

/**
 *
 * @author kotic
 */
public class CostFunction {
	public static double evaluate(double[] position) {
		
		double x1 = position[0]; // the "x" part of the location
		double y1 = position[1]; // the "y" part of the location
 
                //TO BE RANDOMLY GENERATED LATER ON
                double x2 = 900; // the "x" part of the target
		double y2 = 500; // the "y" part of the target
                 
                double distanceFromTarget = Math.sqrt(Math.abs(Math.pow(x2-x1,2)+Math.pow(y2-y1,2)));
		return distanceFromTarget;
	}    
}