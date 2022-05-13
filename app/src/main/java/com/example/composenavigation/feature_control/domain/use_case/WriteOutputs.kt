package com.example.composenavigation.feature_control.domain.use_case

import com.example.composenavigation.feature_control.domain.model.OutputsModel
import com.example.composenavigation.feature_control.domain.repository.DKController

class WriteOutputs(
    private val dkController: DKController
) {
    suspend operator fun invoke(outputsModel: OutputsModel): String {
        return if (dkController.getCurrentPeripheral() != null){
            dkController.writeOutputs(outputsModel)
                .fold(
                    {
                        "Got error while writing outputs"
                    },
                    {
                        "Writing successfull"
                    }
                )
        }
        else {
            "Peripheral is null"
        }
    }
}