import function.Functions;
import function.basic.*;
import function.threads.*;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //nonThreads();
        //simpleThreads();
        complicatedThreads();
    }

    public static void nonThreads() {
        Task task = new Task();
        Random rand = new Random();
        task.setNumberOfOperation(100);
        for(int i = 0; i < 150;  ++i) {
            double base = rand.nextDouble() * 10 + 1;
            task.setFunction(new Log(base));
            task.setLeftX(rand.nextDouble() * 100);
            task.setRightX(rand.nextDouble() * 100 + 100);
            task.setDiscreteStep(rand.nextDouble());
            System.out.print("Source: " + task);
            double integrate =
                    Functions.integrate(task.getFunction(), task.getLeftX(), task.getRightX(), task.getDiscreteStep());
            System.out.println("Result: " + integrate + '\n');
        }
    }

    public static void nonThreads(Task task) {
        System.out.print("Source: " + task);
        double integrate =
                Functions.integrate(task.getFunction(), task.getLeftX(), task.getRightX(), task.getDiscreteStep());
        System.out.print("Result: " + integrate);
    }

    public static void simpleThreads() {
        Task task = new Task();
        task.setNumberOfOperation(1000);
        SimpleGenerator generator = new SimpleGenerator(task);
        SimpleIntegrator integrator = new SimpleIntegrator(task);
        Thread generatorThread = new Thread(generator);
        Thread integratorThread = new Thread(integrator);
        generatorThread.setPriority(Thread.MAX_PRIORITY);
        integratorThread.setPriority(Thread.MIN_PRIORITY);

        generatorThread.start();
        integratorThread.start();
    }

    public static void complicatedThreads() throws InterruptedException {
        Task task = new Task();
        task.setNumberOfOperation(100);

        Semaphore semaphore = new Semaphore(1);
        Generator generator = new Generator(task, semaphore);
        Integrator integrator = new Integrator(task, semaphore);
        generator.setPriority(Thread.MAX_PRIORITY);
        integrator.setPriority(Thread.MIN_PRIORITY);

        generator.start();
        integrator.start();

//        Thread.sleep(50);
//        generator.interrupt();
//        integrator.interrupt();
    }
}
