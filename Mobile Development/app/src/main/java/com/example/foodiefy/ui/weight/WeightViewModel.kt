package com.example.foodiefy.ui.weight

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeightViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is AI Fragment"
    }
    val text: LiveData<String> = _text
}