package function.threads;

import function.basic.*;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Generator extends Thread {

    private Task task;
    private Semaphore semaphore;

    public Generator(Task t, Semaphore s) {
        this.task = t;
        this.semaphore = s;
    }

    @Override
    public void run() {
        Random rand = new Random();
        int numberOfOperation = task.getNumberOfOperation();
        for(int i = 0; i < numberOfOperation; ++i) {

            if(this.isInterrupted()) {
                System.out.println("Generator is interrupted.");
                break;
            }

            try {

                semaphore.acquire();

                //noinspection Duplicates
                switch (rand.nextInt(5)) {
                    case 0: {
                        task.setFunction(new Cos());
                        break;
                    }
                    case 1: {
                        task.setFunction(new Log(rand.nextDouble() * 10 + 1));
                        break;
                    }
                    case 2: {
                        task.setFunction(new Sin());
                        break;
                    }
                    case 3: {
                        task.setFunction(new Tan());
                        break;
                    }
                    default:
                        task.setFunction(new Exp());
                }
                task.setLeftX(rand.nextDouble() * 100);
                task.setRightX(rand.nextDouble() * 100 + 100);
                task.setDiscreteStep(rand.nextDouble());
                System.out.println(task);


                semaphore.release();
            } catch (InterruptedException e) {
                //e.printStackTrace();
                break;
            }
        }
    }
}
