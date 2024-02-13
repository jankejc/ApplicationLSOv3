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
        ADMIN_PASSWORD("admin_password")
    }

    private val db = Firebase.database
    private val altarBoysDbRef = db.getReference(UsefulNodesNames.ALTAR_BOYS.nodeName)
    private val eventsDbRef = db.getReference(UsefulNodesNames.EVENTS.nodeName)
    private val adminPasswordDbRef = db.getReference(UsefulNodesNames.ADMIN_PASSWORD.nodeName)
    private var adminPassword: String? = null

    init {
        readAltarBoys()
        readEvents()
        readAdminPassword()
    }

    fun getAdminPassword(): String? = adminPassword

    private fun readAdminPassword() {
        adminPasswordDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                adminPassword = dataSnapshot.getValue(String::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(
                    FIREBASE_SERVICE_TAG,
                    "Failed to read admin password value.",
                    error.toException()
                )
            }
        })
    }

    fun createAltarBoy(
        altarBoy: AltarBoy,
        callback: AdministrationViewModel.FirebaseStatusCallback,
    ) {
        // I know that I have here non-null object,
        // because in alert I accept only non-empty string,
        // so object must have been well-initialized.
        altarBoysDbRef
            .child(altarBoy.name!!)
            .setValue(altarBoy)
            .addOnCompleteListener { task ->
                // This is asynchronous...
                if (task.isSuccessful) {
                    callback.onUpdateSuccess()
                } else {
                    callback.onUpdateFailure()
                }
            }
    }

    fun updateAltarBoy(altarBoy: AltarBoy, callback: EnrollmentViewModel.FirebaseStatusCallback) {
        altarBoysDbRef
            .child(altarBoy.name!!)
            .setValue(altarBoy)
            .addOnCompleteListener { task ->
                // This is asynchronous...
                if (task.isSuccessful) {
                    callback.onUpdateSuccess()
                } else {
                    callback.onUpdateFailure()
                }
            }
    }

    private fun readAltarBoys() {
        altarBoysDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val altarBoys = arrayListOf<AltarBoy>()
                for (childSnapshot in dataSnapshot.children) {
                    val altarBoy = childSnapshot.getValue(AltarBoy::class.java)
                    altarBoy?.let {
                        altarBoys.add(it)
                    }
                }
                EnrollmentViewModel.updateAltarBoys(altarBoys)
            }

            override fun onCancelled(error: DatabaseError) {
                // There is toast in EnrollmentFragment that react to nulls in fields.
                Log.w(
                    FIREBASE_SERVICE_TAG,
                    "Failed to read value.",
                    error.toException()
                )
            }
        })
    }

    fun createEvent(event: Event, callback: AdministrationViewModel.FirebaseStatusCallback) {
        eventsDbRef
            .child(event.name!!)
            .setValue(event)
            .addOnCompleteListener { task ->
                // This is asynchronous...
                if (task.isSuccessful) {
                    callback.onUpdateSuccess()
                } else {
                    callback.onUpdateFailure()
                }
            }
    }

    private fun readEvents() {
        eventsDbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val events = arrayListOf<Event>()
                for (childSnapshot in dataSnapshot.children) {
                    val event = childSnapshot.getValue(Event::class.java)
                    event?.let {
                        events.add(it)
                    }
                }
                EnrollmentViewModel.updateEvents(events)
            }

            override fun onCancelled(error: DatabaseError) {
                // There is toast in EnrollmentFragment that react to nulls in fields.
                Log.w(
                    FIREBASE_SERVICE_TAG,
                    "Failed to read value.",
                    error.toException()
                )
            }
        })
    }
}