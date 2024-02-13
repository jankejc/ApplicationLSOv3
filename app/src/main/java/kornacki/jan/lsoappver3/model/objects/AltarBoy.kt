package kornacki.jan.lsoappver3.model.objects

data class AltarBoy(val name: String? = null, val presences: ArrayList<Presence>? = null) {
    override fun toString(): String {
        return name ?: ""
    }
}