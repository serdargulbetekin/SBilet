package com.example.sbilet.modules.main.plane


import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sbilet.modules.journeys.LocationJourneyItem
import com.example.sbilet.modules.main.bus.BusPlaneLocationItem
import com.example.sbilet.modules.main.bus.BusRepository
import com.example.sbilet.modules.main.bus.DateType
import com.example.sbilet.util.SbiletDate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("CheckResult")
@HiltViewModel
class PlaneViewModel @Inject constructor(
    busRepository: BusRepository
) : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _locationItem = MutableLiveData<List<BusPlaneLocationItem>>()
    val locationItem: LiveData<List<BusPlaneLocationItem>>
        get() = _locationItem

    private val _fromLocation = MutableLiveData<BusPlaneLocationItem?>()
    val fromLocation: LiveData<BusPlaneLocationItem?>
        get() = _fromLocation

    private val _toLocation = MutableLiveData<BusPlaneLocationItem?>()
    val toLocation: LiveData<BusPlaneLocationItem?>
        get() = _toLocation


    private val _sBiletDepartureDate = MutableLiveData<SbiletDate>()
    val sBiletDepartureDate: LiveData<SbiletDate>
        get() = _sBiletDepartureDate

    private val _shouldContinueTriple =
        MutableLiveData<Triple<Boolean, String, LocationJourneyItem?>>()
    val shouldContinueTriple: LiveData<Triple<Boolean, String, LocationJourneyItem?>>
        get() = _shouldContinueTriple

    private val sBiletDateTomorrow = SbiletDate.tomorrow()


    init {
        _progress.postValue(true)
        _fromLocation.postValue(null)
        _toLocation.postValue(null)
        _sBiletDepartureDate.postValue(sBiletDateTomorrow)
        busRepository.getLocations()
            .subscribe({
                _progress.postValue(false)
                _locationItem.postValue(it)
            }, {
                _progress.postValue(false)
            })
    }

    fun onSelectFrom(fromLocation: BusPlaneLocationItem) {
        if (this.toLocation.value?.id == fromLocation.id) {
            _errorMessage.postValue("Başlangıç ve varış noktaları aynı olamaz")
        } else {
            _fromLocation.postValue(fromLocation)
        }
    }

    fun onSelectTo(toLocation: BusPlaneLocationItem) {
        if (this.fromLocation.value?.id == toLocation.id) {
            _errorMessage.postValue("Başlangıç ve varış noktaları aynı olamaz")
        } else {
            _toLocation.postValue(toLocation)
        }
    }

    fun onClickFindTicket() {
        when {
            fromLocation == null -> {
                _shouldContinueTriple.postValue(
                    Triple(
                        false, "Başlangıç noktasını seçmediniz.", null
                    )
                )
            }
            toLocation == null -> {
                _shouldContinueTriple.postValue(
                    Triple(
                        false, "Varış noktasını seçmediniz.", null
                    )
                )
            }
            else -> {
                _shouldContinueTriple.postValue(
                    Triple(
                        false, "What to do....",
                        null
                    )
                )
            }
        }
    }

    companion object {
        const val SP_PLANE_QUERY = "SP_PLANE_QUERY"
    }
}

