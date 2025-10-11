package simulator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Obstacle {
    private double x, y, w, h;

    public Obstacle(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public boolean contains(double px, double py) {
        return px >= x && px <= x + w && py >= y && py <= y + h;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(x, y, w, h);
    }
}
