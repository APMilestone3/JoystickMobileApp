package com.example.joystickmobile

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.joystickmobile.databinding.ActivityMainBinding
import com.example.joystickmobile.viewModels.MainViewModel
import com.example.joystickmobile.views.JoystickView
import com.example.joystickmobile.views.OnJoystickChange
import java.math.BigInteger
import java.net.InetAddress


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var joystickView: JoystickView

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        dataBinding.viewModel = mainViewModel

        joystickView = findViewById(R.id.joystick_control)


        // set the joystick functional interface
        joystickView.onChange = OnJoystickChange { aileron, elevator ->
            mainViewModel.onAileronChange(aileron)
            mainViewModel.onElevatorChange(elevator)
        }


        val rudderSeekBar = findViewById<SeekBar>(R.id.rudderSeekBar)

        // rudderSeekBar listener
        rudderSeekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                mainViewModel.onChangeRudderProgress(progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {

            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })

        // throttleSeekBar listener
        val throttleSeekBar = findViewById<SeekBar>(R.id.throttleSeekBar)
        throttleSeekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                mainViewModel.onChangeThrottleProgress(progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })


        var isConnected = false
        val connectButton = findViewById<Button>(R.id.connect_button)
        val disconnectButton = findViewById<Button>(R.id.disconnect_button)

        // connectButton listener
        connectButton.setOnClickListener {
            val ipEditText = findViewById<EditText>(R.id.ip)
            val portEditText = findViewById<EditText>(R.id.port)

            // missing ip or port dialog
            val missingDataDialog = AlertDialog.Builder(this)

                .setCancelable(false)

                .setNegativeButton("Close", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })

            // create dialog box
            val missingDataAlert = missingDataDialog.create()

            // set title for alert dialog box
            missingDataAlert.setTitle("please enter port and ip")

            // invalid port dialog
            val outOfBoundPortNumberDialog = AlertDialog.Builder(this)

                .setCancelable(false)

                .setNegativeButton("Close", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })


            val portOutOfBoundAlert = outOfBoundPortNumberDialog.create()

            portOutOfBoundAlert.setTitle("Invalid port number")


            val portNumber = portEditText.text.toString().toIntOrNull()

            // if ip or port is missing, pop out suitable massage
            if (ipEditText.text.toString() == "" || portEditText.text.toString() == "") {
                missingDataAlert.show()
            }

            // in case invalid port number inserted
            else if (portNumber == null || portNumber < 0 || portNumber > 65535) {
                portOutOfBoundAlert.show()

            }

            else {
                mainViewModel.onChangeConnectClick()
                connectButton.text = "Connected"
                connectButton.isEnabled = false
                disconnectButton.text = "Disconnect"
                disconnectButton.isEnabled = true
                isConnected = true
            }

        }


        // disconnectButton listener
        disconnectButton.setOnClickListener {

            mainViewModel.onChangeDisconnectClick()
            connectButton.text = "Connect"
            connectButton.isEnabled = true
            disconnectButton.text = "Disconnected"
            disconnectButton.isEnabled = false
            isConnected = false

        }

    }



}

