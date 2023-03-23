package cat.copernic.gamespace.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.gamespace.R
import java.util.*
import kotlin.concurrent.schedule

class splashart : AppCompatActivity() {
    var timer = Timer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Esconder AppBar
        if(supportActionBar != null)
            supportActionBar!!.hide()

        //XML a mostrar
        setContentView(R.layout.activity_splashart)

        //Tiempo que se mostrará la pantalla
        timer.schedule(1200) {
            //Activity que se mostrará finalizado el tiempo
            var intent = Intent(this@splashart, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}