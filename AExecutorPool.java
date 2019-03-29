package pers.asparaw.fakeneteasecloudmusic.test;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;



import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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




    public <T> void submit(@NonNull Runnable task,T result){
        executorService.submit(task,result);
    }
    public <T> void submit(@NonNull Callable<T> task){
        executorService.submit(task);
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
     * @param command is the passed in runnable
     */
    public long executePerformance(@NonNull Runnable command){
        long timeMillis=0L;
        return  timeMillis;
    }

    public void post(@NonNull Runnable r){
        mHandler.post(r);
    }

    public void postDelay(@NonNull Runnable r,long delayMillis){
        mHandler.postDelayed(r,delayMillis);
    }

}
