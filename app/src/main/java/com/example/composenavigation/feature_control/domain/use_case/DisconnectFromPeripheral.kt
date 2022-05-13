package com.example.composenavigation.feature_control.domain.use_case

import com.example.composenavigation.feature_control.domain.repository.DKController

class DisconnectFromPeripheral(
    private val dkController: DKController
) {
    suspend operator fun invoke(): String {
        val currentPeripheral = dkController.getCurrentPeripheral()
        if (currentPeripheral != null) {
            return dkController.disconnect(currentPeripheral)
                .fold(
                    {
                        "error during disconnection"
                    },
                    {
                        "disconnection complete"
                    }
                )
        }
        return "Cant disconnect from no peripheral"
    }
}