package kornacki.jan.lsoappver3.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kornacki.jan.lsoappver3.model.objects.AltarBoy
import kornacki.jan.lsoappver3.model.objects.Event
import kornacki.jan.lsoappver3.model.objects.Presence
import kornacki.jan.lsoappver3.model.services.FirebaseService
import java.time.LocalDateTime

object EnrollmentViewModel : ViewModel() {
    // LiveData
    private val _altarBoys = MutableLiveData<List<AltarBoy>>()
    val altarBoys: LiveData<List<AltarBoy>> = _altarBoys

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events

    /**
     * When database react to changed data it calls this method to update the list.
     * ViewModel lives until activity doesn't die.
     */
    fun updateAltarBoys(altarBoys: List<AltarBoy>) {
        _altarBoys.value = altarBoys
    }

    /**
     * When database react to changed data it calls this method to update the list.
     * ViewModel lives until activity doesn't die.
     */
    fun updateEvents(events: List<Event>) {
        _events.value = events
    }

    fun createPresence(
        altarBoy: AltarBoy,
        event: Event,
        now: LocalDateTime,
        callback: FirebaseStatusCallback
    ) {
        if (altarBoy.presences == null) {
            altarBoy.presences = ArrayList()
        }
        altarBoy.lastLogin = now.toString()
        altarBoy.presences!!.add(Presence(event, now.toString()))
        FirebaseService.updateAltarBoy(altarBoy, callback)
    }

    interface FirebaseStatusCallback {
        fun onUpdateSuccess()
        fun onUpdateFailure()
    }
}