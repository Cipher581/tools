package art.cipher581.tools;


import java.text.DecimalFormat;
import java.util.Collection;
import java.util.LinkedList;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoint;


public class KunstPreis {

    private static final int[][] CALC = new int[][]{{10, 10}, {20, 20}, {30, 60}, {50, 50}, {80, 40}, {100, 100}, {150, 100}};

    private static final double[][] FIX = new double[][]{{100, 0.5}, {400, 0.18}, {2500, 0.1}, {15000, 0.06}, {30000, 0.05}, {60000, 0.05}};

    private static final DecimalFormat DF = new DecimalFormat("0.0000");


    public static void main(String[] args) {
        PolynomialCurveFitter curveFitter = PolynomialCurveFitter.create(2);

        Collection<WeightedObservedPoint> points = new LinkedList<>();

        for (double[] price : FIX) {
            points.add(new WeightedObservedPoint(3, price[0], price[1]));
        }

        double[] coefficients = curveFitter.fit(points);
        PolynomialFunction f = new PolynomialFunction(coefficients);

        System.out.println("function: " + f);

        for (double coefficient : coefficients) {
            System.out.println("coefficient: " + DF.format(coefficient));
        }

        for (int[] size : CALC) {
            double x = size[0] * size[1];

            double factor = f.value(x);

            System.out.println(size[0] + "x" + size[1] + ": " + DF.format(factor));
        }
    }

}
