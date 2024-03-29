package kornacki.jan.lsoappver3.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import kornacki.jan.lsoappver3.R
import kornacki.jan.lsoappver3.databinding.FragmentEnrollmentBinding
import kornacki.jan.lsoappver3.model.objects.AltarBoy
import kornacki.jan.lsoappver3.model.objects.Event
import kornacki.jan.lsoappver3.model.services.FirebaseService
import kornacki.jan.lsoappver3.viewModel.EnrollmentViewModel
import java.time.LocalDateTime

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EnrollmentFragment : Fragment(), EnrollmentViewModel.FirebaseStatusCallback {
    private var _binding: FragmentEnrollmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var viewModel: EnrollmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentEnrollmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[EnrollmentViewModel::class.java]

        // Init Firebase
        FirebaseService

        initViewModelLiveData()

        bindFunctionality()

        return binding.root
    }

    private fun initViewModelLiveData() {
        viewModel.altarBoys.observe(viewLifecycleOwner) {
            if (viewModel.altarBoys.value == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_altar_boys_names_problem),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                binding.spinnerAltarBoys.adapter = getAltarBoysSpinnerAdapter(
                    viewModel.altarBoys.value!!
                )
            }
        }

        viewModel.events.observe(viewLifecycleOwner) {
            if (viewModel.events.value == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_events_names_problem),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                binding.spinnerEvents.adapter = getEventsSpinnerAdapter(
                    viewModel.events.value!!
                )
            }
        }
    }

    private fun isBlankOption(id: Long): Boolean = id == 0L

    private fun bindFunctionality() {
        binding.btnEnroll.setOnClickListener {
            val pickedAltarBoyId = binding.spinnerAltarBoys.selectedItemId
            val pickedEventId = binding.spinnerEvents.selectedItemId

            val pickedAltarBoy = binding.spinnerAltarBoys.selectedItem as AltarBoy?
            val pickedEvent = binding.spinnerEvents.selectedItem as Event?

            val now = LocalDateTime.now()

            if (pickedAltarBoy == null || pickedEvent == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_no_data),
                    Toast.LENGTH_LONG
                ).show()
            } else if (
                isBlankOption(pickedAltarBoyId)
                || isBlankOption(pickedEventId)
            ) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_choose_again),
                    Toast.LENGTH_LONG
                ).show()
            } else if (loggedInLastHalfAnHour(pickedAltarBoy, now)) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.toast_frequent_log_in_protection),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                viewModel.createPresence(
                    pickedAltarBoy,
                    pickedEvent,
                    now,
                    this
                )

                binding.spinnerEvents.setSelection(0)
                binding.spinnerAltarBoys.setSelection(0)
            }
        }
    }

    private fun loggedInLastHalfAnHour(altarBoy: AltarBoy, now: LocalDateTime): Boolean {
        return altarBoy.lastLogin != null
                && LocalDateTime
                    .parse(altarBoy.lastLogin)
                    .plusMinutes(30)
                    .isAfter(now)
    }

    private fun getAltarBoysSpinnerAdapter(altarBoys: List<AltarBoy>):
            ArrayAdapter<AltarBoy> {

        var altarBoysToSpinner = altarBoys
        if (altarBoys.isNotEmpty() && altarBoys[0].name != getString(R.string.pick_encourage)) {
            altarBoysToSpinner =
                listOf(AltarBoy(getString(R.string.pick_encourage))) + altarBoys
        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.bigger_spinner_item,
            altarBoysToSpinner
        )

        adapter.setDropDownViewResource(R.layout.bigger_spinner_dropdown_item)
        return adapter
    }

    private fun getEventsSpinnerAdapter(events: List<Event>):
            ArrayAdapter<Event> {

        var eventsToSpinner = events
        if (events.isNotEmpty() && events[0].name != getString(R.string.pick_encourage)) {
            eventsToSpinner = listOf(Event(getString(R.string.pick_encourage))) + events
        }

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.bigger_spinner_item,
            eventsToSpinner
        )
        adapter.setDropDownViewResource(R.layout.bigger_spinner_dropdown_item)
        return adapter
    }

    override fun onUpdateSuccess() {
        Toast.makeText(
            context,
            getString(R.string.toast_presence_add_successful),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onUpdateFailure() {
        Toast.makeText(
            context,
            getString(R.string.toast_db_error_no_presence_add),
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}