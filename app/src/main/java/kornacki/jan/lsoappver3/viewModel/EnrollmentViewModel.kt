package kornacki.jan.lsoappver3.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kornacki.jan.lsoappver3.model.objects.AltarBoy
import kornacki.jan.lsoappver3.model.objects.Event
import kornacki.jan.lsoappver3.model.objects.Presence
import kornacki.jan.lsoappver3.model.services.FirebaseService

object EnrollmentViewModel : ViewModel() {
    // TODO: callback from db and toast when success and failure
    // LiveData
    private val _altarBoys = MutableLiveData<ArrayList<AltarBoy>>()
    val altarBoys: LiveData<ArrayList<AltarBoy>> = _altarBoys

    private val _events = MutableLiveData<ArrayList<Event>>()
    val events: LiveData<ArrayList<Event>> = _events

    /**
     * When database react to changed data it calls this method to update the list.
     * ViewModel lives until activity doesn't die.
     */
    fun updateAltarBoys(altarBoys: ArrayList<AltarBoy>) {
        _altarBoys.value = altarBoys
    }

    /**
     * When database react to changed data it calls this method to update the list.
     * ViewModel lives until activity doesn't die.
     */
    fun updateEvents(events: ArrayList<Event>) {
        _events.value = events
    }

    fun createPresence(
        altarBoy: AltarBoy,
        presence: Presence,
        callback: FirebaseStatusCallback
    ) {
        if (altarBoy.presences == null) {
            altarBoy.presences = ArrayList()
        }
        altarBoy.presences!!.add(presence)
        FirebaseService.updateAltarBoy(altarBoy, callback)
    }

    interface FirebaseStatusCallback {
        fun onUpdateSuccess()
        fun onUpdateFailure()
    }
}