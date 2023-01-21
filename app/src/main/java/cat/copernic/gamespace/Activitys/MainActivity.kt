package cat.copernic.gamespace.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.gamespace.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_mostrar_videojuego)
    }
}