package org.learningconcurrency.ch2
import scala.collection.mutable
import scala.annotation.tailrec

object SynchronizedPool extends App{
  private val tasks = mutable.Queue[() => Unit]()

  object Worker extends Thread {
    setDaemon(true)
    var terminated = false
    def poll() = tasks.synchronized {
        while (tasks.isEmpty && !terminated) tasks.wait()

        if (!terminated) Some(tasks.dequeue) else None
    }

    @tailrec
    override def run() = poll() match {
        case Some(task) => task(); run()
        case None => 
    }

    def shutdown() = tasks.synchronized {
        terminated = true
        tasks.notify
    }
  }

  Worker.start

  def asyncrhonous(body: => Unit ) = tasks.synchronized {
    tasks.enqueue(() => body)
    tasks.notify()
  }

  asyncrhonous{ println("Hello") }

  asyncrhonous{ println("World!") }

  Thread.sleep(500)
}
