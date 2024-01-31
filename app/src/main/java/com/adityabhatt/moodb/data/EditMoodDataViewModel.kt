package com.adityabhatt.moodb.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditMoodDataViewModel: ViewModel() {
    private val _causeLists = MutableLiveData(
        listOf(
            "Great night sleep",
            "Exercised first thing",
            "Worked on most important task"
        )
    )
    val causeLists: LiveData<List<String>> get() = _causeLists
    fun updateList(stringToAdd: String) {
        val newList = _causeLists.value!!.toMutableList()
        newList.add(stringToAdd)
        _causeLists.value = newList
    }
}