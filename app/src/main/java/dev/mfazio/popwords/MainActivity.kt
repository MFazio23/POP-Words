package dev.mfazio.popwords

import android.os.Bundle
import android.text.SpannableString
import android.text.util.Linkify
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dev.mfazio.popwords.db.HFWDatabase
import dev.mfazio.popwords.db.HFWRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private lateinit var hfwRepository: HFWRepository
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val dao = HFWDatabase.getInstance(this.application).hfwDao()
        this.hfwRepository = HFWRepository.getInstance(dao)

        this.navController = this.findNavController(R.id.containerFragment)

        findViewById<BottomNavigationView>(R.id.bottom_nav)
            .setupWithNavController(this.navController)

        AppCenter.start(
            application, "5a948da2-7f85-4c92-9965-811805b8768c",
            Analytics::class.java, Crashes::class.java
        )
        AppCenter.start(
            application, "5a948da2-7f85-4c92-9965-811805b8768c",
            Analytics::class.java, Crashes::class.java
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options, menu)

        return true
    }

    fun showClearDataDialog(item: MenuItem) {
        AlertDialog.Builder(this)
            .setMessage(R.string.clear_data_dialog_message)
            .setTitle(R.string.clear_data_dialog_title)
            .setIcon(R.drawable.ic_delete_forever_black_24dp)
            .setPositiveButton(R.string.yes) { dialog, id ->
                launch {
                    hfwRepository.clearAttempts()
                    Toast.makeText(
                        this@MainActivity,
                        "Attempt data has been cleared!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton(R.string.no) { dialog, id ->
                dialog?.cancel()
            }
            .create()
            .show()
    }

    fun showAboutTheApp(item: MenuItem) {
        val message = SpannableString(applicationContext.getText(R.string.about_the_app_message))
        Linkify.addLinks(message, Linkify.WEB_URLS)
        AlertDialog.Builder(this)
            .setMessage(message)
            .setTitle(R.string.about_the_app)
            .setIcon(R.drawable.ic_help_black_24dp)
            .setPositiveButton(R.string.ok) { dialog, id ->
            }
            .create()
            .show()
    }

}
