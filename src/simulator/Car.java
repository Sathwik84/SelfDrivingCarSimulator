package simulator;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.List;

public class Car {
    private double x, y, angle, speed;
    private Image carImage;

    public Car(double x, double y) {
        this.x = x;
        this.y = y;
        this.angle = 0;
        this.speed = 2;
        try {
            carImage = new Image(getClass().getResourceAsStream("car.png"));
        } catch (Exception e) {
            carImage = null;
        }
    }

    public void applyAction(int action) {
        if (action == 0) angle -= 10;
        else if (action == 2) angle += 10;
    }

    public void update(List<Obstacle> obstacles) {
        double oldX = x, oldY = y;
        x += speed * Math.cos(Math.toRadians(angle));
        y += speed * Math.sin(Math.toRadians(angle));

        for (Obstacle ob : obstacles) {
            if (ob.contains(x, y)) {
                x = oldX;
                y = oldY;
                angle += 180;
            }
        }

        // Road boundary check
        if (x < 100 || x > 500 || y < 100 || y > 500) {
            x = oldX;
            y = oldY;
            angle += 180;
        }
    }

    public void draw(GraphicsContext gc) {
        if (carImage != null) {
            gc.save();
            gc.translate(x, y);
            gc.rotate(angle);
            gc.drawImage(carImage, -20, -10, 40, 20);
            gc.restore();
        } else {
            gc.setFill(Color.RED);
            gc.fillOval(x - 10, y - 10, 20, 20);
        }

        gc.setStroke(Color.BLUE);
        for (double sensorAngle : new double[]{-45, 0, 45}) {
            double sx = x + 100 * Math.cos(Math.toRadians(angle + sensorAngle));
            double sy = y + 100 * Math.sin(Math.toRadians(angle + sensorAngle));
            gc.strokeLine(x, y, sx, sy);
        }
    }

    public double[] getSensorReadings(List<Obstacle> obstacles) {
        double[] readings = new double[3];
        int idx = 0;
        for (double sa : new double[]{-45, 0, 45}) {
            double dist = 100;
            for (int d = 0; d < 100; d++) {
                double sx = x + d * Math.cos(Math.toRadians(angle + sa));
                double sy = y + d * Math.sin(Math.toRadians(angle + sa));
                for (Obstacle ob : obstacles) {
                    if (ob.contains(sx, sy)) {
                        dist = d;
                        break;
                    }
                }
                if (dist < 100) break;
            }
            readings[idx++] = dist;
        }
        return readings;
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public double getAngle() { return angle; }
    public double getSpeed() { return speed; }

    // ⬅️ NEW: allow teleporting the car (used for timeout auto-finish)
    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
