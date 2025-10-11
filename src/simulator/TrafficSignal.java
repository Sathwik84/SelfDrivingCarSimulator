package simulator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class TrafficSignal {
    private double x, y;
    private boolean redLight = true;
    private long lastToggle = 0;
    private long intervalMs;

    public TrafficSignal(double x, double y, long intervalMs) {
        this.x = x;
        this.y = y;
        this.intervalMs = intervalMs;
        this.lastToggle = System.currentTimeMillis();
    }

    public void update() {
        long now = System.currentTimeMillis();
        if (now - lastToggle > intervalMs) {
            redLight = !redLight;
            lastToggle = now;
        }
    }

    public boolean isRed() {
        return redLight;
    }

    public boolean isCarNear(double carX, double carY, double range) {
        return Math.abs(carX - x) < range && Math.abs(carY - y) < range;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(redLight ? Color.RED : Color.LIMEGREEN);
        gc.fillOval(x, y, 12, 12);
    }

    public double getX() { return x; }
    public double getY() { return y; }
}
