package com.example.sbilet.modules.journeys

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.sbilet.modules.journeys.JourneyActivity.Companion.EXTRAS_LOCATION_JOURNEY
import com.example.sbilet.modules.main.bus.LocationJourneyItem
import dagger.BindsInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("CheckResult")
@HiltViewModel
class JourneyViewModel @Inject constructor(
    journeyRepository: JourneyRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _locationJourneyItem =
        savedStateHandle.getLiveData<LocationJourneyItem>(EXTRAS_LOCATION_JOURNEY)

    val locationJourneyItem: LiveData<LocationJourneyItem>
        get() = _locationJourneyItem

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _journeyList = MutableLiveData<List<JourneyItem>>()
    val journeyList: LiveData<List<JourneyItem>>
        get() = _journeyList

    init {
        _progress.postValue(true)
        _locationJourneyItem.value?.let {
            journeyRepository.singleJourneyList(
                from = it.from,
                to = it.to,
                date = it.date
            ).subscribe({
                _progress.postValue(false)
                _journeyList.postValue(it)
            }, {
                _progress.postValue(false)
                _errorMessage.postValue(it.message ?: "Bir hata oluştu lütfen tekrar deneyiniz...")
            })
        } ?: _errorMessage.postValue("Bir hata oluştu lütfen tekrar deneyiniz...")
    }

}