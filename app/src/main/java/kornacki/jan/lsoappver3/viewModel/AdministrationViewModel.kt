package kornacki.jan.lsoappver3.viewModel

import androidx.lifecycle.ViewModel
import kornacki.jan.lsoappver3.model.objects.AltarBoy
import kornacki.jan.lsoappver3.model.services.FirebaseService

object AdministrationViewModel : ViewModel() {
    fun createAltarBoy(name: String, callback: FirebaseStatusCallback) {
        FirebaseService.createAltarBoy(AltarBoy(name.trim()), callback)
    }

    interface FirebaseStatusCallback {
        fun onUpdateSuccess()
        fun onUpdateFailure()
    }
}