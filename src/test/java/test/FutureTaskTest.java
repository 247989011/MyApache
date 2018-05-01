package test;

import java.util.concurrent.*;

/**
 *
 * @author
 * @date
 */
public class FutureTaskTest {



    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        MyCallable call1 = new MyCallable(1000);
        MyCallable call2 = new MyCallable(2000);

        FutureTask<String> task1 = new FutureTask<String>(call1);
        FutureTask<String> task2 = new FutureTask<String>(call2);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.execute(task1);
        executorService.execute(task2);

        while(true){
            if(task1.isDone() && task2.isDone()){
                System.out.println("All be Done");
                executorService.shutdown();
                break;
            }
            if(!task1.isDone()){ // 任务1没有完成，会等待，直到任务完成
                System.out.println("task1 output="+task1.get());
            }
            System.out.println("Waiting for task2 to complete");
            String s = task2.get();//(100L, TimeUnit.MILLISECONDS);
            if(s !=null){
                System.out.println("task2 output="+s);
            }

        }

    }



}

 class MyCallable implements Callable{

    private long waitTime;

    public MyCallable(long waitTime){
        this.waitTime = waitTime;
    }
    public Object call() throws Exception {
        Thread.sleep(waitTime);
        return Thread.currentThread().getName();
    }
}
