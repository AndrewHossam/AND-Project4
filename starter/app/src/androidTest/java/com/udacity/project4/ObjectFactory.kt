package com.udacity.project4

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import kotlin.random.Random

object ObjectFactory {
    val reminder1 =
        ReminderDTO("title1", "desc1", "location1", Random.nextDouble(), Random.nextDouble())
    val reminder2 =
        ReminderDTO("title2", "desc2", "location2", Random.nextDouble(), Random.nextDouble())
    val reminder3 =
        ReminderDTO("title3", "desc3", "location3", Random.nextDouble(), Random.nextDouble())

}