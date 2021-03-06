package cn.leran.currentroad.chapter1;

/**
 * <pre>
 * 当一个线程调用一个共享变量的wait()方法时 , 该线程会被阻塞挂起 , 直到
 * 1. 其他线程调用了该共享变量的notify() 或者 notifyAll() 方法
 * 2. 其他线程调用了该线程的interrupt() 方法 该线程抛出InterruptedException 异常
 * 如果调用wait()方法前没有获取共享变量的监视器锁 也会抛异常
 * </pre>
 * wait 和 notify 多线程协作 在wait 和 notify 的时候都需要获得object锁.
 *
 * @author shaoyijiong
 * @date 2018/7/16
 */
public class Thread4 {

  /**
   * 共享变量 用于多线程间加锁
   */
  private static final Object OBJECT = new Object();

  public static class T1 extends Thread {

    @Override
    public void run() {
      synchronized (OBJECT) {
        System.out.println(System.currentTimeMillis() + ": T1 start!");
        try {
          // 释放锁资源 让出cpu 进入等待状态 使当前线程阻塞，前提是 必须先获得锁，一般配合synchronized 关键字使用
          // 当线程执行wait()方法时候，会释放当前的锁，然后让出CPU，进入等待状态。
          System.out.println(System.currentTimeMillis() + ": T1 wait!");
          // sleep(1000) 不参与 CPU 竞争
          // wait(1000) 表示将锁释放1000毫秒，到时间后如果锁没有被其他线程占用，则再次得到锁，然后wait方法结束，执行后面的代码，如果锁被其他线程占用，则等待其他线程释放锁。
          // 超时时间的wait方法一旦过了超时时间，并不需要其他线程执行notify也能自动解除阻塞，但是如果没设置超时时间的wait方法必须等待其他线程执行notify。
          OBJECT.wait();
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() + ": T1 end!");
      }
    }
  }

  public static class T2 extends Thread {

    @Override
    public void run() {
      synchronized (OBJECT) {
        System.out.println(System.currentTimeMillis() + ": T2 start! notify one thread");
        // 唤醒一个正在等待状态的线程而不会立即释放锁，锁的释放要看代码块的具体执行情况。 本例中当前线程还持有锁 要打出T2 end 会优先于T1 end
        // 所以在编程中，尽量在使用了notify()/notifyAll() 后立即退出临界区，以唤醒其他线程
        // notify() 只会唤醒等待池中随机一个线程 notifyAll() 会唤醒所有等待池中的线程 去竞争锁
        OBJECT.notify();
        System.out.println(System.currentTimeMillis() + ": T2 end!");
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static class T3 extends Thread {

    // 线程在没有拿到监视器锁的情况下调用 监视对象的wait
    // 会抛出IllegalMonitorStateException 监视器状态异常
    @Override
    public void run() {
      try {
        OBJECT.wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) throws InterruptedException {
    Thread t1 = new T1();
    Thread t2 = new T2();
    //Thread t3 = new T3();
    //t3.start();
    t1.start();
    // 增加一个睡眠时间 防止t2在t1前获取锁
    Thread.sleep(2000);
    t2.start();
  }
}
