package com.example.composenavigation.feature_control.presenter.control

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composenavigation.feature_control.data.DataStoreManager
import com.example.composenavigation.feature_control.domain.use_case.DKControllerUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ControlScreenViewModel @Inject constructor(
    private val dkControllerUseCases: DKControllerUseCases,
    private val dataStoreManager: DataStoreManager
): ViewModel() {

    private var readingJob: Job? = null

    private val _controllerState = mutableStateOf(SuspensionControllerState())
    val controllerState: State<SuspensionControllerState> = _controllerState

    fun startReadingSensors(){
        readingJob = viewModelScope.launch {
            try{
                while(true){
                    dkControllerUseCases.readSensorsValues()
                    delay(200)
                }
            }
            catch(e: Exception){
                Timber.e("ERROR: Got exception in reading: ${e.toString()}")
            }
            finally {
                Timber.i("finally job canceled")
                readingJob = null
            }
        }
    }

    fun stopReadingSensors(){
        if (readingJob != null){
            Timber.i("Cancelling Reading Job")
            readingJob?.cancel()
            readingJob = null
        }
    }
}