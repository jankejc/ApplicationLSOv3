package kornacki.jan.lsoappver3.model.objects

data class Event(val name: String? = null, val points: Int? = null) {
    constructor() : this("", 0)
    override fun toString(): String {
        return name ?: ""
    }
}