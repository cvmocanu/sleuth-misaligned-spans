# Misaligned spans: minimal reproduction test case

## Purpose
This project makes it easy to reproduce a Sleuth and/or Zipkin/brave tracing issue.

## How is this test configured
The project uses ``spring-cloud-stream`` to start a Kafka consumer.
Upon receiving a new message from Kafka, we use Spring ``WebClient`` to make an HTTP call to a 3rd party API.

This will create two spans:
* one for the Kafka message
* a second one for the HTTP call

## How to reproduce the problem
1. make sure JDK 11 is available in the PATH
2. run ``./gradlew test`` (on Mac or Linux) or ``gradlew.bat test`` (on Windows)
3. the test will fail with the following exception:
```text
java.lang.AssertionError: Misalignment: scoped span RealSpan(850ce4456eb5a532/850ce4456eb5a532) !=  current span LazySpan(850ce4456eb5a532/e4703a6d3b57311f)
	at brave.propagation.ThreadLocalSpan.remove(ThreadLocalSpan.java:154)
	at org.springframework.cloud.sleuth.instrument.messaging.TracingChannelInterceptor.finishSpan(TracingChannelInterceptor.java:381)
	at org.springframework.cloud.sleuth.instrument.messaging.TracingChannelInterceptor.afterSendCompletion(TracingChannelInterceptor.java:246)
	at org.springframework.integration.channel.AbstractMessageChannel$ChannelInterceptorList.afterSendCompletion(AbstractMessageChannel.java:642)
	at org.springframework.integration.channel.AbstractMessageChannel.send(AbstractMessageChannel.java:469)
	at org.springframework.integration.channel.AbstractMessageChannel.send(AbstractMessageChannel.java:403)
	at net.mocanu.tracing_issue.ApplicationTests.test(ApplicationTests.java:50)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:50)
	at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
	at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:47)
	at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
	at org.springframework.test.context.junit4.statements.RunBeforeTestExecutionCallbacks.evaluate(RunBeforeTestExecutionCallbacks.java:74)
	at org.springframework.test.context.junit4.statements.RunAfterTestExecutionCallbacks.evaluate(RunAfterTestExecutionCallbacks.java:84)
	at org.springframework.test.context.junit4.statements.RunBeforeTestMethodCallbacks.evaluate(RunBeforeTestMethodCallbacks.java:75)
	at org.springframework.test.context.junit4.statements.RunAfterTestMethodCallbacks.evaluate(RunAfterTestMethodCallbacks.java:86)
	at org.springframework.test.context.junit4.statements.SpringRepeat.evaluate(SpringRepeat.java:84)
	at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:325)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:251)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.runChild(SpringJUnit4ClassRunner.java:97)
	at org.junit.runners.ParentRunner$3.run(ParentRunner.java:290)
	at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:71)
	at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:288)
	at org.junit.runners.ParentRunner.access$000(ParentRunner.java:58)
	at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:268)
	at org.junit.internal.runners.statements.RunBefores.evaluate(RunBefores.java:26)
	at org.springframework.test.context.junit4.statements.RunBeforeTestClassCallbacks.evaluate(RunBeforeTestClassCallbacks.java:61)
	at org.junit.internal.runners.statements.RunAfters.evaluate(RunAfters.java:27)
	at org.springframework.test.context.junit4.statements.RunAfterTestClassCallbacks.evaluate(RunAfterTestClassCallbacks.java:70)
	at org.junit.runners.ParentRunner.run(ParentRunner.java:363)
	at org.springframework.test.context.junit4.SpringJUnit4ClassRunner.run(SpringJUnit4ClassRunner.java:190)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.runTestClass(JUnitTestClassExecutor.java:110)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:58)
	at org.gradle.api.internal.tasks.testing.junit.JUnitTestClassExecutor.execute(JUnitTestClassExecutor.java:38)
	at org.gradle.api.internal.tasks.testing.junit.AbstractJUnitTestClassProcessor.processTestClass(AbstractJUnitTestClassProcessor.java:62)
	at org.gradle.api.internal.tasks.testing.SuiteTestClassProcessor.processTestClass(SuiteTestClassProcessor.java:51)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.dispatch.ContextClassLoaderDispatch.dispatch(ContextClassLoaderDispatch.java:33)
	at org.gradle.internal.dispatch.ProxyDispatchAdapter$DispatchingInvocationHandler.invoke(ProxyDispatchAdapter.java:94)
	at com.sun.proxy.$Proxy2.processTestClass(Unknown Source)
	at org.gradle.api.internal.tasks.testing.worker.TestWorker.processTestClass(TestWorker.java:118)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:566)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:36)
	at org.gradle.internal.dispatch.ReflectionDispatch.dispatch(ReflectionDispatch.java:24)
	at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:182)
	at org.gradle.internal.remote.internal.hub.MessageHubBackedObjectConnection$DispatchWrapper.dispatch(MessageHubBackedObjectConnection.java:164)
	at org.gradle.internal.remote.internal.hub.MessageHub$Handler.run(MessageHub.java:412)
	at org.gradle.internal.concurrent.ExecutorPolicy$CatchAndRecordFailures.onExecute(ExecutorPolicy.java:64)
	at org.gradle.internal.concurrent.ManagedExecutorImpl$1.run(ManagedExecutorImpl.java:48)
	at java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1128)
	at java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:628)
	at org.gradle.internal.concurrent.ThreadFactoryImpl$ManagedThreadRunnable.run(ThreadFactoryImpl.java:56)
	at java.base/java.lang.Thread.run(Thread.java:834)
```

## Analysis of the problem
* The problem occurs while trying to finish the root span (the one for Kafka).
* It looks like the second span (that one for the WebClient) is either not finished, or finished improperly
(in a wrong order), leaving things behind in the ``ThreadLocal``.
* The project uses the latest versions of Sleuth and Brave, and this doesn't fix the problem.
* I've added some logging breakpoints in IntelliJ in the class ``ThreadLocalCurrentTraceContext`` to debug this issue.
  Note that to see the output of these logging breakpoints, you need to run a JUnit run configuration in IntelliJ,
  and not delegate the running of the test to Gradle.
  The breakpoints are setup as follows
  * one breakpoint on line #79 (``local.set(currentSpan);``), with the following configuration
    * uncheck ``Suspend``
    * check ``Evaluate and log`` with the expression: ``">>> [" + Thread.currentThread().name + "] set(" +currentSpan + ")"``
  * a second breakpoint on line #82 (``local.set(previous);``), with the following configuration
    * uncheck ``Suspend``
    * check ``Evaluate and log`` with the expression: ``">>> [" + Thread.currentThread().name + "] cls(" + local.get() + "); set(" + previous + ")"``
  
  This produces the following output:
  ```text 
  line #01  >>> [main] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #02  >>> [main] set(b5ed91742919b8ab/6a6a3f13f89b5b4c)
  line #03  >>> [main] cls(b5ed91742919b8ab/6a6a3f13f89b5b4c); set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #04  >>> [main] set(b5ed91742919b8ab/6a6a3f13f89b5b4c)
  line #05  >>> [main] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #06  >>> [main] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(b5ed91742919b8ab/6a6a3f13f89b5b4c)
  line #07  >>> [main] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #08  >>> [main] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(b5ed91742919b8ab/6a6a3f13f89b5b4c)
  line #09  >>> [main] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #10  >>> [main] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(b5ed91742919b8ab/6a6a3f13f89b5b4c)
  line #11  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/6a6a3f13f89b5b4c)
  line #12  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/6a6a3f13f89b5b4c); set(null)
  line #13  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/6a6a3f13f89b5b4c)
  line #14  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/6a6a3f13f89b5b4c); set(null)
  line #15  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #16  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(null)
  line #17  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #18  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(null)
  line #19  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #20  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(null)
  line #21  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #22  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(null)
  line #23  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #24  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(null)
  line #25  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #26  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(null)
  line #27  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #28  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/6a6a3f13f89b5b4c)
  line #29  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/6a6a3f13f89b5b4c); set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #30  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #31  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(null)
  line #32  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #33  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(null)
  line #34  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #35  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(null)
  line #36  >>> [reactor-http-epoll-2] set(b5ed91742919b8ab/b5ed91742919b8ab)
  line #37  >>> [reactor-http-epoll-2] cls(b5ed91742919b8ab/b5ed91742919b8ab); set(null)
  line #38  >>> [main] cls(b5ed91742919b8ab/6a6a3f13f89b5b4c); set(null)
  ```
  ``b5ed91742919b8ab/b5ed91742919b8ab`` would be the span for Kafka (root span),
  and ``b5ed91742919b8ab/6a6a3f13f89b5b4c`` would be the span for WebClient.
  
  Notice a few suspect things:
  * a new scope for the same span(s) is started and stopped many times, from different threads
  * look at lines #04-#06. This starts a scope for the WebClient, then a scope for Kafka, and then the scope for Kafka
    finishes, leaving the WebClient as the current scope. The order in which the scopes are opened on these lines
    look incorrect to me, maybe even the source of the problem.
