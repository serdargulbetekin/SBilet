package com.example.sbilet.modules.main.bus

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sbilet.util.SbiletDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.parcel.Parcelize
import javax.inject.Inject

@SuppressLint("CheckResult")
@HiltViewModel
class BusViewModel @Inject constructor(
    busRepository: BusRepository
) : ViewModel() {

    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean>
        get() = _progress

    private val _locationItem = MutableLiveData<List<BusPlaneLocationItem>>()
    val locationItem: LiveData<List<BusPlaneLocationItem>>
        get() = _locationItem

    private val _shouldContinueTriple =
        MutableLiveData<Triple<Boolean, String, LocationJourneyItem?>>()
    val shouldContinueTriple: LiveData<Triple<Boolean, String, LocationJourneyItem?>>
        get() = _shouldContinueTriple


    private val _sBiletDepartureDate = MutableLiveData<SbiletDate>()
    val sBiletDepartureDate: LiveData<SbiletDate>
        get() = _sBiletDepartureDate

    private var fromLocation: BusPlaneLocationItem? = null
    private var toLocation: BusPlaneLocationItem? = null

    private val sBiletDateToday = SbiletDate.today()
    private val sBiletDateTomorrow = SbiletDate.tomorrow()

    init {
        _progress.postValue(true)
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
        this.fromLocation = fromLocation
    }

    fun onSelectTo(toLocation: BusPlaneLocationItem) {
        this.toLocation = toLocation
    }

    fun setDate(sbiletDate: SbiletDate) {
        _sBiletDepartureDate.postValue(sbiletDate)
    }

    fun onFastDateClick(dateType: DateType) {
        when (dateType) {
            DateType.TODAY -> {
                setDate(sBiletDateToday)
            }
            DateType.TOMORROW -> {
                setDate(sBiletDateTomorrow)
            }
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
                        true, "",
                        LocationJourneyItem(
                            fromLocation!!,
                            toLocation!!,
                            sBiletDepartureDate.value!!
                        )
                    )
                )
            }
        }
    }


    companion object {
        var CACHED_DATA: List<BusPlaneLocationItem>? = null
    }
}

@Parcelize
data class LocationJourneyItem(
    val from: BusPlaneLocationItem,
    val to: BusPlaneLocationItem,
    val date: SbiletDate
) : Parcelable