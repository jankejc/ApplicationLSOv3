package kornacki.jan.lsoappver3.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kornacki.jan.lsoappver3.R
import kornacki.jan.lsoappver3.viewModel.AdministrationViewModel
import kornacki.jan.lsoappver3.databinding.FragmentAdministrationBinding

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
    }

    private fun addAltarBoy() {
        val editText = EditText(requireContext())
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_type_altar_boy_name))
            .setView(editText)
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