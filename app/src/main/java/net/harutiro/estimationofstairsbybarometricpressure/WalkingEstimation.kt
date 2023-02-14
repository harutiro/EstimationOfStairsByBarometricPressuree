package net.harutiro.estimationofstairsbybarometricpressure

import android.icu.text.SimpleDateFormat
import android.util.Log
import java.util.*

class WalkingEstimation {

    var beforeData = SensorModel(0.0, Date(System.currentTimeMillis()))

    var getTime:Date = Date(System.currentTimeMillis())



    fun comeData(sensingData:Double):String{

        Log.d("now",(Date(System.currentTimeMillis()).time - getTime.time).toString())


        //サンプリング周波数の調整
        //0.5秒に一回取得するように変更
        if(Date(System.currentTimeMillis()).time - getTime.time < 500){
            return "sampling"
        }
        getTime = Date(System.currentTimeMillis())

        Log.d("now",sensingData.toString())


        //気圧センサーが前回より0.05の大きさなら
        if(Math.abs(sensingData - beforeData.sensorData) > 0.05){
            val diff = Date(System.currentTimeMillis()).time - beforeData.time.time


            Log.d("WolkingEstimation", "nowTime: " + Date(System.currentTimeMillis()))
            Log.d("WolkingEstimation", "diff: $diff")

            //現在の状態を保存
            beforeData = SensorModel(sensingData, Date(System.currentTimeMillis()))

            //もし3以上状態が続いていたら、止まっていると判断
            return if(diff <= 3000){ "上昇もしくは下降" } else { "動いていない" }

        }

        //何も判定されなかったら動いていない。
        return "動いていない"
    }
}