package kornacki.jan.lsoappver3.model.objects

data class AltarBoy(
    val name: String? = null,
    var lastLogin: String? = null,
    var presences: ArrayList<Presence>? = null
) {
    override fun toString(): String {
        return name ?: ""
    }
}