import java.util.Comparator;
import java.util.Objects;

public class Task {
    String name;
    int burstTime;
    State state; //ready running finished
    int cpuTime;
    int arrivalTime;

    Task(String name, String type, int burstTime) {
        this.burstTime = burstTime;
        this.name = name;
        this.cpuTime = 0;
        state = State.READY;
    }

    Task(String name, String type, int burstTime, int arrivalTime) {
        this.burstTime = burstTime;
        this.name = name;
        this.cpuTime = 0;
        state = State.READY;
        this.arrivalTime = arrivalTime;
    }

    void changeToRunning() {
        state = State.RUNNING;
    }


    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", burstTime=" + burstTime +
                ", state=" + state +
                ", cpuTime=" + cpuTime +
                ", arrivalTime=" + arrivalTime +
                "}";
    }
}

enum State {
    RUNNING,
    READY
}
