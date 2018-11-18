package Chapter5;

import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * <p>
 *      {@link TwinsLock} 测试类
 * </p>
 *
 * @author getthrough
 * @date 18-11-18
 */
public class TwinsLockTest {
    @Test
    public void test() {
        final Lock lock = new TwinsLock();
        class Worker extends Thread {
            @Override
            public void run() {
                while (true) {
                    lock.lock();
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        System.out.println("currentThreadName : " + Thread.currentThread().getName());
                        // TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
        // 启动 10 个线程
        for (int i = 0; i < 10; i++) {
            Worker worker = new Worker();
            // main 线程死亡后 worker 线程跟着死亡
            worker.setDaemon(true);
            worker.start();
        }
        // 每隔 1 秒换行
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
                System.out.println();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
