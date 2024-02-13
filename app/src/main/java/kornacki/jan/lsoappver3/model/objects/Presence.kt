package kornacki.jan.lsoappver3.model.objects

data class Presence(val event: Event, val date: String) {
    constructor() : this(Event(), "")
}