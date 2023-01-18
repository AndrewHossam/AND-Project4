package com.udacity.project4.locationreminders.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.udacity.project4.ObjectFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
//Unit test the DAO
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RemindersDatabase

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun getAllReminder() = runBlockingTest {

        var result = database.reminderDao().getReminders()

        assertThat(result, CoreMatchers.`is`(emptyList()))

        database.reminderDao().saveReminder(ObjectFactory.reminder1)
        database.reminderDao().saveReminder(ObjectFactory.reminder2)
        database.reminderDao().saveReminder(ObjectFactory.reminder3)

        result = database.reminderDao().getReminders()

        assertThat(result, CoreMatchers.`is`(CoreMatchers.notNullValue()))

    }

    @Test
    fun saveReminder_getById() = runBlockingTest {

        database.reminderDao().saveReminder(ObjectFactory.reminder1)

        val result = database.reminderDao().getReminderById(ObjectFactory.reminder1.id)

        assertThat(result, CoreMatchers.`is`(CoreMatchers.notNullValue()))
        assertThat(result!!.id, CoreMatchers.`is`(ObjectFactory.reminder1.id))

    }

    @Test
    fun saveReminders_deleteAllReminders_emptyList() = runBlockingTest {
        database.reminderDao().saveReminder(ObjectFactory.reminder1)
        database.reminderDao().saveReminder(ObjectFactory.reminder2)
        database.reminderDao().saveReminder(ObjectFactory.reminder3)

        database.reminderDao().deleteAllReminders()

        val result = database.reminderDao().getReminders()

        assertThat(result, CoreMatchers.`is`(emptyList()))
    }


}