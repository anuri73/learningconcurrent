package org.learningconcurrency.ch2
import scala.collection.mutable

object SynchronizedPool extends App{
  private val tasks = mutable.Queue[() => Unit]()

  object Worker extends Thread {
    setDaemon(true)
    def poll() = tasks.synchronized {
        while (tasks.isEmpty) tasks.wait()
        tasks.dequeue()
    }
    override def run() = while(true) {
        val task = poll()
        task()
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
