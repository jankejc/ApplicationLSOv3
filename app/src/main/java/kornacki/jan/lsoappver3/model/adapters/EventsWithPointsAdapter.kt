package kornacki.jan.lsoappver3.model.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kornacki.jan.lsoappver3.R
import kornacki.jan.lsoappver3.model.objects.Event

class EventsWithPointsAdapter(
    context: Context,
    events: List<Event>,
) : ArrayAdapter<Event>(context, R.layout.bigger_spinner_item, events) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(position: Int, convertView: View?, parent: ViewGroup): View {
        val event = getItem(position)
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.bigger_spinner_item, parent, false)
        val textView = view.findViewById<TextView>(R.id.tv_bigger_spinner_item)

        if (event?.points == null) {
            textView.text = event?.toString()
        } else {
            textView.text = event.toStringWithPoints()
        }

        return view
    }
}