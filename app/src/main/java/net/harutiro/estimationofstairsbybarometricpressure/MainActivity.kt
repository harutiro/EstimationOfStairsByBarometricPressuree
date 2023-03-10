package net.harutiro.estimationofstairsbybarometricpressure

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.math.BigDecimal
import kotlin.math.floor

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var AccSensor: Sensor? = null

    val WalkingEstimation = WalkingEstimation()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        AccSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)
    }

    //センサーに何かしらのイベントが発生したときに呼ばれる
    override fun onSensorChanged(event: SensorEvent) {
        var sensorX: Float

        Log.d("Main",event.toString())
        // Remove the gravity contribution with the high-pass filter.
        if (event.sensor.type == Sensor.TYPE_PRESSURE) {
            sensorX = event.values[0]

            findViewById<TextView>(R.id.textView).text = WalkingEstimation.noiseRemoved(sensorX.toDouble(),WalkingEstimation.beforeData.sensorData).toString()

            val ans = WalkingEstimation.comeData(sensorX.toDouble())

            Log.d("main", ans.toString())

            findViewById<TextView>(R.id.textView2).text = BigDecimal.valueOf(floor(ans * 1000000) / 1000000).toPlainString()

            findViewById<TextView>(R.id.textView3).text = if(ans > -0.00002){
                "登ってない"
            }else{
                "登ってる"
            }

        }
    }
    //センサの精度が変更されたときに呼ばれる
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
    }

    override fun onResume() {
        super.onResume()
        //リスナーとセンサーオブジェクトを渡す
        //第一引数はインターフェースを継承したクラス、今回はthis
        //第二引数は取得したセンサーオブジェクト
        //第三引数は更新頻度 UIはUI表示向き、FASTはできるだけ早く、GAMEはゲーム向き
        sensorManager.registerListener(this, AccSensor, SensorManager.SENSOR_DELAY_UI)
    }

    //アクティビティが閉じられたときにリスナーを解除する
    override fun onPause() {
        super.onPause()
        //リスナーを解除しないとバックグラウンドにいるとき常にコールバックされ続ける
        sensorManager.unregisterListener(this)
    }
}