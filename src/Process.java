public class Process {
    private int burstTime;
    private int arrivalTime;
    private int waitTime;
    private int turnaroundTime;
    private String name;
    private boolean inQueue;

    public Process(Process p){
        name = p.name;
        arrivalTime = p.arrivalTime;
        burstTime = p.burstTime;
        waitTime = 0;
        turnaroundTime = 0;
        inQueue = false;
    }
    public Process(String n, int aTime, int bTime){
        name = n;
        arrivalTime = aTime;
        burstTime = bTime;
        waitTime = 0;
        turnaroundTime = 0;
        inQueue = false;
    }

    public String getName(){
        return name;
    }

    public int getWaitTime(){
        return waitTime;
    }

    public int getTurnaroundTime(){
        return turnaroundTime;
    }

    public int getArrivalTime(){
        return arrivalTime;
    }

    public int getBurstTime(){
        return burstTime;
    }

    public boolean getInQueue(){
        return inQueue;
    }

    public void setInQueue(boolean value){
        inQueue = value;
    }

    public void decrementBurstTime(){
        burstTime--;
    }

    public void incrementWaitTime(){
        waitTime++;
    }

    public void incrementTurnaroundTime(){
        turnaroundTime++;
    }

    public String toString(){
        return "Name: " + name + " Arrived: " + arrivalTime + " Burst: " + burstTime + "\n";
    }
}
