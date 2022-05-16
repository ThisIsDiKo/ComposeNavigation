package com.example.composenavigation.feature_control.domain.use_case.DKController

import arrow.core.Either
import arrow.core.left
import com.example.composenavigation.feature_control.data.BleCommunicationError
import com.example.composenavigation.feature_control.domain.repository.DKController

class ConnectToPeripheral(
    private val dkController: DKController
) {
    suspend operator fun invoke(mac: String): Either<BleCommunicationError, Unit>{
        val peripheral = dkController.createPeripheral(mac)
        return if (peripheral != null) {
            dkController.connect(peripheral)
        }
        else {
            BleCommunicationError.UnknownError("Peripheral is null").left()
        }
    }
}