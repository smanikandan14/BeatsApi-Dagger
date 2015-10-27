package mani.beatsalbumsearch.test.util;

import java.util.concurrent.Executor;

/**
 * Used for testing.
 */
public class SynchronousExecutor implements Executor {
    @Override public void execute(Runnable runnable) {
        runnable.run();
    }
}
