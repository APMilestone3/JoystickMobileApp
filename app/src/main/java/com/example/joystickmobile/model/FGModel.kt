package com.example.joystickmobile.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.ForkJoinPool

class FGModel() {
    private var aileron: Float = 0.0f
    private var elevator: Float = 0.0f
    private var rudder: Float = 0.0f
    private var throttle: Float = 0.0f
    private lateinit var client: Socket
    private var output: PrintWriter? = null
    private lateinit var _ip: String
    private var _port: Int = 0
    @Volatile var isStopped : Boolean = true

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val fj: ForkJoinPool =
        ForkJoinPool(1, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true)



    init {
        println("Model created")
    }

    fun setIp(ip: MutableLiveData<String>) {
        this._ip = ip.value.toString()

    }

    fun setPort(port: MutableLiveData<String>) {
        this._port = port.value.toString().toInt()
    }

    fun setRudder(rudder: Float) {
        this.rudder = rudder
        println("the rudder progress is ${this.rudder}")
    }

    fun setThrottle(throttle: Float) {
        this.throttle = throttle
        println("the throttle progress is ${this.throttle}")
    }

    fun setAileron(aileron: Float) {
        this.aileron = aileron
        println("the aileron value is ${this.aileron}")
    }

    fun setElevator(elevator: Float) {
        this.elevator = elevator
        println("the elevator value is ${this.elevator}")
    }



    fun connect() {
        Thread {
            try {
                client = Socket(_ip, _port)
                output = PrintWriter(client.getOutputStream(), true)
                this.isStopped = false
                println("connected")
            } catch (e: Exception) {
                println(e)
            }
        }.start()

    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Throws(InterruptedException::class)
    fun send() {
        this.fj.execute {
            output?.print("set /controls/engines/current-engine/throttle $throttle\r\n")
            output?.flush()
            output?.print("set /controls/flight/aileron $aileron\r\n")
            output?.flush()
            output?.print("set /controls/flight/elevator $elevator\r\n")
            output?.flush()
            output?.print("set /controls/flight/rudder $rudder\r\n")
            output?.flush()
            Thread.sleep(100)
        }
    }


    fun close(client: Socket) {
        try {
            client.close();
            this.isStopped = true
            println("disconnected")
        } catch (e: Exception) {
            println("error while closing")
        }
    }
}