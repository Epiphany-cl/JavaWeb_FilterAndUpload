package test;

public class ThreadLocalExample {
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        // 在主线程中设置ThreadLocal的值
        threadLocal.set(42);

        // 创建两个新线程并分别获取ThreadLocal的值
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1: " + threadLocal.get()); // 输出Thread 1: null
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2: " + threadLocal.get()); // 输出Thread 2: null
        });

        System.out.println("Thread main: " + threadLocal.get()); // 输出Thread main: 42


        thread1.start();
        thread2.start();
    }
}
