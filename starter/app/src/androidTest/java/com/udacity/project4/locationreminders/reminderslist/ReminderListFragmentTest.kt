package com.udacity.project4.locationreminders.reminderslist

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.udacity.project4.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
//UI Testing
@MediumTest
class ReminderListFragmentTest {

    //    TODO: test the navigation of the fragments.
    @Test
    fun clickAddReminder_navigateToLocationLocationReminder() {

        val navController = TestNavHostController(
            ApplicationProvider.getApplicationContext())


        launchFragmentInContainer<ReminderListFragment>(themeResId = R.style.AppTheme).onFragment {
            navController.setGraph(R.navigation.nav_graph)
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(ViewMatchers.withId(R.id.addReminderFAB)).perform(ViewActions.click())
        runBlocking {

            delay(1000)
        }
        println(navController.currentDestination?.navigatorName)
        assertThat(navController.currentDestination?.id,
            `is`(ReminderListFragmentDirections.toSaveReminder().actionId))

    }
//    TODO: test the displayed data on the UI.
//    TODO: add testing for the error messages.
}