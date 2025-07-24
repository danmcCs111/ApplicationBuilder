package BezierCurveCalculations;

import java.awt.Point;

public class BezierCurveSample 
{
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
}
