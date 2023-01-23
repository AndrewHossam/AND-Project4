package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.ObjectFactory
import com.udacity.project4.locationreminders.data.dto.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Medium Test to test the repository
@MediumTest
class RemindersLocalRepositoryTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var remindersLocalRepository: RemindersLocalRepository
    private lateinit var database: RemindersDatabase

    @Before
    fun setup() {

        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        remindersLocalRepository = RemindersLocalRepository(
            database.reminderDao(),
            Dispatchers.Main
        )
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun save3Reminder_verify3ReminderRetrived() = runBlocking {

        var result = remindersLocalRepository.getReminders()

        MatcherAssert.assertThat(result is Result.Success, CoreMatchers.`is`(true))

        remindersLocalRepository.saveReminder(ObjectFactory.reminder1)
        remindersLocalRepository.saveReminder(ObjectFactory.reminder2)
        remindersLocalRepository.saveReminder(ObjectFactory.reminder3)

        result = remindersLocalRepository.getReminders()

        MatcherAssert.assertThat(result, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        result as Result.Success
        MatcherAssert.assertThat((result.data.size), CoreMatchers.`is`(3))

    }

    @Test
    fun getReminder_noReminderInDB_returnError() = runBlocking {


        val result = remindersLocalRepository.getReminder(ObjectFactory.reminder1.id)

        MatcherAssert.assertThat(
            result as Result.Error,
            CoreMatchers.`is`(CoreMatchers.notNullValue())
        )
        MatcherAssert.assertThat(result.message, CoreMatchers.`is`("Reminder not found!"))

    }

    @Test
    fun saveReminder_getById() = runBlocking {

        remindersLocalRepository.saveReminder(ObjectFactory.reminder1)

        val result = remindersLocalRepository.getReminder(ObjectFactory.reminder1.id)

        MatcherAssert.assertThat(
            result as Result.Success,
            CoreMatchers.`is`(CoreMatchers.notNullValue())
        )
        MatcherAssert.assertThat(result.data.id, CoreMatchers.`is`(ObjectFactory.reminder1.id))

    }

    @Test
    fun saveReminders_deleteAllReminders_emptyList() = runBlocking {
        remindersLocalRepository.saveReminder(ObjectFactory.reminder1)
        remindersLocalRepository.saveReminder(ObjectFactory.reminder2)
        remindersLocalRepository.saveReminder(ObjectFactory.reminder3)

        remindersLocalRepository.deleteAllReminders()

        val result = remindersLocalRepository.getReminders()

        MatcherAssert.assertThat((result as Result.Success).data, CoreMatchers.`is`(emptyList()))
    }

}