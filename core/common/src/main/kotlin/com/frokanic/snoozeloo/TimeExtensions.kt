package com.frokanic.snoozeloo

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

fun Pair<Int?, Int?>.updateTime(): Pair<Int, Int>? {

    if (first == null || second == null)
        return null

    val hour = first!!
    val minute = second!!
    val now = LocalTime.now()
    val target = LocalTime.of(hour, minute)
    var duration = ChronoUnit.MINUTES.between(now, target)

    if (duration < 0) {
        duration += 24 * 60
    }

    val hoursUntil = (duration / 60).toInt()
    val minutesUntil = (duration % 60).toInt()

    return Pair(hoursUntil, minutesUntil)
}

fun Pair<Int, Int>?.formatTimeForUi(): String {
    if (this == null) return "No alarm set"

    val (hours, minutes) = this
    val hoursText = if (hours > 0) "${hours}h" else ""
    val minuteText = if (minutes > 0) "${minutes}m" else ""

    return when {
        hours == 0 && minutes == 0 -> "Alarm now"
        hours == 0 && minutes > 0 -> "Alarm in $minuteText"
        hours > 0 && minutes == 0 -> "Alarm in $hoursText"
        else -> "Alarm in $hoursText $minuteText"
    }.trim()
}

fun Pair<Int, Int>.toFormattedTimePair(): Pair<String, DayPeriod> {
    val (hour, minute) = this
    val period = if (hour < 12) DayPeriod.AM else DayPeriod.PM
    val formattedHour = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    val formattedMinute = minute.toString().padStart(2, '0')
    val time = "$formattedHour:$formattedMinute"
    return Pair(time, period)
}

fun Pair<Int, Int>.getNextDateTime(): LocalDateTime {
    val (hour, minute) = this

    val now = LocalDateTime.now()
    var nextAlarm = LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute))

    if (!nextAlarm.isAfter(now)) {
        nextAlarm = nextAlarm.plusDays(1)
    }

    return nextAlarm
}