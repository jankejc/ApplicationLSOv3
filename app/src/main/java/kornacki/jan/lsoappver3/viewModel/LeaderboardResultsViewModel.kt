package kornacki.jan.lsoappver3.viewModel

import androidx.lifecycle.ViewModel

object LeaderboardResultsViewModel : ViewModel() {
    var leaderboard: List<String> = listOf()

    fun getCSVFromLeaderboard(csvHeader: String): String {
        val stringBuilder = StringBuilder(csvHeader).append("\n")

        leaderboard.forEach { row ->
            stringBuilder
                .append(row.replace(": ", ",").replace(". ", ","))
                .append("\n")
        }

        return stringBuilder.toString()
    }
}