import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.JSONParser;

public class SecretSharingSimplified {

    public static void main(String[] args) {
        try {
            // Load and parse JSON file
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(new FileReader("input.json"));
            JSONObject keys = (JSONObject) data.get("keys");
            int k = ((Long) keys.get("k")).intValue();

            // Decode points
            List<Point> points = new ArrayList<>();
            for (Object key : data.keySet()) {
                if (!key.equals("keys")) {
                    JSONObject pointData = (JSONObject) data.get(key);
                    int x = Integer.parseInt((String) key);
                    int base = Integer.parseInt((String) pointData.get("base"));
                    String value = (String) pointData.get("value");
                    BigInteger y = new BigInteger(value, base);
                    points.add(new Point(BigInteger.valueOf(x), y));
                }
            }

            // Compute constant term (secret) with Lagrange interpolation
            BigInteger secret = calculateConstantTerm(points, k);
            System.out.println("The constant term (secret) is: " + secret);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function to calculate the constant term using Lagrange Interpolation
    public static BigInteger calculateConstantTerm(List<Point> points, int k) {
        BigInteger result = BigInteger.ZERO;

        for (int i = 0; i < k; i++) {
            BigInteger xi = points.get(i).x;
            BigInteger yi = points.get(i).y;
            BigInteger term = yi;

            // Calculate the product part of the Lagrange formula
            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = points.get(j).x;
                    term = term.multiply(BigInteger.ZERO.subtract(xj)).divide(xi.subtract(xj));
                }
            }
            result = result.add(term);
        }
        return result;
    }

    // Helper class to store points (x, y)
    static class Point {
        BigInteger x, y;
        Point(BigInteger x, BigInteger y) {
            this.x = x;
            this.y = y;
        }
    }
}
