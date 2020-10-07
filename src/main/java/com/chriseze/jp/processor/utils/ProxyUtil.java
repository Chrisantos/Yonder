package com.chriseze.jp.processor.utils;

import java.util.concurrent.Future;
import java.util.function.Supplier;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

@Stateless
public class ProxyUtil {

    /**
     *
     * @param supplier
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public <T> T executeWithNewTransaction(Supplier<T> supplier){
        return supplier.get();
    }

    /**
     *
     * @param runnableTask
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void executeWithNewTransaction(RunnableTask runnableTask){
        runnableTask.run();
    }

    /**
     *
     * @param <T>
     * @param supplier
     * @return
     */
    @Asynchronous
    public <T> Future<T> executeAsync(Supplier<T> supplier){
        return new AsyncResult<T>(supplier.get());
    }

    /**
     *
     * @param runnableTask
     */
    @Asynchronous
    public void executeAsync(RunnableTask runnableTask){
        runnableTask.run();
    }

    /**
     * This functional interface was declared and used in the methods above in order to create a {@link FunctionalInterface} equivalent
     * of {@link Runnable} to avoid any confusion since {@link Runnable} often connotes a {@link Thread} context which is not really the
     * case for the scenarios above
     */
    @FunctionalInterface
    public interface RunnableTask{
        void run();
    }

}
