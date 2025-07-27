package BezierCurveCalculations;

import java.awt.Point;

public interface BezierCurveSample 
{
	/*
	 * https://en.wikipedia.org/wiki/B%C3%A9zier_curve
	 * B(t) = 
	 *  (1-t)^3 * point 0 +
	 *  3 * (1-t) * t * point 1 +
	 *  3 * (1-t) * t^2 * point 2 +
	 *  t^3 * point 3
	 */
	public static Point getPointAtCubicCurve(Point start, Point control1, Point control2, Point end, double tStep) 
	{
		double 
			inverseTStep = (1-tStep),
			tStepSquared = Math.pow(tStep, 2),
			tStepCubed = Math.pow(tStep, 3),
			inverseTStepsquared = Math.pow(inverseTStep, 2),
			inverseTStepCubed = Math.pow((inverseTStep), 3);
		
		double x = (start.getX() * inverseTStepCubed)
				+ (3 * control1.getX() * tStep * inverseTStepsquared)
		        + (3 * control2.getX() * tStepSquared * inverseTStep)
		        + (end.getX() * tStepCubed);
		double y = (start.getY() * inverseTStepCubed)
				+ (3 * control1.getY() * tStep * inverseTStepsquared)
		        + (3 * control2.getY() * tStepSquared * inverseTStep)
		        + (end.getY() * tStepCubed);

		return new Point((int)x, (int)y);
	}
	
	/*
	 * https://en.wikipedia.org/wiki/B%C3%A9zier_curve
	 * B(t) = 
	 *  (1-t)^2 * point 0 +
	 *  2 * (1-t) * t * point 1 +
	 *  t^2 point 2
	 */
	public static Point getPointAtQuadraticCurve(Point start, Point control1, Point end, double tStep) 
	{
		double 
			inverseTStep = (1-tStep),
			tStepSquared = Math.pow(tStep, 2),
			inverseTStepsquared = Math.pow(inverseTStep, 2);
		
		double x = (start.getX() * inverseTStepsquared)
				+ (2 * control1.getX() * tStep * inverseTStep)
				+ (end.getX() * tStepSquared);
		double y = (start.getY() * inverseTStepsquared)
				+ (2 * control1.getY() * tStep * inverseTStep)
				+ (end.getY() * tStepSquared);

		return new Point((int)x, (int)y);
	}
	
	/**
	 * https://en.wikipedia.org/wiki/B%C3%A9zier_curve
	 * B(t) = 
	 *  (1-t) * point 0 +
	 *  t * point 1
	 */
	public static Point getPointAtLine(Point start, Point end, double tStep)
	{
		double inverseTStep = (1-tStep);
		double x = start.getX() * inverseTStep + end.getX() * tStep;
	    double y = start.getY() * inverseTStep + end.getY() * tStep;
	    
	    return new Point((int)x, (int)y);
	}
}
