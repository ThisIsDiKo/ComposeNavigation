package com.example.composenavigation.feature_control.domain.use_case

import com.example.composenavigation.feature_control.domain.repository.DKController

class ConnectToPeripheral(
    private val dkController: DKController
) {
    suspend operator fun invoke(mac: String): String{
        val peripheral = dkController.createPeripheral(mac)
        return if (peripheral != null){
            dkController.connect(peripheral)
                .fold(
                    {
                        "can't connect to peripheral"
                    },
                    {
                        "connected"
                    }
                )
        } else{
            "Can't connect to peripheral, peripheral is not created"
        }
    }
}