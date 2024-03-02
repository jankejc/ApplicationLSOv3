package kornacki.jan.lsoappver3.view

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kornacki.jan.lsoappver3.R
import kornacki.jan.lsoappver3.databinding.FragmentLeaderboardResultsBinding
import kornacki.jan.lsoappver3.viewModel.LeaderboardResultsViewModel
import java.time.LocalDateTime

class LeaderboardResultsFragment : Fragment() {
    private var _binding: FragmentLeaderboardResultsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var viewModel: LeaderboardResultsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentLeaderboardResultsBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(this)[LeaderboardResultsViewModel::class.java]

        inflateListView()

        bindFunctionality()

        return binding.root
    }

    private fun inflateListView() {
        binding.lvAltarBoysInOrder.adapter = getLeaderboardListViewAdapter(
            viewModel.leaderboard
        )
    }

    private fun bindFunctionality() {
        binding.btnDownloadCsv.setOnClickListener {
            exportLeaderboardToCSV()
        }
    }

    private fun getLeaderboardListViewAdapter(leaderboard: List<String>):
            ArrayAdapter<String> {

        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.bigger_spinner_item,
            leaderboard
        )

        adapter.setDropDownViewResource(R.layout.bigger_spinner_dropdown_item)
        return adapter
    }

    private fun getContainerizedView(view: View): LinearLayout {
        val container = LinearLayout(context)
        container.setPadding(30)
        container.orientation = LinearLayout.VERTICAL
        container.addView(view)

        return container
    }

    private fun String.containsOnlyLetters(): Boolean {
        return this.all { it.isLetter() }
    }

    private fun isWrongFilename(filename: String): Boolean {
        return filename == "" || !filename.containsOnlyLetters()
    }

    private fun exportLeaderboardToCSV() {
        val editText = EditText(requireContext())

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_type_results_filename))
            .setView(getContainerizedView(editText))
            .setPositiveButton(getString(R.string.alert_option_accept)) { _, _ ->
                if (isWrongFilename(editText.text.toString())) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_wrong_filename),
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    exportFile(editText.text.toString())
                }
            }
            .setNegativeButton(getString(R.string.alert_option_decline), null)
            .show()
    }

    private fun exportFile(filename: String) {
        val csvStringFromLeaderboard =
            viewModel.getCSVFromLeaderboard(getString(R.string.csv_file_header))

        saveToDocuments(requireContext(), filename, csvStringFromLeaderboard)
    }

    private fun saveToDocuments(context: Context, filename: String, content: String) {
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename + "_" + LocalDateTime.now() + ".csv")
            put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS)
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Files.getContentUri("external"), values)

        try {
            uri?.let {
                resolver.openOutputStream(it).use { outputStream ->
                    outputStream?.write(content.toByteArray())
                }
            }

            Toast.makeText(
                context,
                getString(R.string.toast_results_downloaded),
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                getString(R.string.toast_results_not_downloaded),
                Toast.LENGTH_LONG
            ).show()
            e.printStackTrace()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}