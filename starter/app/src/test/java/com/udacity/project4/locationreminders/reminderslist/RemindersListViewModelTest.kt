package com.udacity.project4.locationreminders.reminderslist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.project4.locationreminders.MainCoroutineRule
import com.udacity.project4.locationreminders.data.FakeDataSource
import com.udacity.project4.locationreminders.data.ReminderDataSource
import com.udacity.project4.locationreminders.data.dto.ReminderDTO
import com.udacity.project4.locationreminders.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.not
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.nullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class RemindersListViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: RemindersListViewModel

    private lateinit var reminderDataSource: FakeDataSource

    @Before
    fun setup() {
        reminderDataSource = FakeDataSource()
        viewModel =
            RemindersListViewModel(ApplicationProvider.getApplicationContext(), reminderDataSource)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun getAllReminders_listNotNull() {
        viewModel.loadReminders()
        val reminders = viewModel.remindersList.getOrAwaitValue()
        assertThat(reminders, `is`(not(nullValue())))
    }

    @Test
    fun addReminder_listNotEmpty() = mainCoroutineRule.runBlockingTest {
        val item = ReminderDTO("Title", "Desc", "Location", 123.1, 31.4)
        reminderDataSource.saveReminder(item)
        viewModel.loadReminders()
        val reminders = viewModel.remindersList.getOrAwaitValue()
        assertThat(reminders.isNotEmpty(), `is`(true))
    }

    @Test
    fun getAllReminders_showLoading() {
        mainCoroutineRule.pauseDispatcher()

        viewModel.loadReminders()
        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(true))


        mainCoroutineRule.resumeDispatcher()
        assertThat(viewModel.showLoading.getOrAwaitValue(), `is`(false))
    }
}