package com.example.customviewsproject

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.customviewsproject.component.data.ProgressData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    private var _liveData: MutableLiveData<ApiResult<ProgressData>> = MutableLiveData()
    val liveData: LiveData<ApiResult<ProgressData>> = _liveData

    fun loadData() {
        viewModelScope.launch {
            _liveData.postValue(ApiResult.Loading)
            delay(500)
            _liveData.postValue(ApiResult.Success(data))
        }
    }

    companion object {
        val data = ProgressData(
            targetItems = listOf(200, 300, 500, 1000),
            progress = 594.92f
        )
    }

}
