package kornacki.jan.lsoappver3.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import kornacki.jan.lsoappver3.R
import kornacki.jan.lsoappver3.viewModel.AdministrationViewModel
import kornacki.jan.lsoappver3.databinding.FragmentAdministrationBinding
import kornacki.jan.lsoappver3.model.objects.AltarBoy
import kornacki.jan.lsoappver3.model.objects.Event

class AdministrationFragment : Fragment(), AdministrationViewModel.FirebaseStatusCallback {
    private var _binding: FragmentAdministrationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: AdministrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAdministrationBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[AdministrationViewModel::class.java]

        bindFunctionalities()

        return binding.root
    }

    private fun bindFunctionalities() {
        binding.btnAddAltarBoy.setOnClickListener {
            addAltarBoy()
        }

        binding.btnAddEvent.setOnClickListener {
            addEvent()
        }

        binding.btnDeleteAltarBoy.setOnClickListener {
            deleteAltarBoy()
        }

        binding.btnDeleteEvent.setOnClickListener {
            deleteEvent()
        }
    }

    private fun getContainerizedView(view: View): LinearLayout {
        val container = LinearLayout(context)
        container.setPadding(30)
        container.orientation = LinearLayout.VERTICAL
        container.addView(view)

        return container
    }

    private fun addAltarBoy() {
        val editText = EditText(requireContext())

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_type_altar_boy_name))
            .setView(getContainerizedView(editText))
            .setPositiveButton(getString(R.string.alert_option_accept)) { _, _ ->
                if (editText.text.toString() == "") {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_altar_boy_wrong_name),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.createAltarBoy(editText.text.toString(), this)
                }
            }
            .setNegativeButton(getString(R.string.alert_option_decline), null)
            .show()
    }

    private fun getAltarBoysSpinnerAdapter(altarBoys: ArrayList<AltarBoy>):
            ArrayAdapter<AltarBoy> {

        if (altarBoys.isNotEmpty() && altarBoys[0].name != getString(R.string.pick_encourage)) {
            altarBoys.add(0, AltarBoy(getString(R.string.pick_encourage)))
        }
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.bigger_spinner_item,
            altarBoys
        )
        adapter.setDropDownViewResource(R.layout.bigger_spinner_dropdown_item)
        return adapter
    }

    private fun getEventsSpinnerAdapter(events: ArrayList<Event>):
            ArrayAdapter<Event> {
        if (events.isNotEmpty() && events[0].name != getString(R.string.pick_encourage)) {
            events.add(0, Event(getString(R.string.pick_encourage)))
        }
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.bigger_spinner_item,
            events
        )
        adapter.setDropDownViewResource(R.layout.bigger_spinner_dropdown_item)
        return adapter
    }

    private fun isBlankOption(id: Long): Boolean = id == 0L

    private fun deleteAltarBoy() {
        val spinner = Spinner(context)
        spinner.adapter = getAltarBoysSpinnerAdapter(viewModel.altarBoys)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_choose_a_boy))
            .setView(getContainerizedView(spinner))
            .setPositiveButton(getString(R.string.alert_option_accept)) { _, _ ->
                val selectedItemId = spinner.selectedItemId
                val altarBoy = spinner.selectedItem as AltarBoy

                if (isBlankOption(selectedItemId)) {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_pick_a_boy),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.deleteAltarBoy(altarBoy, this)
                }
            }
            .setNegativeButton(getString(R.string.alert_option_decline), null)
            .show()
    }

    private fun deleteEvent() {
        val spinner = Spinner(context)
        spinner.adapter = getEventsSpinnerAdapter(viewModel.events)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_choose_event))
            .setView(getContainerizedView(spinner))
            .setPositiveButton(getString(R.string.alert_option_accept)) { _, _ ->
                val selectedItemId = spinner.selectedItemId
                val event = spinner.selectedItem as Event

                if (isBlankOption(selectedItemId)) {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_pick_event),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.deleteEvent(event, this)
                }
            }
            .setNegativeButton(getString(R.string.alert_option_decline), null)
            .show()
    }

    private fun String.isNumeric(): Boolean {
        return this.matches(Regex("^[0-9]+$"))
    }

    private fun String.isNegativeNumber(): Boolean {
        return this.toIntOrNull()?.let { it < 0 } ?: false
    }

    private fun getAddEventAlertView(
        editTextName: EditText,
        editTextPoints: EditText,
    ): LinearLayout {
        val textNameEncourage = TextView(context)
        val textPointsEncourage = TextView(context)

        textNameEncourage.text = getString(R.string.alert_add_event_type_name)
        textPointsEncourage.text = getString(R.string.alert_add_event_type_points)

        val container = LinearLayout(context)
        container.setPadding(30)
        container.orientation = LinearLayout.VERTICAL
        container.addView(textNameEncourage)
        container.addView(editTextName)
        container.addView(textPointsEncourage)
        container.addView(editTextPoints)

        return container
    }

    private fun addEvent() {
        val editTextName = EditText(context)
        val editTextPoints = EditText(context)

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_specify_event_name))
            .setView(getAddEventAlertView(editTextName, editTextPoints))
            .setPositiveButton(getString(R.string.alert_option_accept)) { _, _ ->
                val name = editTextName.text.toString()
                val pointsString = editTextPoints.text.toString()

                if (name.isEmpty()) {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_event_wrong_name),
                        Toast.LENGTH_LONG
                    ).show()
                } else if (!pointsString.isNumeric() || pointsString.isNegativeNumber()) {
                    Toast.makeText(
                        context,
                        getString(R.string.toast_event_wrong_points),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    viewModel.createEvent(name, pointsString.toInt(), this)
                }
            }
            .setNegativeButton(getString(R.string.alert_option_decline), null)
            .show()
    }

    override fun onUpdateSuccess() {
        Toast.makeText(context, getString(R.string.toast_update_success), Toast.LENGTH_LONG).show()
    }

    override fun onUpdateFailure() {
        Toast.makeText(context, getString(R.string.toast_update_failure), Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}