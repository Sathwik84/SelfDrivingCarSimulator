package simulator;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SelfDrivingCarSimulator extends Application {

    private Car car;
    private AIController ai;
    private List<Obstacle> obstacles = new ArrayList<>();
    private List<TrafficSignal> trafficSignals = new ArrayList<>();
    private FinishLine finishLine;
    private RewardFunction rewardFunction = new RewardFunction();

    private boolean simulationRunning = true;
    private double totalDistance = 0;
    private long startTime;

    private final long TIME_LIMIT_MS = 1 * 60 * 1000; // 1 minutes

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Canvas canvas = new Canvas(600, 600);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);
        stage.setTitle("Self-Driving Car Simulator");
        stage.setScene(scene);
        stage.show();

        car = new Car(130, 130);
        ai = new AIController();
        finishLine = new FinishLine(480, 480, 40, 20);

        generateRandomObstacles(5);   // 5 obstacles
        generateRandomTrafficSignals(4); // 4 traffic signals

        startTime = System.currentTimeMillis();

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!simulationRunning) return;

                long elapsedTime = System.currentTimeMillis() - startTime;

                // --- Time limit exceeded ---
                if (elapsedTime > TIME_LIMIT_MS) {
                    car = new Car(finishLine.getX() + finishLine.getWidth() / 2,
                                  finishLine.getY() + finishLine.getHeight() / 2);
                    simulationRunning = false;
                    showPopup();
                    draw(gc);
                    return;
                }

                // Update traffic signals
                for (TrafficSignal signal : trafficSignals) {
                    signal.update();
                }

                // Stop if near any red signal
                boolean stop = false;
                for (TrafficSignal signal : trafficSignals) {
                    if (signal.isRed() && signal.isCarNear(car.getX(), car.getY(), 30)) {
                        stop = true;
                        break;
                    }
                }

                if (!stop) {
                    double[] prevSensors = car.getSensorReadings(obstacles);
                    int action = ai.decide(prevSensors);
                    car.applyAction(action);

                    double dx = car.getSpeed() * Math.cos(Math.toRadians(car.getAngle()));
                    double dy = car.getSpeed() * Math.sin(Math.toRadians(car.getAngle()));
                    totalDistance += Math.sqrt(dx * dx + dy * dy);

                    car.update(obstacles);
                    double[] nextSensors = car.getSensorReadings(obstacles);

                    double reward = rewardFunction.calculateReward(
                            nextSensors,
                            car.getX(),
                            car.getY(),
                            finishLine.getX(),
                            finishLine.getY()
                    );

                    ai.updateQ(prevSensors, action, reward, nextSensors);
                }

                // Check finish line
                if (finishLine.contains(car.getX(), car.getY())) {
                    simulationRunning = false;
                    showPopup();
                }

                draw(gc);
            }
        }.start();
    }

    private void generateRandomObstacles(int count) {
        Random rand = new Random();
        obstacles.clear();

        for (int i = 0; i < count; i++) {
            double x = 120 + rand.nextInt(360);
            double y = 120 + rand.nextInt(360);
            double w = 30 + rand.nextInt(40);
            double h = 20 + rand.nextInt(40);
            obstacles.add(new Obstacle(x, y, w, h));
        }
    }

    private void generateRandomTrafficSignals(int count) {
        Random rand = new Random();
        trafficSignals.clear();

        for (int i = 0; i < count; i++) {
            double x, y;
            boolean overlap;
            do {
                overlap = false;
                x = 120 + rand.nextInt(360);
                y = 120 + rand.nextInt(360);
                for (TrafficSignal s : trafficSignals) {
                    if (Math.abs(s.getX() - x) < 50 && Math.abs(s.getY() - y) < 50) {
                        overlap = true;
                        break;
                    }
                }
            } while (overlap);
            trafficSignals.add(new TrafficSignal(x, y, 5000)); // 5 sec toggle
        }
    }

    private void showPopup() {
        long elapsed = System.currentTimeMillis() - startTime;
        double timeSecs = elapsed / 1000.0;
        double avgSpeed = totalDistance / timeSecs;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Simulation Complete");
        alert.setHeaderText("Car reached the DESTINATION POINT!");
        alert.setContentText("Total Distance: " + String.format("%.2f", totalDistance) + " units\n"
                + "Average Speed: " + String.format("%.2f", avgSpeed) + " units/sec");
        alert.show();
    }

    private void draw(GraphicsContext gc) {
        gc.setFill(Color.DARKGREEN);
        gc.fillRect(0, 0, 600, 600);

        // Road
        gc.setFill(Color.GRAY);
        gc.fillRect(100, 100, 400, 400);

        // Lane dividers
        gc.setStroke(Color.WHITE);
        gc.setLineDashes(10);
        for (int i = 1; i < 4; i++) {
            gc.strokeLine(100 + i * 100, 100, 100 + i * 100, 500);
            gc.strokeLine(100, 100 + i * 100, 500, 100 + i * 100);
        }
        gc.setLineDashes(0);

        // U-Turns
        gc.setStroke(Color.YELLOW);
        gc.strokeArc(100, 80, 400, 40, 0, 180, null);   // Top U-turn
        gc.strokeArc(100, 480, 400, 40, 180, 180, null); // Bottom U-turn

        // Traffic Signals
        for (TrafficSignal signal : trafficSignals) {
            signal.draw(gc);
        }

        // Finish Line
        finishLine.draw(gc);
        gc.setFill(Color.BLACK);
        gc.fillText("FINISH", 490, 495);

        // Obstacles
        for (Obstacle ob : obstacles) {
            ob.draw(gc);
        }

        // Car
        car.draw(gc);
    }

    public static void main(String[] args) {
        launch();
    }
}
