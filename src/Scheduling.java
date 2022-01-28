
import java.util.ArrayList;
import java.util.Scanner;

public class Scheduling {

    //track time spent during scheduling
    static int totalTime = 1;

    // Cpu is given to tasks according their burst time
    //Shortest-Job-First
    static void SJF(ArrayList<Task> queue) {
        System.out.println(Color.YELLOW_BOLD_BRIGHT+"\nShortest Job First Scheduling...\n"+Color.RESET);

        //Sorting the readyQueue
        sortByBurstTime(queue);

        //give cpu to all tasks in ready queue according to rules
        GiveCpuToListByFIFO(queue);
    }

    // Cpu is given to tasks according to their arrival time
    // First-Come, First-Served
    static void FCFS(ArrayList<Task> queue) {

        totalTime = 1;

        System.out.println(Color.YELLOW_BOLD_BRIGHT+"\nFirst Come First Serve Scheduling...\n"+Color.RESET);

        //give cpu to all tasks in ready queue according to rules(FIFO)
        GiveCpuToListByFIFO(queue);
    }

    //Cpu is given to ready task turn by turn for a limited time slice.
    //Round-Robin
    static void RR(ArrayList<Task> queue) {

        System.out.println(Color.YELLOW_BOLD_BRIGHT+"\nRound Robin Scheduling with quantum 1 ...\n"+Color.RESET);

        // time slice
        int quantum = 1;

        //give cpu to all tasks in ready queue according to rules
        while (!queue.isEmpty()) {

            Task task = queue.get(0);

            //decide the amount of time task has cpu
            task.cpuTime = Math.min(quantum, task.burstTime);
            task.state = State.RUNNING;

            //give cpu to task
            for (int t = 0; t < task.cpuTime; t++) {
                task.burstTime--;
                printSituation(queue, task);
                totalTime++;
            }

            //check to see if the task is done, if not,
            // it adds to end of queue to get Cpu in next turn
            if (task.burstTime > 0) {
                task.state = State.READY;
                queue.add(task);
            }
            queue.remove(0);
        }
    }

    static void HRRN(ArrayList<Task> queue) {

        System.out.println(Color.YELLOW_BOLD_BRIGHT+"Highest Response Ratio next Scheduling...\n"+Color.RESET);

        int burstTimes = 0, i, t;

        // queue is sorted by arrival time

        //calculate time cpu is busy with task
        for (Task task : queue) {
            burstTimes += task.burstTime;
        }

        for (t = queue.get(0).arrivalTime; t < burstTimes; ) {
            int ratio = Integer.MIN_VALUE;

            int temp;
            int index = 0;

            // Find highest response ratio
            for (i = 0; i < queue.size(); i++) {

                //check if task i has arrived
                if (queue.get(i).arrivalTime <= t) {

                    // Response Ratio= (waiting time + burst time)/burst time
                    // Waiting time = all time - arrival time
                    temp = (queue.get(i).burstTime + (t - queue.get(i).arrivalTime)) / queue.get(i).burstTime;

                    //choose the task to give cpu to
                    if (ratio < temp) {
                        ratio = temp;
                        index = i;
                    }
                }
            }

            //update time spent on tasks
            t += queue.get(index).burstTime;
            giveCpuInEachQuantumByBurstTime(queue, queue.get(index));
            queue.remove(index);
        }

    }

    //Sort readyQueue based on burst time
    static void sortByBurstTime(ArrayList<Task> queue){
        int n = queue.size();

        for (int i = 0; i < n - 1; i++) {

            // Find the minimum element in unsorted array
            int minIndex = i;
            for (int j = i + 1; j < n; j++) {
                if (queue.get(j).burstTime < queue.get(minIndex).burstTime) {
                    minIndex = j;
                }
            }

            // Swap the minimum with the first element
            queue.add(i, queue.get(minIndex));
            queue.remove(minIndex + 1);
        }
    }

    // Give CPU to tasks according to queue order
    private static void GiveCpuToListByFIFO(ArrayList<Task> queue) {
        while (!queue.isEmpty()) {
            Task task = queue.remove(0);
            task.changeToRunning();

            giveCpuInEachQuantumByBurstTime(queue, task);
        }
    }

    // Give CPU to tasks in each quantum based on their burst time
    private static void giveCpuInEachQuantumByBurstTime(ArrayList<Task> queue, Task task){
        //give cpu to task
        for (int t = 0; t < task.burstTime; t++) {
            printSituation(queue, task);
            totalTime++;
        }
    }

    // Print current cpu task and ready queue tasks
    private static void printSituation(ArrayList<Task> queue, Task task){
        System.out.println(Color.GREEN_BOLD_BRIGHT + "At " + totalTime + "s " + task.name + " has CPU");
        System.out.println(task);
        System.out.println(Color.BLUE_BOLD_BRIGHT + "Ready queue at the moment:");
        System.out.println(queue + "\n" + Color.RESET);
    }

    public static void main(String[] args) {
        Scanner scanner;
        int n; // number of tasks
        ArrayList<Task> readyQueue = new ArrayList<>();

        scanner = new Scanner(System.in);
        n = scanner.nextInt();

        scanner.nextLine();

        //get tasks and their details
        for (int i = 0; i < n; i++) {

            String s = scanner.nextLine();
            String[] parts = s.split(" ");

            // Arrival time will be the order index
            Task task = new Task(parts[0], parts[1], Integer.parseInt(parts[2]), i);

            readyQueue.add(task);
        }

        // SCHEDULING ALGORITHMS
        System.out.println(Color.PURPLE_BOLD_BRIGHT + "To Choose Time Scheduling Enter: ");
        System.out.println(Color.WHITE_BOLD_BRIGHT + "1 -> Shortest Job First");
        System.out.println("2 -> First-Come First-Served");
        System.out.println("3 -> Round Robin");
        System.out.println("4 -> Highest Response Ratio next" + Color.RESET);

        int m = scanner.nextInt();
        switch (m) {
            case 1: {
                SJF(readyQueue);
                break;
            }
            case 2: {
                FCFS(readyQueue);
                break;
            }
            case 3: {
                RR(readyQueue);
                break;
            }
            case 4: {
                HRRN(readyQueue);
                break;
            }
            default: {
                System.out.println("Please enter a valid number!");
                break;
            }
        }
    }
}