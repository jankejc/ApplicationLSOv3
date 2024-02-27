package kornacki.jan.lsoappver3.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import kornacki.jan.lsoappver3.R
import kornacki.jan.lsoappver3.databinding.FragmentLeaderboardOptionsBinding
import kornacki.jan.lsoappver3.model.adapters.EventsToFilterOptionsAdapter
import kornacki.jan.lsoappver3.model.objects.Event
import kornacki.jan.lsoappver3.viewModel.LeaderboardOptionsViewModel
import java.time.LocalDate

class LeaderboardOptionsFragment : Fragment() {
    private var _binding: FragmentLeaderboardOptionsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: LeaderboardOptionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentLeaderboardOptionsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[LeaderboardOptionsViewModel::class.java]

        inflateListView()

        bindFunctionality()

        // when going back from results i need to have reset options because i can't rollback the
        // view state
        viewModel.resetOptions()

        return binding.root
    }

    private fun inflateListView() {
        binding.lvEventsList.adapter = getEventsListViewAdapter(viewModel.events)
    }

    private fun bindFunctionality() {
        binding.btnChooseStartDay.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    // month + 1 because Calendar#MONTH has 0-11 values
                    viewModel.prepareStartDateToCompare(year, month + 1, dayOfMonth)
                    binding.tvStartDayStatus.text = LocalDate.of(year, month + 1, dayOfMonth)
                        .toString()
                },
                viewModel.getStartDateYear(),
                viewModel.getStartDateMonth(),
                viewModel.getStartDateDayOfMonth()
            )
            datePickerDialog.show()
        }

        binding.btnChooseEndDay.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    viewModel.prepareEndDateToCompare(year, month + 1, dayOfMonth)
                    binding.tvEndDayStatus.text = LocalDate.of(year, month + 1, dayOfMonth)
                        .toString()
                },
                viewModel.getEndDateYear(),
                viewModel.getEndDateMonth(),
                viewModel.getEndDateDayOfMonth()
            )
            datePickerDialog.show()
        }

        binding.btnFilter.setOnClickListener {
            viewModel.prepareLeaderboard()
            viewModel.passLeaderboardToResultsFragment()
            requireActivity().findNavController(R.id.nav_host_fragment_content_main)
                .navigate(R.id.action_LeaderboardOptionsFragment_to_LeaderboardResultsFragment)
        }
    }

    private fun getEventsListViewAdapter(events: List<Event>):
            EventsToFilterOptionsAdapter {

        val adapter = EventsToFilterOptionsAdapter(
            requireContext(),
            events
        )
        adapter.setDropDownViewResource(R.layout.bigger_spinner_dropdown_item)
        return adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}