package simulator;

public class RewardFunction {

    private double lastDistanceToFinish = Double.MAX_VALUE;

    /**
     * Calculate reward based on sensors and distance to finish line.
     *
     * @param sensors  Sensor readings (left, front, right)
     * @param carX     Current car X position
     * @param carY     Current car Y position
     * @param finishX  Finish line X
     * @param finishY  Finish line Y
     * @return reward value
     */
    public double calculateReward(double[] sensors, double carX, double carY,
                                  double finishX, double finishY) {

        double reward = 0.0;

        // --- 1. Collision penalty ---
        double minSensor = Math.min(sensors[0], Math.min(sensors[1], sensors[2]));
        if (minSensor < 15) {
            return -1.0; // immediate penalty for being too close to an obstacle
        }

        // --- 2. Distance-based progress reward ---
        double distToFinish = Math.hypot(finishX - carX, finishY - carY);

        if (distToFinish < lastDistanceToFinish) {
            reward += 0.5; // moving closer
        } else {
            reward -= 0.2; // moving away
        }

        lastDistanceToFinish = distToFinish;

        // --- 3. Small living reward to encourage movement ---
        reward += 0.1;

        return reward;
    }
}
