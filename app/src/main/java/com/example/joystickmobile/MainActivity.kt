package com.example.joystickmobile

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.joystickmobile.databinding.ActivityMainBinding
import com.example.joystickmobile.viewModels.MainViewModel
import com.example.joystickmobile.views.JoystickView
import com.example.joystickmobile.views.OnJoystickChange
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog


class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var joystickView: JoystickView

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        dataBinding.viewModel = mainViewModel

        joystickView = findViewById(R.id.joystick_control)


        joystickView.onChange = OnJoystickChange { aileron, elevator ->
            mainViewModel.onAileronChange(aileron)
            mainViewModel.onElevatorChange(elevator)
        }


        val rudderSeekBar = findViewById<SeekBar>(R.id.rudderSeekBar)
        rudderSeekBar?.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                mainViewModel.onChangeRudderProgress(progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })

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
        val ipEditText = findViewById<EditText>(R.id.ip)
        val portEditText = findViewById<EditText>(R.id.port)
        connectButton.setOnClickListener {

            val connectionDialog = AlertDialog.Builder(this)

                // set message of alert dialog

                // if the dialog is cancelable
                .setCancelable(false)

                // negative button text and action
                .setNegativeButton("Close", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })

            // create dialog box
            val missingDataAlert = connectionDialog.create()
            // set title for alert dialog box
            missingDataAlert.setTitle("please enter port and ip")

            val outOfBoundPortNumberDialog = AlertDialog.Builder(this)

                // if the dialog is cancelable
                .setCancelable(false)

                // negative button text and action
                .setNegativeButton("Close", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })

            // create dialog box
            val portOutOfBoundAlert = outOfBoundPortNumberDialog.create()
            // set title for alert dialog box
            portOutOfBoundAlert.setTitle("Invalid port number")


            val onSuccessDialog = AlertDialog.Builder(this)

                // if the dialog is cancelable
                .setCancelable(false)

                // negative button text and action
                .setNegativeButton("Close", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                })

            // create dialog box
            val onSuccessAlert = onSuccessDialog.create()
            // set title for alert dialog box
            onSuccessAlert.setTitle("Connected")


            if (ipEditText.text.toString() == "" || portEditText.text.toString() == "") {
                missingDataAlert.show()
            } else if (portEditText.text.toString().toInt() < 0 ||
                portEditText.text.toString().toInt() > 65535
            ) {
                portOutOfBoundAlert.show()
            } else {
                isConnected = true
                mainViewModel.onChangeConnectClick()
                onSuccessAlert.show()
            }
        }

        val disconnectButton = findViewById<Button>(R.id.disconnect_button)

        disconnectButton.setOnClickListener {

            val clientNotConnectedDialog = AlertDialog.Builder(this)

                // set message of alert dialog

                // if the dialog is cancelable
                .setCancelable(false)

                // negative button text and action
                .setNegativeButton("Close", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

            // create dialog box
            val notConnectedAlert = clientNotConnectedDialog.create()
            // set title for alert dialog box
            notConnectedAlert.setTitle("Error! client is not connected")


            val onDisconnectedDialog = AlertDialog.Builder(this)

                // set message of alert dialog

                // if the dialog is cancelable
                .setCancelable(false)

                // negative button text and action
                .setNegativeButton("Close", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

            // create dialog box
            val onDisconnectedAlert = onDisconnectedDialog.create()
            // set title for alert dialog box
            onDisconnectedAlert.setTitle("disconnected")


            if (!isConnected) {
                notConnectedAlert.show()
            }
            else {
                onDisconnectedAlert.show()
                mainViewModel.onChangeDisconnectClick()
                isConnected = false
            }

        }

    }

}

