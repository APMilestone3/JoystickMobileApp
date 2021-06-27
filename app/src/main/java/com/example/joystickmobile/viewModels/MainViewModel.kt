package com.example.joystickmobile.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.joystickmobile.models.FGModel


class MainViewModel : ViewModel() {
    private var model : FGModel = FGModel()
    val ipAddress = MutableLiveData<String>()
    val portNumber = MutableLiveData<String>()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onChangeRudderProgress(progress: Int) {
        val realProgress = progress.toFloat()/100
        model.setRudder(realProgress)
        this.model.send()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onChangeThrottleProgress(progress: Int) {
        val realProgress = progress.toFloat()/100
        model.setThrottle(realProgress)
        this.model.send()
    }

    fun onChangeConnectClick() {
        model.setIp(ipAddress)
        model.setPort(portNumber)
        model.connect()
    }

    fun onChangeDisconnectClick() {
        model.close()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onAileronChange(aileron: Float) {
        model.setAileron(aileron)
        this.model.send()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun onElevatorChange(elevator: Float) {
        model.setElevator(elevator)
        this.model.send()
    }


}