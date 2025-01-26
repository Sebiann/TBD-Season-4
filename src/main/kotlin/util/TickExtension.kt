package util

/**
 * Example:
 * ```kt
 * 5.secondsToTicks() // -> 100 ticks
 * ```
 */
fun Int.secondsToTicks(): Int {
    return this.times(20)
}
