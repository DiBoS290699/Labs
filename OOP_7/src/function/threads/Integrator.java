package function.threads;

import function.Functions;

import java.util.concurrent.Semaphore;

public class Integrator extends Thread {

    private Task task;
    private Semaphore semaphore;

    public Integrator(Task t, Semaphore s) {
        this.task = t;
        this.semaphore = s;
    }

    @Override
    public void run() {
        for(int i = 0; i < task.getNumberOfOperation(); i++) {
            if(this.isInterrupted()) {
                System.out.println("Integrator is interrupted.");
                break;
            }
            try {
                semaphore.acquire();

                if (task.getFunction() == null) {
                    System.out.println(this.getClass().getName() + ": Null pointer");
                    semaphore.release();
                    continue;
                }
                double result =
                        Functions.integrate(task.getFunction(), task.getLeftX(), task.getRightX(), task.getDiscreteStep());
                System.out.println(task + " Result: " + result);


                semaphore.release();
            } catch (InterruptedException e) {
                //e.printStackTrace();
                break;
            }
        }
    }
}
