package simulator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FinishLine {
    private double x, y, w, h;

    public FinishLine(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean contains(double px, double py) {
        return px >= x && px <= x + w && py >= y && py <= y + h;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRect(x, y, w, h);
    }

    // Add these getter methods
    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return w; }
    public double getHeight() { return h; }
}
