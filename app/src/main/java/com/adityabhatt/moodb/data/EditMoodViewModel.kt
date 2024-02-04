package com.adityabhatt.moodb.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditMoodViewModel: ViewModel() {
    private val _causeLists = MutableLiveData(
        listOf<String>()
    )
    val causeLists: LiveData<List<String>> get() = _causeLists

    sealed class DialogState (
        open val causeString: String = ""
    ) {
        data class ADD(override val causeString: String = ""): DialogState()
        data class EDIT(val index: Int, override val causeString: String): DialogState()
        data class DELETE(val index: Int) : DialogState()
    }

    fun updateList(dialogState: DialogState) {
        when (dialogState) {
            is DialogState.ADD -> {
                val newList = _causeLists.value!!.toMutableList()
                newList.add(dialogState.causeString)
                _causeLists.value = newList
            }
            is DialogState.EDIT -> {
                val newList = _causeLists.value!!.toMutableList()
                newList[dialogState.index] = dialogState.causeString
                _causeLists.value = newList
            }
            is DialogState.DELETE -> {
                val newList = _causeLists.value!!.toMutableList()
                newList.removeAt(dialogState.index)
                _causeLists.value = newList
            }
        }
    }
}