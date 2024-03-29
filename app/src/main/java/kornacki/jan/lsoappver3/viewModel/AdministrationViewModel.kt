package kornacki.jan.lsoappver3.viewModel

import androidx.lifecycle.ViewModel
import kornacki.jan.lsoappver3.model.objects.AltarBoy
import kornacki.jan.lsoappver3.model.objects.Event
import kornacki.jan.lsoappver3.model.services.FirebaseService

object AdministrationViewModel : ViewModel() {
    var altarBoys: List<AltarBoy> = listOf()
    var events: List<Event> = listOf()

    fun createAltarBoy(name: String, callback: FirebaseStatusCallback) {
        FirebaseService.createAltarBoy(AltarBoy(name.trim()), callback)
    }

    fun createEvent(name: String, points: Int, callback: FirebaseStatusCallback) {
        FirebaseService.createEvent(Event(name.trim(), points), callback)
    }

    fun deleteAltarBoy(altarBoy: AltarBoy, callback: FirebaseStatusCallback) {
        FirebaseService.deleteAltarBoy(altarBoy, callback)
    }

    fun deleteEvent(event: Event, callback: FirebaseStatusCallback) {
        FirebaseService.deleteEvent(event, callback)
    }

    interface FirebaseStatusCallback {
        fun onUpdateSuccess()
        fun onUpdateFailure()
    }
}