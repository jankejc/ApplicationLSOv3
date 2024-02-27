package kornacki.jan.lsoappver3.model.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import kornacki.jan.lsoappver3.R
import kornacki.jan.lsoappver3.model.objects.Event
import kornacki.jan.lsoappver3.viewModel.LeaderboardOptionsViewModel

class EventsToFilterOptionsAdapter(
    context: Context,
    private val events: List<Event>,
) : ArrayAdapter<Event>(context, 0, events) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View {
        val view =
            convertView ?: LayoutInflater.from(context).inflate(R.layout.checkbox_filter_option, parent, false)
        val item = events[position]
        val checkBox = view.findViewById<CheckBox>(R.id.checkbox_filter_option)
        checkBox.text = item.toString()
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                LeaderboardOptionsViewModel.addEventToFilter(item)
            } else {
                LeaderboardOptionsViewModel.removeEventFromFilter(item)
            }
        }
        return view
    }
}