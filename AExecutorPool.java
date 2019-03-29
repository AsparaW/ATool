package pers.asparaw.fakeneteasecloudmusic.test;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import pers.asparaw.fakeneteasecloudmusic.test.impl.LongCallBack;


/**
 * Created by asparaw on 2019/3/26.
 */
public class AExecutorPool {

    private ExecutorService executorService;
    private Handler mHandler;

    /***
     * create instance if use
     */
    private static class instanceHolder{
        private static final AExecutorPool instance =new AExecutorPool();
    }
    private AExecutorPool(){
        int cpuCount=Runtime.getRuntime().availableProcessors();
        int threadCount=cpuCount*2+1;//best performance
        executorService= Executors.newFixedThreadPool(threadCount);
        mHandler=new Handler(Looper.getMainLooper());
    }
    public static AExecutorPool getInstance(){
        return instanceHolder.instance;
    }




    public <T> Future<?> submit(@NonNull Runnable task,T result){
        return executorService.submit(task,result);
    }
    public <T> Future<?> submit(@NonNull Callable<T> task){
        return executorService.submit(task);
    }


    public void submit(@NonNull Runnable task){
        executorService.submit(task);
    }

    public void execute(@NonNull Runnable command){
        executorService.execute(command);
    }

    /***
     * executePerformance method would return
     * the millis formatted time consumed
     * CAUTION:First SINGLETON ONLY(BLOCK METHOD)
     * @param command is the passed in runnable
     */
    public long executePerformance(@NonNull final Runnable command){

        Callable callable=new Callable() {
            @Override
            public Object call() throws Exception {
                long timeMillis=System.currentTimeMillis();
                command.run();
                timeMillis=System.currentTimeMillis()-timeMillis;
                return timeMillis;
            }
        };
        Future future=submit(callable);
        try {
            return (long) future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Long.MAX_VALUE;
    }
    public void executePerformance(@NonNull final Runnable command,final LongCallBack threadCallBack){

        Callable callable=new Callable() {
            @Override
            public Object call() throws Exception {
                long timeMillis=System.currentTimeMillis();
                command.run();
                timeMillis=System.currentTimeMillis()-timeMillis;
                threadCallBack.onSuccess(timeMillis);
                return timeMillis;
            }
        };
        submit(callable);
    }


    public void post(@NonNull Runnable r){
        mHandler.post(r);
    }

    public void postDelay(@NonNull Runnable r,long delayMillis){
        mHandler.postDelayed(r,delayMillis);
    }


}



