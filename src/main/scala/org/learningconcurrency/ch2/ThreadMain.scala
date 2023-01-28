package org.learningconcurrency.ch2

object ThreadMain extends App{
    val t:Thread = Thread.currentThread
    val name = t.getName
    println(s"Hello, I'm the thread $name")
}