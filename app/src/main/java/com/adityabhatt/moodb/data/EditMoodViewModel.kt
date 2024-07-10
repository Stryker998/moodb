package com.adityabhatt.moodb.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.adityabhatt.moodb.util.now
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

sealed class StateOfScreen {
    data object AddItem: StateOfScreen()
    data class EditItem(val moodData: MoodData): StateOfScreen()
}
class EditMoodViewModel(private val databaseViewModel: MoodDatabaseViewModel, private val stateOfScreen: StateOfScreen): ViewModel() {
    private var _currentDate: MutableLiveData<LocalDate>
    private var _causeList: MutableLiveData<List<String>>
    private var _mood: MutableLiveData<Mood>
    private var _id: MutableLiveData<Int>

    init {
        when (stateOfScreen) {
            StateOfScreen.AddItem -> {
                _currentDate = MutableLiveData(LocalDate.now())
                _causeList = MutableLiveData(listOf())
                _mood = MutableLiveData(Mood.GOOD_MOOD)
                _id = MutableLiveData(0)
            }
            is StateOfScreen.EditItem -> {
                _currentDate = MutableLiveData(stateOfScreen.moodData.date)
                _causeList = MutableLiveData(stateOfScreen.moodData.causeList)
                _mood = MutableLiveData(stateOfScreen.moodData.mood)
                _id = MutableLiveData(stateOfScreen.moodData.id)
            }
        }
    }

    val causeList: LiveData<List<String>> get() = _causeList
    val currentDate: LiveData<LocalDate> get() = _currentDate
    val mood: LiveData<Mood> get() = _mood
    private val id: LiveData<Int> get() = _id

    sealed class DialogState (
        open val causeString: String = ""
    ) {
        data class ADD(override val causeString: String = ""): DialogState()
        data class EDIT(val index: Int, override val causeString: String): DialogState()
        data class DELETE(val index: Int) : DialogState()
    }

    fun changeMood() {
        if (_mood.value == Mood.GOOD_MOOD) {
            _mood.value = Mood.BAD_MOOD
        } else {
            _mood.value = Mood.GOOD_MOOD
        }
    }

    fun updateDate(epoch: Long) {
        _currentDate.value = Instant.fromEpochMilliseconds(epoch).toLocalDateTime(TimeZone.currentSystemDefault()).date
    }


    fun updateList(dialogState: DialogState) {
        when (dialogState) {
            is DialogState.ADD -> {
                val newList = _causeList.value!!.toMutableList()
                newList.add(dialogState.causeString)
                _causeList.value = newList
            }
            is DialogState.EDIT -> {
                val newList = _causeList.value!!.toMutableList()
                newList[dialogState.index] = dialogState.causeString
                _causeList.value = newList
            }
            is DialogState.DELETE -> {
                val newList = _causeList.value!!.toMutableList()
                newList.removeAt(dialogState.index)
                _causeList.value = newList
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (shouldDelete) {
            val moodData = MoodData(id.value!!, currentDate.value!!, causeList.value!!, mood.value!!)
            databaseViewModel.deleteMood(moodData)
            shouldDelete = false
        } else if (shouldSave) {
            val moodData = MoodData(id.value!!, currentDate.value!!, causeList.value!!, mood.value!!)

            when(stateOfScreen) {
                StateOfScreen.AddItem -> {
                    databaseViewModel.addMood(
                        moodData
                    )
                }
                is StateOfScreen.EditItem -> {
                    databaseViewModel.updateMood(
                        moodData
                    )
                }
            }
            shouldSave = false
        }
    }

    companion object {
        var shouldSave = false
        var shouldDelete = false
        fun Factory(databaseViewModel: MoodDatabaseViewModel, stateOfScreen: StateOfScreen): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer {
                    EditMoodViewModel(
                        databaseViewModel, stateOfScreen
                    )
                }
            }
        }
    }
}