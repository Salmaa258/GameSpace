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

        setContentView(R.layout.activity_splashart)
        timer.schedule(1200) {
            var intent = Intent(this@splashart, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}