package kornacki.jan.lsoappver3.view

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kornacki.jan.lsoappver3.databinding.FragmentLeaderboardBinding
import kornacki.jan.lsoappver3.viewModel.LeaderboardViewModel
import java.time.LocalDate

class LeaderboardFragment : Fragment() {
    private var _binding: FragmentLeaderboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: LeaderboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[LeaderboardViewModel::class.java]

        bindFunctionality()

        return binding.root
    }

    private fun bindFunctionality() {
        binding.btnChooseStartDay.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                viewModel.prepareStartDateToCompare(year, month, dayOfMonth)
            }, LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
            datePickerDialog.show()
        }

        binding.btnChooseEndDay.setOnClickListener {
            val datePickerDialog = DatePickerDialog(requireContext(), { _, year, month, dayOfMonth ->
                viewModel.prepareEndDateToCompare(year, month, dayOfMonth)
            }, LocalDate.now().year, LocalDate.now().monthValue, LocalDate.now().dayOfMonth)
            datePickerDialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}