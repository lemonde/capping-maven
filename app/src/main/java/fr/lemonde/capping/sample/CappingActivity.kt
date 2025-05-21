package fr.lemonde.capping.sample

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import fr.lemonde.capping.Capping
import fr.lemonde.capping.internal.CappingEvent
import fr.lemonde.capping.internal.CappingLock
import fr.lemonde.capping.internal.CappingLockStatusListener
import fr.lemonde.capping.internal.CappingMode
import fr.lemonde.capping.internal.CappingOperationFailedListener

class CappingActivity : AppCompatActivity() {

    lateinit var capping: Capping
    lateinit var lockStatusListener: CappingLockStatusListener
    lateinit var operationFailedListener: CappingOperationFailedListener
    lateinit var tvMode: TextView
    lateinit var tvState: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capping)

        tvMode = findViewById<Button>(R.id.tv_mode)
        tvState = findViewById<Button>(R.id.tv_state)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener { Capping.getInstance(this).continueReading() }
        button.isVisible = true

        val mode = CappingMode.DEVICE

        tvMode.text = "Mode: ${mode.nameMode}"

        capping = Capping.getInstance(this)
        capping.setMode(mode)
        capping.setUserId("userId")
        capping.setTolerance(1)
        capping.setAutoUnblock(true)
        capping.setActive(true)

        lockStatusListener = object : CappingLockStatusListener {
            override fun onLockChange(cappingLock: CappingLock) {
                when (cappingLock) {
                    is CappingLock.Blocked -> {
                        displayLock()
                        button.isVisible = true
                    }

                    CappingLock.Unblocked -> {
                        displayUnlock()
                        button.isVisible = false
                    }
                }
            }
        }
        capping.attachListener(lockStatusListener)

        operationFailedListener = object : CappingOperationFailedListener {
            override fun cappingOperationFailed(localizedDescription: String) {
                Toast.makeText(this@CappingActivity, localizedDescription, Toast.LENGTH_LONG).show()
            }
        }
        capping.attachOperationFailedListener(operationFailedListener)

        val buttonTagConversion = findViewById<Button>(R.id.button_tag_conversion)
        buttonTagConversion.setOnClickListener { this.capping.trackEvent(CappingEvent.CONVERSION) }
    }

    override fun onResume() {
        super.onResume()
        capping.startSession()
    }

    override fun onPause() {
        super.onPause()
        capping.stopSession()
    }

    override fun onDestroy() {
        super.onDestroy()
        capping.detachListener(lockStatusListener)
        capping.detachOperationFailedListener(operationFailedListener)
    }

    private fun displayLock() {
        Toast.makeText(this, "Capping Locked", Toast.LENGTH_LONG).show()
        tvState.text = "Locked"
        this.capping.trackEvent(CappingEvent.POPIN_DISPLAYED)
    }

    private fun displayUnlock() {
        Toast.makeText(this, "Capping unLocked", Toast.LENGTH_LONG).show()
        tvState.text = "Not locked"
    }
}
