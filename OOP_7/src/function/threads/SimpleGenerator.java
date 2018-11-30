package function.threads;

import function.basic.*;

import java.util.Random;

@SuppressWarnings("ALL")
public class SimpleGenerator implements Runnable {

    private Task task;

    public SimpleGenerator(Task t) {
        this.task = t;
    }

    @Override
    public void run() {
        Random rand = new Random();
        int numberOfOperation = task.getNumberOfOperation();
        for(int i = 0; i < numberOfOperation; ++i) {
            synchronized (task) {
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
            }
        }
    }
}
