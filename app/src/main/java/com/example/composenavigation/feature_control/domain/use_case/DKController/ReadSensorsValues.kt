package com.example.composenavigation.feature_control.domain.use_case.DKController

import com.example.composenavigation.feature_control.data.BleCommunicationError
import com.example.composenavigation.feature_control.domain.repository.DKController

class ReadSensorsValues(
    private val dkController: DKController
) {
    suspend operator fun invoke(): String {
        return dkController.readSensors()
            .fold(
                {error ->
                    when(error){
                        is BleCommunicationError.InvalidSensorsDataPacketError ->{
                            "Got invalid packet ${error.msg}"
                        }
                        is BleCommunicationError.UnknownError -> {
                            "Got unknown error ${error.msg}"
                        }
                    }

                },
                {sensorModel ->
                    "${sensorModel.pressure1} ${sensorModel.pressure2} ${sensorModel.pressure3}"
                }
            )
    }
}