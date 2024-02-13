package kornacki.jan.lsoappver3.model.objects

import java.time.LocalDateTime

class Presence(val event: Event, val date: String) {
    constructor() : this(Event(), "")
}