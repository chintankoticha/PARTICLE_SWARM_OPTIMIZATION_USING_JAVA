/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package try1;

/**
 *
 * @author ayush
 */
public class ProblemSet {
	public static final double LOC_X_LOW = 1;
	public static final double LOC_X_HIGH = 4;
	public static final double LOC_Y_LOW = -1;
	public static final double LOC_Y_HIGH = 1;
	public static final double VEL_LOW = -1;
	public static final double VEL_HIGH = 1;
	
	public static final double ERR_TOLERANCE = 1E-20; // the smaller the tolerance, the more accurate the result, 
	                                                  // but the number of iteration is increased
	
	public static double evaluate(Location location) {
		double result = 0;
		double x1 = location.getLoc()[0]; // the "x" part of the location
		double y1 = location.getLoc()[1]; // the "y" part of the location
                
                double x2 = 1200; // the "x" part of the location
		double y2 = 400;
                 
                double distance = Math.sqrt(Math.abs(Math.pow(x2-x1,2)+Math.pow(y2-y1,2)));
		
//		result = Math.pow(2.8125 - x + x * Math.pow(y, 4), 2) + 
//				Math.pow(2.25 - x + x * Math.pow(y, 2), 2) + 
//				Math.pow(1.5 - x + x * y, 2);
		
		return distance;
	}
}
