package app.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A timer wrapper for scheduling tasks with a delay. Used for dynamically displaying feedbacks.
 */

public class Delay extends Timer {
    public Delay(Runnable runnable, int delayMs) {
        super.schedule(
            new TimerTask() {
                @Override
                public void run() {
                    runnable.run();
                }
        }, delayMs);
    }
}
