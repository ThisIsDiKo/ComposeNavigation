package com.example.composenavigation.feature_control.domain.model

import kotlin.experimental.inv

data class OutputsModel(
    val arraySize: Int,
){
    var outputs = Array(arraySize){false}

    fun setAllOutputs(state: Boolean){
        for (i in outputs.indices){
            outputs[i] = state
        }
    }

    fun setOutput(index: Int, state: Boolean){
        if (index >= 0 && index < outputs.size){
            outputs[index] = state
        }
    }

    fun convertOutputsToBytes(): ByteArray{
        var outputsByte: Int = 0

        for (i in outputs.indices){
            outputsByte = outputsByte or (outputs[i].toInt() shl i)
        }

        //Необходимое преобразование
        return byteArrayOf(
            outputsByte.toByte().inv(),
            (outputsByte shr 8).toByte().inv(),
            outputsByte.toByte(),
            (outputsByte shr 8).toByte()
        )
    }
}


fun Boolean.toInt() = if (this) 1 else 0