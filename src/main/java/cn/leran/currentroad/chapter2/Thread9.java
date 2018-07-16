package cn.leran.currentroad.chapter2;

/**
 * synchronized线程安全
 *
 * @author shaoyijiong
 * @date 2018/7/16
 */
public class Thread9 implements Runnable {
    static Thread9 account = new Thread9();
    static volatile int i = 0;

    //public static void add(){
    public synchronized static void add() {
        i++;
    }

    @Override
    public void run() {
        for (int j = 0; j < 100000; j++) {
            add();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(account);
        Thread t2 = new Thread(account);
        t1.start();
        t2.start();
        //当前线程等待t1 t2 结束 结果小于 200000因为线程直接覆盖了
        t1.join();
        t2.join();
        System.out.println(i);
    }
}