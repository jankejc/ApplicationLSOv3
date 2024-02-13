package kornacki.jan.lsoappver3.model.services

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kornacki.jan.lsoappver3.model.objects.AltarBoy
import kornacki.jan.lsoappver3.model.objects.Event
import kornacki.jan.lsoappver3.viewModel.AdministrationViewModel
import kornacki.jan.lsoappver3.viewModel.EnrollmentViewModel

const val FIREBASE_SERVICE_TAG = "Firebase Service"

object FirebaseService {
    enum class UsefulNodesNames(val nodeName: String) {
        ALTAR_BOYS("altar_boys"),
        EVENTS("events"),
    }

    private val db = Firebase.database
    private val altarBoysDbRef = db.getReference(UsefulNodesNames.ALTAR_BOYS.nodeName)
    private val eventsDbRef = db.getReference(UsefulNodesNames.EVENTS.nodeName)

    init {
        readAltarBoys()
    }

    fun createAltarBoy(
        altarBoy: AltarBoy,
        callback: AdministrationViewModel.FirebaseStatusCallback
    ) {
        // I know that I have here non-null object,
        // because in alert I accept only non-empty string,
        // so object must have been well-initialized.
        altarBoysDbRef.child(altarBoy.name!!).setValue(altarBoy)
            .addOnCompleteListener { task ->
                // This is asynchronous...
                if (task.isSuccessful) {
                    callback.onUpdateSuccess()
                } else {
                    callback.onUpdateFailure()
                }
            }
    }

    fun updateAltarBoy(altarBoy: AltarBoy) {
        // TODO: what if failure
        // TODO: null check
        altarBoysDbRef
            .child(altarBoy.name!!)
            .setValue(altarBoy)
    }

    private fun readAltarBoys() {
        val altarBoys = arrayListOf<AltarBoy>()
        altarBoysDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val altarBoy = childSnapshot.getValue(AltarBoy::class.java)
                    altarBoy?.let {
                        altarBoys.add(it)
                    }
                }
                EnrollmentViewModel.updateAltarBoys(altarBoys)
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO: what if failure
                Log.w(
                    FIREBASE_SERVICE_TAG,
                    "Failed to read value.",
                    error.toException()
                )
            }
        })
    }

    fun createEvent(event: Event) {
        // TODO: what if failure
        // TODO: null check
        eventsDbRef.child(event.name!!).setValue(event)
    }

    fun updateEvent(event: Event) {
        // TODO: what if failure
        // TODO: null check
        eventsDbRef
            .child(event.name!!)
            .setValue(event)
    }

    private fun readEvents() {
        val events = arrayListOf<Event>()
        eventsDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (childSnapshot in dataSnapshot.children) {
                    val event = childSnapshot.getValue(Event::class.java)
                    event?.let {
                        events.add(it)
                    }
                }
                EnrollmentViewModel.updateEvents(events)
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO: what if failure
                Log.w(
                    FIREBASE_SERVICE_TAG,
                    "Failed to read value.",
                    error.toException()
                )
            }
        })
    }
}