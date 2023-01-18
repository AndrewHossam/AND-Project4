package com.udacity.project4.locationreminders.data

import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.data.dto.Result

//Use FakeDataSource that acts as a test double to the LocalDataSource
class FakeDataSource(private val list: MutableList<ReminderDTO> = mutableListOf()) :
    ReminderDataSource {

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getReminders(): Result<List<ReminderDTO>> {
        if (shouldReturnError) return Result.Error("Error getting data")
        return Result.Success(list)
    }

    override suspend fun saveReminder(reminder: ReminderDTO) {
        list.add(reminder)
    }

    override suspend fun getReminder(id: String): Result<ReminderDTO> {
        if (shouldReturnError) return Result.Error("Error getting data")
        val foundItem = list.firstOrNull { it.id == id }
        return if (foundItem != null) {
            Result.Success(foundItem)
        } else {
            Result.Error("Item not found")
        }
    }

    override suspend fun deleteAllReminders() {
        list.clear()
    }
}