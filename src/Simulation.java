import java.util.*;
import java.io.*;

public class Simulation {
    public static void main(String[] args){
        LinkedList<Process> processList = new LinkedList();
        int numElems;
        int option = 0;
        boolean fileOpened = false;
        boolean flag = false;
        Scanner keyboard = new Scanner(System.in);
        Scanner inputFile = null;

        while(!flag) {
            System.out.println("Choose from the following options:\n"
                    + "1. Read input from file\n"
                    + "2. Randomly generate input based on user specifications\n");
            option = keyboard.nextInt();
            if (option != 1 && option != 2) {
                System.out.println("Invalid input.");
                System.out.println("Choose from the following options:\n"
                    + "1. Read input from file\n"
                    + "2. Randomly generate input based on user specifications\n");
            option = keyboard.nextInt();
        }
            else if(option == 1){
                while (!fileOpened) {
                    try {
                        System.out.println("Enter file name to be read: ");
                        inputFile = new Scanner(new File(keyboard.next()));
                        fileOpened = true;
                    } catch (FileNotFoundException e) {
                        System.out.println("--- File Not Found! ---");
                        fileOpened = false;
                    }
                }
                createProcessListFromFile(inputFile, processList);
                LinkedList<Process> processList2 = copyList(processList);
                System.out.println("\n-------------------\n"
                        + "Shortest Job First\n" + "-------------------\n");
                SJF(processList);
                System.out.println("\n-------------------\n"
                        + "Round Robin\n" + "-------------------\n");
                RR(processList2);
                flag = true;
            }
            else{
                System.out.println("Enter how many processes: (Between 1 and 20)");
                numElems = keyboard.nextInt();
                createProcessList(processList, numElems);
                LinkedList<Process> processList2 = copyList(processList);
                System.out.println("\n-------------------\n"
                        + "Shortest Job First\n" + "-------------------\n");
                SJF(processList);
                System.out.println("\n-------------------\n"
                        + "Round Robin\n" + "-------------------\n");
                RR(processList2);
                flag = true;
            }

        }
    }

    public static void createProcessList(LinkedList<Process> processList, int numElems){
        for(int i = 0; i < numElems; i++){
            int arrivalTime = randInt();
            int burstTime = randInt();
            Process p = new Process ("p" + i, arrivalTime, burstTime);
            int size = processList.size();
            if(i == 0){
                processList.add(p);
            }
            else{
                for (int j = 0; j < size; j++) {
                    if(processList.get(j).getArrivalTime() > p.getArrivalTime()){
                        processList.add(j, p);
                        break;
                    }
                    else if(j == size - 1){
                        processList.add(p);
                    }
                }
            }
        }
        System.out.println(processList);
    }

    public static void createProcessListFromFile(Scanner inputFile, LinkedList<Process> processList){
        int processNum = 0;
        while(inputFile.hasNextLine()){
            Process p = new Process ("p" + processNum, inputFile.nextInt(), inputFile.nextInt());
            int size = processList.size();
            if(processNum == 0){
                processList.add(p);
            }
            else{
                for (int i = 0; i < size; i++) {
                    if(processList.get(i).getArrivalTime() > p.getArrivalTime()){
                        processList.add(i, p);
                        break;
                    }
                    else if(i == size - 1){
                        processList.add(p);
                    }
                }
            }
            processNum++;
        }
        System.out.println(processList);
    }

    public static LinkedList<Process> copyList(LinkedList<Process> processList){
        LinkedList<Process> copy = new LinkedList<Process>();
        for(int i = 0; i < processList.size(); i++){
            Process p = new Process(processList.get(i));
            copy.add(p);
        }
        return copy;
    }

    public static int randInt(){
        Random rand = new Random();

        int randomInt = rand.nextInt((10 - 1) + 1) + 1;
        return randomInt;
    }

    //Shortest Job First Method
    public static void SJF(LinkedList<Process> processList){
        LinkedList<Process> workingList = processList;
        //Organize the list to execute the shortest processes first
        for(int i = 0; i < processList.size(); i++){
            Process p = processList.get(i);
            for(int j = i; j < processList.size(); j++){
                if(p.getArrivalTime() == processList.get(j).getArrivalTime() && p.getBurstTime() > processList.get(j).getBurstTime()){
                    processList.set(i, processList.get(j));
                    processList.set(j, p);
                }
            }
        }

        //Counter of total time passed
        int totalTime = 0;

        //Outer loop works until the list is empty. Thus a process is removed from the list when it is completed
        while(!processList.isEmpty()){
            //How long each process runs for
            int processTime = 0;

            //This loop counts how long each process that has entered the ready queue waits
            // before being accepted into the running queue
            while(totalTime < processList.peek().getArrivalTime()){
                for(int i = 1; i < processList.size(); i++){
                    if(processList.get(i).getArrivalTime() <= totalTime) {
                        processList.get(i).incrementTurnaroundTime();
                        processList.get(i).incrementWaitTime();
                    }
                }
                totalTime++;
            }

            //This loop counts how long the process runs for in the running queue
            //It also continues to count how long processes in the ready queue are waiting to enter the running queue
            while(processTime < processList.peek().getBurstTime()){
                processList.peek().incrementTurnaroundTime();
                for(int i = 1; i < processList.size(); i++){
                    if(processList.get(i).getArrivalTime() <= totalTime) {
                        processList.get(i).incrementTurnaroundTime();
                        processList.get(i).incrementWaitTime();
                    }
                }
                processTime++;
                totalTime++;
            }
            //Output the process name, the wait time, and turnaround time
            System.out.println("Process: " + processList.peek().getName() + "\nWait Time:" + processList.peek().getWaitTime() + "\nTurnaround Time: "
                    + processList.peek().getTurnaroundTime());
            System.out.println("Total Time passed: " + totalTime + "\n");

            //Remove the process because it has been completed
            processList.pop();
        }
    }

    public static void RR(LinkedList<Process> processList){
        System.out.println(processList);
        int timeQuantum;
        int totalTime = 0;
        Scanner keyboard = new Scanner(System.in);
        System.out.println("Enter the time quantum: ");
        timeQuantum = keyboard.nextInt();
        Queue<Process> processQueue = new LinkedList<Process>();
        boolean done = false;

        while(!processList.isEmpty()){
            for(int i = 0; i < processList.size(); i++){
                if(processList.get(i).getArrivalTime() == totalTime && done == false){
                    processQueue.add(processList.get(i));
                    processList.get(i).setInQueue(true);
                    done = true;
                }
            }
            int processTime = 0;
            if(!processQueue.isEmpty()){
                while(processTime < timeQuantum  && !processQueue.isEmpty()) {
                    processQueue.peek().incrementTurnaroundTime();
                    processQueue.peek().decrementBurstTime();
                    processTime++;
                    totalTime++;
                    for(int i = 0; i < processList.size(); i++){
                        if(processList.get(i).getInQueue() && !processList.get(i).getName().equals(processQueue.peek().getName())){
                            processList.get(i).incrementWaitTime();
                            processList.get(i).incrementTurnaroundTime();
                        }
                        if(processList.get(i).getArrivalTime() == totalTime && processList.get(i).getInQueue() == false){
                            processQueue.add(processList.get(i));
                            processList.get(i).setInQueue(true);
                        }
                    }
                    if (processQueue.peek().getBurstTime() == 0) {
                        System.out.println("Process: " + processQueue.peek().getName() + "\nWait Time:" + processQueue.peek().getWaitTime() + "\nTurnaround Time: "
                                + processQueue.peek().getTurnaroundTime());
                        for(int i = 0; i < processList.size(); i++) {
                            if (processQueue.peek().getName().equals(processList.get(i).getName()))
                                processList.remove(i);
                        }
                        processQueue.remove();
                    }
                    else if(processTime == timeQuantum){
                        processQueue.add(processQueue.peek());
                        processQueue.remove();
                    }
                }
            }
            else{
                totalTime++;
            }
        }
    }
}