package com.placement.service;

import com.placement.model.Student;

import java.util.List;

/**
 * UNIT-IV: Multithreading & Concurrency.
 * - Creates Threads using Runnable interface
 * - Thread Lifecycle (NEW, RUNNABLE, TIMED_WAITING, TERMINATED)
 * - Synchronization: Thread-safe counter to avoid race conditions
 */
public class NotificationService implements Runnable {

    private final Student student;
    private final String companyName;
    private static int totalDispatched = 0;

    // Object for synchronization lock
    private static final Object lock = new Object();

    public NotificationService(Student student, String companyName) {
        this.student = student;
        this.companyName = companyName;
    }

    @Override
    public void run() {
        // Simulates concurrent background notification delivery
        try {
            System.out.printf(" [Thread: %-15s] Dispatching interview ticket to %-12s (%s)...
",
                    Thread.currentThread().getName(), student.getName(), student.getEmail());

            // Simulate realistic network/processing delay (Thread lifecycle: TIMED_WAITING)
            Thread.sleep(400);

            // Unit-IV: Synchronization block ensuring atomic count increment
            synchronized (lock) {
                totalDispatched++;
            }

            System.out.printf(" [Thread: %-15s] ✓ Sent! Hall ticket delivered to %s.
",
                    Thread.currentThread().getName(), student.getRollNo());

        } catch (InterruptedException e) {
            System.err.println("Notification thread interrupted for " + student.getName());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Launches parallel worker threads for each shortlisted candidate.
     */
    public static void dispatchParallelNotifications(List<Student> shortlisted, String companyName) {
        System.out.println("
--- Initiating Multithreaded Notification Dispatch (Unit-IV) ---");
        totalDispatched = 0;

        Thread[] threads = new Thread[shortlisted.size()];

        for (int i = 0; i < shortlisted.size(); i++) {
            Student s = shortlisted.get(i);
            NotificationService task = new NotificationService(s, companyName);
            // Create Thread with custom descriptive name
            threads[i] = new Thread(task, "NotifyWorker-" + (i + 1));
            threads[i].start(); // Transition: NEW -> RUNNABLE
        }

        // Wait for all threads to finish using join()
        for (Thread t : threads) {
            try {
                t.join(); // Ensures main thread waits for worker completion
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("--- All " + totalDispatched + " notifications dispatched successfully! ---
");
    }

    public static int getTotalDispatched() {
        synchronized (lock) {
            return totalDispatched;
        }
    }
}
