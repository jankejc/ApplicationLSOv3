package kornacki.jan.lsoappver3.model.objects

data class Event(val name: String? = null, val points: Int? = null) {
    constructor() : this("", 0)

    fun toStringWithPoints(): String {
        return "$name ($points)"
    }

    override fun toString(): String {
        return name ?: ""
    }
}