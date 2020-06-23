package org.dhis2.usescases.teidashboard

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.dhis2.R
import org.dhis2.common.rules.DataBindingIdlingResourceRule
import org.dhis2.usescases.BaseTest
import org.dhis2.usescases.searchTrackEntity.SearchTEActivity
import org.dhis2.usescases.searchte.searchTeiRobot
import org.dhis2.usescases.teiDashboard.TeiDashboardMobileActivity
import org.dhis2.usescases.teiDashboard.dashboardfragments.teidata.DashboardProgramViewHolder
import org.dhis2.usescases.teidashboard.entity.EnrollmentUIModel
import org.dhis2.usescases.teidashboard.entity.UpperEnrollmentUIModel
import org.dhis2.usescases.teidashboard.robot.enrollmentRobot
import org.dhis2.usescases.teidashboard.robot.eventRobot
import org.dhis2.usescases.teidashboard.robot.indicatorsRobot
import org.dhis2.usescases.teidashboard.robot.noteRobot
import org.dhis2.usescases.teidashboard.robot.relationshipRobot
import org.dhis2.usescases.teidashboard.robot.teiDashboardRobot
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.experimental.theories.Theories
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TeiDashboardTest : BaseTest() {

    @get:Rule
    val rule = ActivityTestRule(TeiDashboardMobileActivity::class.java, false, false)
    @get:Rule
    val ruleSearch = ActivityTestRule(SearchTEActivity::class.java, false, false)

    @Test
    fun shouldSuccessfullyCreateANoteWhenClickCreateNote() {
        setupCredentials()

        prepareTeiCompletedProgrammeAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnNotesTab()
        }

        noteRobot {
            clickOnFabAddNewNote()
            typeNote(NOTE_VALID)
            clickOnSaveButton()
            checkNewNoteWasCreated(NOTE_VALID)
        }
    }

    @Test
    fun shouldNotCreateANoteWhenClickClear() {
        prepareTeiCompletedProgrammeAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnNotesTab()
        }

        noteRobot {
            clickOnFabAddNewNote()
            typeNote(NOTE_INVALID)
            clickOnClearButton()
            clickYesOnAlertDialog()
            checkNoteWasNotCreated(NOTE_INVALID)
        }
    }

    @Test
    fun shouldOpenNotesDetailsWhenClickOnNote() {
        prepareTeiWithExistingNoteAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnNotesTab()
        }

        noteRobot {
            clickOnNoteWithPosition(0)
            checkNoteDetails("@$USER", NOTE_EXISTING_TEXT)
        }
    }

    @Test
    fun shouldReactivateTEIWhenClickReOpen() {
        prepareTeiCompletedProgrammeAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnMenuMoreOptions()
            clickOnMenuReOpen()
            checkUnlockIconIsDisplay()
            checkCanAddEvent()
        }
    }

    @Test
    fun shouldDeactivateTEIWhenClickOpen() {
        prepareTeiOpenedProgrammeAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnMenuMoreOptions()
            clickOnMenuDeactivate()
            checkLockIconIsDisplay()
            checkCanNotAddEvent()
        }
    }

    @Test
    fun shouldCompleteTEIWhenClickOpen() {
        prepareTeiOpenedForCompleteProgrammeAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnMenuMoreOptions()
            clickOnMenuComplete()
            checkLockCompleteIconIsDisplay()
            checkCanNotAddEvent()
        }
    }

    @Test
    fun shouldShowQRWhenClickOnShare() {
        prepareTeiCompletedProgrammeAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnShareButton()
            clickOnNextQR()
        }
    }

    @Test
    fun shouldMakeAReferral() {
        prepareTeiOpenedForReferralProgrammeAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnFab()
            clickOnReferral()
            clickOnFirstReferralEvent()
            clickOnReferralOption()
            clickOnReferralNextButton()
            checkEventCreatedToastIsShown()
            checkEventWasCreated(LAB_MONITORING)
        }
    }

    @Test
    fun shouldSuccessfullyScheduleAnEvent() {
        prepareTeiOpenedWithNoPreviousEventProgrammeAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnFab()
            clickOnScheduleNew()
            clickOnFirstReferralEvent()
            clickOnReferralNextButton()
            checkEventCreatedToastIsShown()
            checkEventWasCreated(LAB_MONITORING)
        }
    }

    @Test
    fun shouldNotBeAbleToCreateNewEventsWhenFull() {
        prepareTeiOpenedWithFullEventsAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnFab()
            clickOnReferral()
            checkCannotAddMoreEventToastIsShown()
        }
    }

    @Test
    fun shouldOpenEventAndSaveSuccessfully() {
        setupCredentials()

        prepareTeiOpenedProgrammeAndLaunchActivity(rule)

        val babyPostNatal = 0
        teiDashboardRobot {
            clickOnEventWithPosition(babyPostNatal)
        }

        eventRobot {
            scrollToBottomForm()
            clickOnFormFabButton()
            clickOnFinish()
        }
    }

    @Test
    fun shouldShowCorrectInfoWhenOpenTEI() {
        prepareTeiCompletedProgrammeAndLaunchActivity(rule)

        val upperInformation = createExpectedUpperInformation()

        teiDashboardRobot {
            checkUpperInfo(upperInformation)
        }
    }

    @Test
    fun shouldShowTEIDetailsWhenClickOnSeeDetails() {
        prepareTeiCompletedProgrammeAndLaunchActivity(rule)

        val enrollmentFullDetails = createExpectedEnrollmentInformation()

        teiDashboardRobot {
            clickOnSeeDetails()
            checkFullDetails(enrollmentFullDetails)
        }
    }

    @Test
    fun shouldShowIndicatorsDetailsWhenClickOnIndicatorsTab() {
        prepareTeiCompletedProgrammeAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnIndicatorsTab()
        }

        indicatorsRobot {
            checkDetails("0", "4817")
        }
    }

    @Test
    fun shouldSuccessfullyCreateANewEvent() {
        prepareTeiToCreateANewEventAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnFab()
            clickOnCreateNewEvent()
            clickOnFirstReferralEvent()
            clickOnReferralNextButton()
            waitToDebounce(600)
        }

        eventRobot {
            fillRadioButtonForm(4)
            clickOnFormFabButton()
            clickOnFinish()
        }

        teiDashboardRobot {
            checkEventWasCreatedAndOpen(LAB_MONITORING, 0)
        }
    }

    @Test
    fun shouldOpenEventEditAndSaveSuccessfully() {
        prepareTeiOpenedToEditAndLaunchActivity(rule)

        val labMonitoring = 2

        teiDashboardRobot {
            clickOnEventWithPosition(labMonitoring)
            waitToDebounce(600)
        }

        eventRobot {
            clickOnChangeDate()
            clickOnEditDate()
            acceptUpdateEventDate()
            clickOnUpdate()
            waitToDebounce(600)
            fillRadioButtonForm(4)
            clickOnFormFabButton()
            clickOnFinishAndComplete()
            waitToDebounce(600)
        }

        teiDashboardRobot {
            checkEventWasCreatedAndClosed(LAB_MONITORING, 3)
        }
    }

    @Test
    @Ignore
    fun shouldEnrollToOtherProgramWhenClickOnProgramEnrollments() {
        val womanProgram = 4
        prepareTeiToEnrollToOtherProgramAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnMenuMoreOptions()
            clickOnMenuProgramEnrollments()
        }

        enrollmentRobot {
            clickOnAProgramForEnrollment(womanProgram)
            clickOnAcceptEnrollmentDate()
            clickOnPersonAttributes(5)
            clickOnCalendarItem(5)
            clickOnAcceptEnrollmentDate()
            scrollToBottomProgramForm()
            clickOnSaveEnrollment()
        }

        eventRobot {
            //typeOnRequiredEventForm("test", 4)
            scrollToBottomForm()
            clickOnFormFabButton()
            clickOnFinish()
        }

        teiDashboardRobot {
            //check event was created
            //checkEventWasCreatedAndOpen("ANC 1st visit", 3)
            //checkEventWasScheduled("Baby Postnatal", 0)
            //checkEventWasCreatedAndOpen("Birth", 1)
        }

    }

    @Test
    @Ignore
    fun shouldSuccessfullyCreateRelationshipWhenClickAdd() {
        prepareTeiCompletedProgrammeAndLaunchActivity(rule)

        teiDashboardRobot {
            clickOnRelationshipTab()
        }

        relationshipRobot {
            clickOnFabAdd()
            clickOnRelationshipType()
            // click on a TEI
            // check relationship was created
            Thread.sleep(5000)
            //    clickOnMotherRelationship()
        }
    }

    @Ignore
    @Test
    fun shouldDeleteTeiSuccessfully() {
        val teiName = "Anthony"
        val teiLastName = "Banks"

        setupCredentials()
        prepareChildProgrammeIntentAndLaunchActivity(ruleSearch)

        searchTeiRobot {
            closeSearchForm()
            Thread.sleep(4000)
            clickOnTEI(teiName, teiLastName)
        }

        teiDashboardRobot {
            clickOnMenuMoreOptions()
            clickOnMenuDeleteTEI()
        }

        searchTeiRobot {
            Thread.sleep(4000)
            checkTEIsDelete(teiName, teiLastName)
        }
    }

    @Ignore
    @Test
    fun shouldDeleteEnrollmentSuccessfully() {

        val teiName = "Anna"
        val teiLastName = "Jones"

        setupCredentials()
        prepareChildProgrammeIntentAndLaunchActivity(ruleSearch)

        searchTeiRobot {
            closeSearchForm()
            Thread.sleep(4000)
            clickOnTEI(teiName, teiLastName)
        }

        teiDashboardRobot {
            clickOnMenuMoreOptions()
            clickOnMenuDeleteEnrollment()
        }

        searchTeiRobot {
            Thread.sleep(4000)
            checkTEIsDelete(teiName, teiLastName)
        }
    }

    private fun createExpectedUpperInformation() =
        UpperEnrollmentUIModel(
            "2021-01-10",
            "2021-01-10",
            "Ngelehun CHC"
        )

    private fun createExpectedEnrollmentInformation() =
        EnrollmentUIModel(
            "2021-01-10",
            "2021-01-10",
            "Ngelehun CHC",
            "40.48713205295354",
            "-3.6847423830882633",
            "Filona",
            "Ryder",
            "Female"
        )

    companion object {
        const val NOTE_VALID = "ThisIsJustATest"
        const val NOTE_INVALID = "InvalidNote"
        const val NOTE_EXISTING_TEXT = "This is a note test"
        const val USER = "android"

        const val LAB_MONITORING = "Lab monitoring"

        const val API_TEI_1_RESPONSE_OK = "mocks/teilist/teilist_1.json"
        const val API_TEI_2_RESPONSE_OK = "mocks/teilist/teilist_2.json"
        const val API_TEI_3_RESPONSE_OK = "mocks/teilist/teilist_3.json"
    }
}
