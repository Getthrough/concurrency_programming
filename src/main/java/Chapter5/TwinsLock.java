package Chapter5;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * <p>
 *      同步工具类。同一时刻只允许至多两个线程访问，其余线程阻塞。
 * </p>
 *
 * @author getthrough
 * @date 18-11-18
 */
public class TwinsLock implements Lock {
    // 并发访问线程数量
    private static int CONCURR_THREAD_COUNT = 2;
    // 同步器代理对象
    private final Sync sync = new Sync(CONCURR_THREAD_COUNT);

    @Override
    public void lock() {
        sync.tryAcquireShared(1);
    }

    @Override
    public void unlock() {
        sync.tryReleaseShared(1);
    }

    /**
     * 自定义同步器
     */
    private static final class Sync extends AbstractQueuedSynchronizer {
        // constructor
        Sync(int count) {
            if (count <= 0)
                throw new IllegalArgumentException("count must greater than zero.");
            // set synchronization state
            setState(count);
        }

        /**
         * 获取共享锁
         * 注：书上该方法有个错误：
         *  if (newCount < 0 || compareAndSetState(currentCount, newCount))
         * @param reduceCount 允许并发的线程减少数量
         * @return
         */
        public int tryAcquireShared(int reduceCount) {
            for (;;) {// 自旋 & CAS
                int currentCount = getState();
                int newCount = currentCount - reduceCount;
                if (newCount >= 0 && compareAndSetState(currentCount, newCount)) {
                    return newCount;
                }
            }
        }

        /**
         * 释放共享锁
         * @param returnCount 释放锁后允许并发访问线程的增加数量
         * @return
         */
        public boolean tryReleaseShared(int returnCount) {
            for(;;) {// 自旋 & CAS
                int currentCount = getState();
                int newCount = currentCount + returnCount;
                if (compareAndSetState(currentCount, newCount)) {
                    return true;
                }
            }
        }

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
