package kornacki.jan.lsoappver3.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import kornacki.jan.lsoappver3.databinding.FragmentEnrollmentBinding
import kornacki.jan.lsoappver3.viewModel.EnrollmentViewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class EnrollmentFragment : Fragment() {

    companion object {
        fun newInstance() = EnrollmentFragment()
    }
    private var _binding: FragmentEnrollmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: EnrollmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEnrollmentBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[EnrollmentViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /* TODO
        val spinnerData = listOf("Stasiek", "Jasiek", "Czesiek")

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, spinnerData)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spinnerAltarBoys.adapter = adapter
         */

        /* TODO
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_EnrollmentFragment_to_LeaderboardFragment)
        }
        */
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}