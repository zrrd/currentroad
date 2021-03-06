package cn.leran.currentroad.chapter2;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义的线程池.
 *
 * @author shaoyijiong
 * @date 2018/7/17
 */
public class Thread7 {


  /**
   * 自定义线程池
   */
  private void threadPoolExecutor() {

    // 设定线程名
    ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("pool-%d").build();

    // 线程池的所有参数
    // corePoolSize 核心线程池数量(就算没有任务执行 , 也会保留在线程池中的可用线程数)
    // maximumPoolSize 最大的线程数量(线程超过corePoolSize后没有任务执行过一定时间后(keepAliveTime) , 就会被回收)
    // keepAliveTime 超过corePoolSize的线程的线程存活时间(用于回收超出corePoolSize的无任务线程的时间)
    // unit keepAliveTime的单位
    // workQueue(BlockQueue的接口对象) 任务队列,被提交但未被执行的任务队列
    // threadFactory 线程工厂用于创建线程
    // handler 拒绝策略 当任务太多来不及处理,如何拒绝任务
    // 1.DiscardPolicy 直接丢弃 2.DiscardOldestPolicy 丢弃队列中最老的任务 3.AbortPolicy 抛异常(默认) 4.CallerRunsPolicy 将任务分给调用线程来执行
    ThreadPoolExecutor pool = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<>(1024),
        nameThreadFactory, new ThreadPoolExecutor.AbortPolicy());


    // 核心线程是否会过期
    pool.allowCoreThreadTimeOut(true);


    //Executor不会停止 需要shutdown来显式停止等待当前任务完成  shutdownNow会终止所有正在执行的任务并立即关闭
    pool.shutdown();
  }


  public static void main(String[] args) {

    //可变线程池 复用空闲线程 没有可用线程的话新建线程
    Executors.newCachedThreadPool();
    //具体实现 核心线程池初始化为0  最大为Integer的最大
    new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>());

    //固定线程池
    Executors.newFixedThreadPool(10);
    //具体实现  核心线程池和最大线程池都是固定的
    new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    //单个线程池
    Executors.newSingleThreadExecutor();
    //具体实现 把核心线程池和最大线程池的数量为1
    new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    //线程池数量为1 定期执行
    ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    //定期执行 Runnable 时间 单位

    //后续的任务在period后执行  等待上个任务执行完毕
    scheduledExecutorService.scheduleAtFixedRate(() -> System.out.println("hello"), 0, 2, TimeUnit.SECONDS);
    //等待上个任务结束后再执行 执行延迟2
    scheduledExecutorService.scheduleWithFixedDelay(() -> System.out.println("hello"), 0, 2, TimeUnit.SECONDS);
    //线程池数量自定义 定期执行
    ScheduledExecutorService scheduledExecutorService1 = Executors.newScheduledThreadPool(10);
    //10个定时任务同时执行
    for (int i = 0; i < 10; i++) {
      scheduledExecutorService1.scheduleWithFixedDelay(() -> System.out.println("hello"), 0, 2, TimeUnit.SECONDS);
    }
  }
}
