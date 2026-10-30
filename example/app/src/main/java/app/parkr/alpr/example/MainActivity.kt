package app.parkr.alpr.example

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import app.parkr.alpr.AlprCameraView
import app.parkr.alpr.AlprCameraViewListener
import app.parkr.alpr.AlprError
import app.parkr.alpr.AlprPlate

class MainActivity : ComponentActivity() {
  private lateinit var camera: AlprCameraView
  private lateinit var readout: TextView

  private val requestCamera = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
    if (granted) camera.isActive = true else readout.text = "Camera permission is needed to scan plates."
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    readout = TextView(this).apply {
      setTextColor(Color.WHITE)
      setBackgroundColor(Color.argb(200, 17, 24, 39))
      setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
      setPadding(32, 24, 32, 24)
      text = "Point the camera at a plate"
    }

    camera = AlprCameraView(this).apply {
      listener = object : AlprCameraViewListener {
        override fun onPlates(view: AlprCameraView, plates: List<AlprPlate>, frameWidth: Int, frameHeight: Int) {
          if (plates.isNotEmpty()) readout.text = plates.joinToString("\n") { "${it.plate}  ${it.countryCode ?: ""}" }
        }

        override fun onError(view: AlprCameraView, error: AlprError) {
          readout.text = error.message
        }
      }
    }

    setContentView(FrameLayout(this).apply {
      addView(camera, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
      addView(readout, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM))
    })

    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) camera.isActive = true
    else requestCamera.launch(Manifest.permission.CAMERA)
  }
}
