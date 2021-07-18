package com.example.sbilet.modules.splash

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sbilet.modules.splash.SplashRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("CheckResult")
@HiltViewModel
class SplashViewModel @Inject constructor(
    splashRepository: SplashRepository
) : ViewModel() {

    private val _progressErrorPair = MutableLiveData<Pair<Boolean,Boolean>>()
    val progressErrorPair: LiveData<Pair<Boolean,Boolean>>
        get() = _progressErrorPair

    init {
        _progressErrorPair.postValue(true to false)
        splashRepository.singleGetSession()
            .subscribe({
                _progressErrorPair.postValue(false to false)
            }, {
                _progressErrorPair.postValue(false to true)
            })
    }
}