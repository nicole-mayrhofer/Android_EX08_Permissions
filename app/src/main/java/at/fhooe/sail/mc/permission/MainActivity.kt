package at.fhooe.sail.mc.permission

import android.Manifest
import android.content.ContentValues.TAG
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import at.fhooe.sail.mc.permission.databinding.ActivityMainBinding

const val TAG: String = "Permission-Test"

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    var launcher: ActivityResultLauncher<String> = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { granted: Boolean ->
        if (granted) {
            setUpCall()
        } else {

            // gracefully degrade UI
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.activityMainButtonCall.setOnClickListener {
            Log.i(TAG, "CallHelp pressed")
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                setUpCall()
            } else {
                Log.e(TAG, "${Manifest.permission.CALL_PHONE} not granted")
                if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
                    Log.e(TAG, "show ${Manifest.permission.CALL_PHONE} reason phrase")

                    // BUILDER FÜR DAS DIALOG-FENSTER
                    AlertDialog.Builder(this)
                        .setTitle("ReasonPhrase")
                        .setMessage("geht nicht ohne!")
                        .setNeutralButton(android.R.string.ok) { _, _ ->

                        }
                        .create()
                        .show()

                    // LANGE VARIANTE - macht das Selbe wie oben
                    val bob: AlertDialog.Builder = AlertDialog.Builder(this)
                    bob.setTitle("ReasonPhrase")
                    bob.setMessage("geht nicht ohne!")
                    bob.setNeutralButton(android.R.string.ok, object : DialogInterface.OnClickListener {
                        override fun onClick(p0: DialogInterface?, p1: Int) {
                            // TO DO: NOTHING
                        }
                    })
                    // kürzere Variante:
                    bob.setNeutralButton(android.R.string.ok, {p0, p1 ->
                        // to do: nothing
                    })
                    // inline variante von lambda --- wenn nur ein übergabeparameter kann ich mir die runden klammern sparen
                    bob.setNeutralButton(android.R.string.ok) {p0, p1 ->
                        // to do: nothing
                    }
                    val dialog: AlertDialog = bob.create()
                    dialog.show()
                }
                launcher.launch(Manifest.permission.CALL_PHONE)
            }
        }
    }

    fun setUpCall() {
        val intent: Intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:123456"))
        startActivity(intent)
    }
}