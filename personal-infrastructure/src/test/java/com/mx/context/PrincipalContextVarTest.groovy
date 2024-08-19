package com.mx.context

import com.mx.context.support.DefaultPrincipal
import com.mx.context.support.PrincipalContextVar
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Specification

import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
/**
 *
 * @author FizzPu
 * @since 2024/5/16 下午5:58
 */
class PrincipalContextVarTest extends Specification {
    Logger logger = LoggerFactory.getLogger(PrincipalContextVarTest)

    def "测试在异步线程中上下文是否可以正常传递"() {
        given: "新建上下文"
        PrincipalContextVar principalContextVar = new PrincipalContextVar()
        principalContextVar.set(new DefaultPrincipal("Fizz"))

        when: "测试在异步线程中是否可以获取该上下文"
        FutureTask<DefaultPrincipal> futureTask = new FutureTask<>(new Callable<DefaultPrincipal>() {
            @Override
            DefaultPrincipal call() throws Exception {
                logger.info("value = {}", principalContextVar.get())
                return principalContextVar.get()
            }
        })

        def thread = new Thread(futureTask)
        thread.start()

        then:
        DefaultPrincipal defaultPrincipal = futureTask.get()
        defaultPrincipal.displayName == "Fizz"
    }

    def "测试在线程池中上下文是否可以正常传递"() {
        given: "新建上下文"
        PrincipalContextVar principalContextVar = new PrincipalContextVar()
        principalContextVar.set(new DefaultPrincipal("Fizz"))

        when: "测试在异步线程中是否可以获取该上下文"
        FutureTask<DefaultPrincipal> futureTask1 = new FutureTask<>(new Callable<DefaultPrincipal>() {
            @Override
            DefaultPrincipal call() throws Exception {
                logger.info("value = {}", principalContextVar.get())
                principalContextVar.set(new DefaultPrincipal("Value1"))
                return principalContextVar.get()
            }
        })

        FutureTask<DefaultPrincipal> futureTask2 = new FutureTask<>(new Callable<DefaultPrincipal>() {
            @Override
            DefaultPrincipal call() throws Exception {
                logger.info("value = {}", principalContextVar.get())
                return principalContextVar.get()
            }
        })

        Executor executor = Executors.newSingleThreadExecutor()
        and: "第一个线程修改上下文"
        executor.execute(futureTask1)

        and: "第二个线程获取上下文"
            executor.execute(futureTask1)

        executor.execute(futureTask2)

        then:
        DefaultPrincipal defaultPrincipal = futureTask1.get()
        defaultPrincipal.displayName == "Value1"

        // Fizz 不会复制
        DefaultPrincipal defaultPrincipal2 = futureTask2.get()
        defaultPrincipal2.displayName == "Value1"

        principalContextVar.get().displayName == "Fizz"
    }

    def "测试在异步线程中，执行代码的正确姿势"() {
        given: "新建上下文"
        PrincipalContextVar principalContextVar = new PrincipalContextVar()
        principalContextVar.set(new DefaultPrincipal("Fizz"))

        when: "测试在异步线程中是否可以获取该上下文"
        FutureTask<DefaultPrincipal> futureTask1 = new FutureTask<>(new Callable<DefaultPrincipal>() {
            @Override
            DefaultPrincipal call() throws Exception {
                logger.info("value = {}", principalContextVar.get())
                principalContextVar.set(new DefaultPrincipal("Value1"))
                return principalContextVar.get()
            }
        })
        InheritContextVarRunnable inheritContextVarRunnable1 = new InheritContextVarRunnable().withContext(principalContextVar).wrap(futureTask1)

        FutureTask<DefaultPrincipal> futureTask2 = new FutureTask<>(new Callable<DefaultPrincipal>() {
            @Override
            DefaultPrincipal call() throws Exception {
                logger.info("value = {}", principalContextVar.get())
                return principalContextVar.get()
            }
        })
        InheritContextVarRunnable inheritContextVarRunnable2 = new InheritContextVarRunnable().withContext(principalContextVar).wrap(futureTask2)

        Executor executor = Executors.newSingleThreadExecutor()
        and: "第一个线程修改上下文, 执行完毕后回复上下文"
        executor.execute(inheritContextVarRunnable1)

        and: "第二个线程获取上下文"
        executor.execute(inheritContextVarRunnable2)

        executor.execute(futureTask2)

        then:
        DefaultPrincipal defaultPrincipal = futureTask1.get()
        defaultPrincipal.displayName == "Value1"

        // Fizz 不会复制
        DefaultPrincipal defaultPrincipal2 = futureTask2.get()
        defaultPrincipal2.displayName == "Fizz"

        principalContextVar.get().displayName == "Fizz"
    }
}
