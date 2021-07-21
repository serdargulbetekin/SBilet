package com.example.sbilet.modules.main.bus

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sbilet.modules.journeys.LocationJourneyItem
import com.example.sbilet.util.QueryPreferencesHelper
import com.example.sbilet.util.SbiletDate
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@SuppressLint("CheckResult")
@HiltViewModel
class BusViewModel @Inject constructor(
    busRepository: BusRepository,
    private val queryPreferencesHelper: QueryPreferencesHelper,
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

    private val _shouldContinueTriple =
        MutableLiveData<Triple<Boolean, String, LocationJourneyItem?>>()
    val shouldContinueTriple: LiveData<Triple<Boolean, String, LocationJourneyItem?>>
        get() = _shouldContinueTriple


    private val _sBiletDepartureDate = MutableLiveData<Pair<SbiletDate, DateType>>()
    val sBiletDepartureDate: LiveData<Pair<SbiletDate, DateType>>
        get() = _sBiletDepartureDate

    private val _fromLocation = MutableLiveData<BusPlaneLocationItem?>()
    val fromLocation: LiveData<BusPlaneLocationItem?>
        get() = _fromLocation

    private val _toLocation = MutableLiveData<BusPlaneLocationItem?>()
    val toLocation: LiveData<BusPlaneLocationItem?>
        get() = _toLocation

    private val sBiletDateToday = SbiletDate.today()
    private val sBiletDateTomorrow = SbiletDate.tomorrow()

    init {
        checkIfQueryExist()
        _progress.postValue(true)
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

    fun setDate(sbiletDate: SbiletDate, dateType: DateType) {
        _sBiletDepartureDate.postValue(sbiletDate to dateType)
    }

    fun onFastDateClick(dateType: DateType) {
        when (dateType) {
            DateType.TODAY -> {
                setDate(sBiletDateToday, dateType)
            }
            DateType.TOMORROW -> {
                setDate(sBiletDateTomorrow, dateType)
            }
            DateType.NONE -> {
                setDate(sBiletDateTomorrow, dateType)
            }
        }
    }

    fun onClickFindTicket() {
        when {
            fromLocation.value == null -> {
                _shouldContinueTriple.postValue(
                    Triple(
                        false, "Başlangıç noktasını seçmediniz.", null
                    )
                )
            }
            toLocation.value == null -> {
                _shouldContinueTriple.postValue(
                    Triple(
                        false, "Varış noktasını seçmediniz.", null
                    )
                )
            }
            else -> {
                val locationJourneyItem = LocationJourneyItem(
                    fromLocation.value!!,
                    toLocation.value!!,
                    sBiletDepartureDate.value!!.first
                )
                _shouldContinueTriple.postValue(
                    Triple(
                        true,
                        "",
                        locationJourneyItem
                    )
                )
                saveBusQuery(locationJourneyItem)
            }
        }
    }

    private fun saveBusQuery(locationJourneyItem: LocationJourneyItem) {
        queryPreferencesHelper.setBusQuery(locationJourneyItem)
    }

    private fun getBusQuery() = queryPreferencesHelper.getBusQuery()

    private fun checkIfQueryExist() {
        val busQuery = getBusQuery()
        if (busQuery != null) {
            _fromLocation.postValue(busQuery.from)
            _toLocation.postValue(busQuery.to)
            _sBiletDepartureDate.postValue(busQuery.date to getDateType(busQuery.date))
        } else {
            _fromLocation.postValue(null)
            _toLocation.postValue(null)
            _sBiletDepartureDate.postValue(sBiletDateTomorrow to DateType.TOMORROW)
        }
    }

    private fun getDateType(sbiletDate: SbiletDate): DateType {
        return when {
            sbiletDate.isEqual(sBiletDateToday) -> {
                DateType.TODAY
            }
            sbiletDate.isEqual(sBiletDateTomorrow) -> {
                DateType.TOMORROW
            }
            else -> {
                DateType.NONE
            }
        }
    }

    companion object {
        var CACHED_DATA: List<BusPlaneLocationItem>? = null
        const val SP_BUS_QUERY = "SP_BUS_QUERY"

    }
}

