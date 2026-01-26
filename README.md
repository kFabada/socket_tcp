# Socket Tcp

Criação de Socket tcp no delay multithreading, com multi usuarios conectados e servidor com redirect de mensagem e comandos de mensagem personalizados.

Thread:
  - wait.
  - notify
  - notifyAll
  - synchronized

Object:
  Imutable Object

Variables:
  - Atomic
  - volatile

Handle PoolThreads:
  - Executor, a simple interface that supports launching new tasks.
  - Executores.
  - ExecutorService, a subinterface of Executor, which adds features that help manage the life cycle, both of the individual tasks and of the executor itself.
  - ScheduledExecutorService, a subinterface of ExecutorService, supports future and/or periodic execution of tasks.
  - ForkJoin

Lock:
   - Semaphore.
   - ReentrantLock.

Collection:
  - BlockLinkedQueu
  - etc

Run project
  - Start /src/ServerMain.class
  - Start one client /src/ClientMain.class
  - Start test load 300-client /src/test.sh in background
     
