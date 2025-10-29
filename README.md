**TOTAL PROJECT PROGRAMS ARE IN src/Simulator folder in this Repository**

# 🚗 Self-Driving Car Simulator (JavaFX)

A JavaFX-based **Self-Driving Car Simulation** environment that demonstrates basic **autonomous driving**, **reinforcement learning**, and **traffic signal interaction**.  
The simulator includes a car controlled by an AI agent that navigates around randomly placed obstacles and obeys traffic signals while learning to reach a finish line efficiently.

---

## 🧠 Features

- **AI-controlled car** powered by reinforcement learning (`AIController`)
- **Dynamic traffic signals** that switch between red and green
- **Obstacle detection** via virtual sensors (left, front, right)
- **Reward-based learning** using distance and collision feedback
- **Randomized environment** with configurable obstacles and signals
- **Interactive visualization** using JavaFX Canvas
- **Automatic simulation timing and scoring**

---

## 📂 Project Structure

| File | Description |
|------|--------------|
| `SelfDrivingCarSimulator.java` | Main simulation class that initializes the JavaFX environment, manages the car, obstacles, signals, and reward system. |
| `RewardFunction.java` | Defines how the AI agent is rewarded or penalized based on progress, collisions, and distance to the finish line. |
| `TrafficSignal.java` | Represents individual traffic lights that toggle between red and green and detect nearby cars. |
| *(Other classes, e.g. `Car`, `AIController`, `Obstacle`, `FinishLine`)* | Support modules handling vehicle control, environment layout, and AI decision-making logic. |

---

## ⚙️ Requirements

- **Java 17+**
- **JavaFX SDK** installed and added to your classpath  
  (Download from [https://openjfx.io](https://openjfx.io))

---

## 🏗️ Setup & Run

1. **Clone the repository**

   ```bash
   git clone https://github.com/<your-username>/SelfDrivingCarSimulator.git
   cd SelfDrivingCarSimulator
