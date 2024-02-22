package kornacki.jan.lsoappver3.viewModel

import androidx.lifecycle.ViewModel
import java.time.LocalDateTime

object LeaderboardViewModel : ViewModel() {
    private var startDate: LocalDateTime = LocalDateTime.MIN
    private var endDate: LocalDateTime = LocalDateTime.MIN

    fun prepareStartDateToCompare(year: Int, month: Int, day: Int) {
        startDate = LocalDateTime.of(year, month, day, 0, 0)
    }

    fun prepareEndDateToCompare(year: Int, month: Int, day: Int) {
        endDate = LocalDateTime.of(year, month, day, 0, 0)
    }
}