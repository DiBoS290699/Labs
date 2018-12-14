package function.threads;

import function.Functions;

@SuppressWarnings("ALL")
public class SimpleIntegrator implements Runnable {

    private Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for(int i = 0; i < task.getNumberOfOperation(); i++) {
            synchronized (task) {
                if (task.getFunction() == null) {
                    System.out.println(this.getClass().getName() + ": Null pointer");
                    continue;
                }
                double result =
                        Functions.integrate(task.getFunction(), task.getLeftX(), task.getRightX(), task.getDiscreteStep());
                System.out.println(task + " Result: " + result);
            }
        }
    }
}
