package kornacki.jan.lsoappver3.viewModel

import androidx.lifecycle.ViewModel
import kornacki.jan.lsoappver3.model.objects.AltarBoy
import kornacki.jan.lsoappver3.model.objects.Event
import java.time.LocalDate
import java.time.LocalDateTime

object LeaderboardOptionsViewModel : ViewModel() {
    private var startDate: LocalDateTime = LocalDateTime.MIN
    private var endDate: LocalDateTime = LocalDateTime.MAX
    var altarBoys: List<AltarBoy> = listOf()
    var events: List<Event> = listOf()
    private val eventsToFilter: MutableList<Event> = mutableListOf()
    private var leaderboard: List<String> = listOf()

    fun resetOptions() {
        startDate = LocalDateTime.MIN
        endDate = LocalDateTime.MAX
        eventsToFilter.clear()
        leaderboard = listOf()
    }
    fun prepareStartDateToCompare(year: Int, month: Int, day: Int) {
        startDate = LocalDateTime.of(year, month, day, 0, 0)
    }

    fun prepareEndDateToCompare(year: Int, month: Int, day: Int) {
        endDate = LocalDateTime.of(year, month, day, 0, 0)
    }

    fun addEventToFilter(event: Event) {
        eventsToFilter.add(event)
    }

    fun removeEventFromFilter(event: Event) {
        eventsToFilter.remove(event)
    }

    private fun filterAndSumPoints(): Map<AltarBoy, Int> {
        return altarBoys.associateWith { altarBoy ->
            altarBoy.presences?.sumOf { presence ->
                val presenceDate = LocalDateTime.parse(presence.date)
                if (presenceDate.isAfter(startDate) && presenceDate.isBefore(endDate) &&
                    (eventsToFilter.isEmpty() || eventsToFilter.contains(presence.event))
                ) {
                    presence.event.points ?: 0
                } else {
                    0
                }
            } ?: 0
        }
    }

    private fun orderScores(sortedScores: Map<AltarBoy, Int>): List<String> {
        var currentRank = 1
        var lastScore = Int.MAX_VALUE
        var displayRank = 1

        return sortedScores.entries.map { (altarBoy, score) ->
            val rank = if (score == lastScore) {
                currentRank++
                displayRank
            } else {
                displayRank = currentRank
                currentRank++
            }
            lastScore = score
            "$rank. ${altarBoy.name}: $score"
        }
    }

    fun prepareLeaderboard() {
        val scores = filterAndSumPoints()
        val sortedScores = scores.toList().sortedByDescending { (_, value) -> value }.toMap()
        leaderboard = orderScores(sortedScores)
    }

    fun passLeaderboardToResultsFragment() {
        LeaderboardResultsViewModel.leaderboard = leaderboard
    }

    fun getStartDateYear(): Int {
        return if (startDate == LocalDateTime.MIN) {
            LocalDate.now().year
        } else {
            startDate.year
        }
    }

    fun getStartDateMonth(): Int {
        // month - 1 because Calendar#MONTH has 0-11 values
        return if (startDate == LocalDateTime.MIN) {
            LocalDate.now().monthValue - 1
        } else {
            startDate.monthValue - 1
        }
    }

    fun getStartDateDayOfMonth(): Int {
        return if (startDate == LocalDateTime.MIN) {
            LocalDate.now().dayOfMonth
        } else {
            startDate.dayOfMonth
        }
    }

    fun getEndDateYear(): Int {
        return if (endDate == LocalDateTime.MAX) {
            LocalDate.now().year
        } else {
            endDate.year
        }
    }

    fun getEndDateMonth(): Int {
        // month - 1 because Calendar#MONTH has 0-11 values
        return if (endDate == LocalDateTime.MAX) {
            LocalDate.now().monthValue - 1
        } else {
            endDate.monthValue - 1
        }
    }

    fun getEndDateDayOfMonth(): Int {
        return if (endDate == LocalDateTime.MAX) {
            LocalDate.now().dayOfMonth
        } else {
            endDate.dayOfMonth
        }
    }
}