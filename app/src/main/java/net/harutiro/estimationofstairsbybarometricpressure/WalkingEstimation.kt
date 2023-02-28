package net.harutiro.estimationofstairsbybarometricpressure

import android.icu.text.SimpleDateFormat
import android.util.Log
import androidx.core.app.RemoteInput.EditChoicesBeforeSending
import java.util.*
import kotlin.math.abs

class WalkingEstimation {

    var beforeData = SensorModel(1010.0, Date(System.currentTimeMillis()))

    var beforeAmount = 0.0

    fun noiseRemoved(sensingData:Double , beforeSensingData: Double):Double{
        return beforeSensingData * 0.99 + sensingData * 0.01
    }

    fun noiseRemoved2(sensingData:Double , beforeSensingData: Double):Double{
        return beforeSensingData * 0.6 + sensingData * 0.4
    }

    fun amountOfChangeCalculation(sensorData:SensorModel):Double{
        return (sensorData.sensorData - beforeData.sensorData) / (sensorData.time.time - beforeData.time.time)
    }

    fun comeData(sensingData:Double):Double{

//        Log.d("now",(Date(System.currentTimeMillis()).time - getTime.time).toString())


        //サンプリング周波数の調整
        //0.5秒に一回取得するように変更
//        if(Date(System.currentTimeMillis()).time - getTime.time < 100){
//            return "sampling"
//        }

        //現在の情報の取得
        val nowSensorModel = SensorModel(sensingData,Date(System.currentTimeMillis()))

        //ローパスフィルター
        nowSensorModel.sensorData = noiseRemoved(sensingData = nowSensorModel.sensorData , beforeSensingData = beforeData.sensorData )
        Log.d("now",nowSensorModel.toString())

        //変化量の計算
        var nowAmount = amountOfChangeCalculation(nowSensorModel)
        nowAmount = noiseRemoved2(sensingData = nowAmount, beforeSensingData = beforeAmount)

        beforeData = nowSensorModel
        beforeAmount = nowAmount

        return nowAmount
    }
}