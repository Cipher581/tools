package art.cipher581.tools.pixelart.core;

import java.awt.Color;
import java.util.List;

import art.cipher581.commons.gui.util.IColorDistanceProvider;

/**
 *
 */
public class Pixel {

    private int x;

    private int y;

    private Color color;

    public Pixel(int x, int y, Color color) {
        super();

        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public static Pixel parseProperty(String property) {
        try {
            String[] splitted = property.split(";");

            int x = Integer.parseInt(splitted[0]);
            int y = Integer.parseInt(splitted[1]);

            Color color = null;
            if (splitted.length >= 3) {
                color = new Color(Integer.parseInt(splitted[2]));
            }

            Pixel pixel = new Pixel(x, y, color);

            return pixel;
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("Error while parsing pixel property: " + property, ex);
        }
    }

    public static String toProperty(Pixel pixel, boolean addColor) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(pixel.getX());
        stringBuilder.append(";");
        stringBuilder.append(pixel.getY());

        if (addColor) {
            stringBuilder.append(";");
            stringBuilder.append(pixel.getColor().getRGB());
        }

        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.x;
        hash = 53 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Pixel other = (Pixel) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    public static Pixel getNearestByColor(Pixel a, List<Pixel> pixels, IColorDistanceProvider distanceProvider) {
        Pixel nearest = null;
        double minDist = -1;

        if (pixels != null) {
            for (Pixel b : pixels) {

                double dist = distanceProvider.getDistance(a.getColor(), b.getColor());

                if (minDist == -1 || dist < minDist) {
                    nearest = b;
                    minDist = dist;
                }
            }
        }

        return nearest;
    }

}
