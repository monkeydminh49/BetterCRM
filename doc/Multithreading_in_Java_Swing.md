# Multithreading in Java Swing

For multithreading purpose, Swing library provides a class called _SwingWorker_. 

_SwingWorker_ allows users to schedule the execution of background tasks on Worker Thread, where all time-consuming background tasks are executed.
It is an abstract class, so we need to extend it to use it.

[//]: # (The class has 3 generic types: V, P, and R. V is the type of the value returned by doInBackground&#40;&#41; method. P is the type of the value passed to the process&#40;&#41; method. R is the type of the value returned by the get&#40;&#41; method.)

Important Methods of SwingWorker class:

| Method           | Action Performed                                                                                                                              |
|------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|
| cancel()         | Cancel the execution of this task.                                                                                                            |
| doInBackGround() | Contains logic (necessary to implement.                                                                                                       |
| done()           | - Is called when thread finished. <br/> - Can receive value from the doInBackground().<br/> - Updating GUI functions can be implemented here. |
| execute()        | 	Schedules this SwingWorker for execution on a worker thread.                                                                                 |
| getState()       | 	Returns the SwingWorker state-bound property.                                                                                                |
| isCancelled()    | Returns true if this task was canceled before it was completed normally.                                                                      |
| isDone()         | Returns true if this task is completed.                                                                                                       |
| get()            | Waits if necessary for the computation to complete, and then retrieves its result.                                                            |

