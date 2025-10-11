package simulator;

import java.util.HashMap;
import java.util.Map;

public class AIController {
    private Map<String, double[]> qTable = new HashMap<>();
    private double alpha = 0.1, gamma = 0.9, epsilon = 0.1;

    public int decide(double[] sensors) {
        String state = discretize(sensors);
        double[] q = qTable.getOrDefault(state, new double[]{0, 0, 0});
        if (Math.random() < epsilon) return (int) (Math.random() * 3);
        int best = 0;
        for (int i = 1; i < 3; i++) if (q[i] > q[best]) best = i;
        return best;
    }

    public void updateQ(double[] prev, int action, double reward, double[] next) {
        String prevState = discretize(prev);
        String nextState = discretize(next);

        double[] qPrev = qTable.getOrDefault(prevState, new double[]{0, 0, 0});
        double[] qNext = qTable.getOrDefault(nextState, new double[]{0, 0, 0});

        double maxNext = Math.max(qNext[0], Math.max(qNext[1], qNext[2]));
        qPrev[action] += alpha * (reward + gamma * maxNext - qPrev[action]);

        qTable.put(prevState, qPrev);
    }

    private String discretize(double[] sensors) {
        StringBuilder sb = new StringBuilder();
        for (double s : sensors) sb.append((int) (s / 10)).append("_");
        return sb.toString();
    }
}
