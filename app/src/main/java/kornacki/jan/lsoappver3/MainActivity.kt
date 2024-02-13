package kornacki.jan.lsoappver3

import android.os.Bundle
import android.text.InputType
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import androidx.navigation.NavController
import kornacki.jan.lsoappver3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
// TODO
//        binding.fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun getContainerizedView(view: View): LinearLayout {
        val container = LinearLayout(this)
        container.setPadding(30)
        container.orientation = LinearLayout.VERTICAL
        container.addView(view)

        return container
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_administration -> {
                val passwordInput = EditText(this)
                passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.alert_password_needed))
                    .setView(getContainerizedView(passwordInput))
                    .setPositiveButton(getString(R.string.alert_option_accept)) { _, _ ->
                        val password = passwordInput.text.toString()
                        if (password == "") { // TODO: get pass from db
                            navController.navigate(R.id.AdministrationFragment)
                        } else {
                            Toast.makeText(
                                applicationContext,
                                getString(R.string.toast_wrong_password),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    .setNegativeButton(getString(R.string.alert_option_decline), null)
                    .show()
                true
            }
            R.id.action_leaderboard -> {
                navController.navigate(R.id.LeaderboardFragment)
                true
            }
            R.id.action_enrollment -> {
                navController.navigate(R.id.EnrollmentFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        if (navController.currentDestination?.id != R.id.EnrollmentFragment) {
            navController.navigate(R.id.EnrollmentFragment)
        }

        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (navController.currentDestination?.id != R.id.EnrollmentFragment) {
            navController.navigate(R.id.EnrollmentFragment)
        }
    }
}