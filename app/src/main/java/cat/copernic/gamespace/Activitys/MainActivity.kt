package cat.copernic.gamespace.Activitys

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.copernic.gamespace.R
import cat.copernic.gamespace.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}