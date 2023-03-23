package cat.copernic.gamespace.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import cat.copernic.gamespace.R
import cat.copernic.gamespace.databinding.FragmentMostrarVideojuegoBinding
import cat.copernic.gamespace.model.videojuegos
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [mostrar_videojuego.newInstance] factory method to
 * create an instance of this fragment.
 */
class mostrar_videojuego : Fragment() {
    private lateinit var binding: FragmentMostrarVideojuegoBinding
    private val args: mostrar_videojuegoArgs by navArgs()
    private lateinit var auth: FirebaseAuth
    private val bd = FirebaseFirestore.getInstance()

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        auth= Firebase.auth

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //obtenim l'usuari que está autenticat actualment
        val user = auth.currentUser

        //pas de parámetres, Safe Args
        //pasem el titol i l'imatge
        val title = args.titulo
        val imageResId = args.imgJuego
        binding.txtNombreVideojuegoMostrar.text = title
        binding.imgVideojuegoMostrar.setImageResource(imageResId)

        //establim els camps dels atributs del objecte "videojuego"
        val juego = videojuegos(binding.txtNombreVideojuegoMostrar.text.toString(), binding.txtDescripcionVideojuegoMostrar.text.toString(), binding.txtGenerosMostrar.text.toString())

        //Controlem el clic a l'imatge per afegir el joc al llistat d'obtinguts
        var imgAfegir = true
        var imgPrinciapalAfegir = binding.imgAnadir

        // Agrega un listener de clics a la ImageView
        imgPrinciapalAfegir.setOnClickListener {
            // Cambia la imagen de la ImageView según el valor de la variable de estado
            if (imgAfegir) {
                imgPrinciapalAfegir.setImageResource(R.drawable.tick)

                //Creem subcolecció "Juegos obtenidos" i li afegim la variable que conté els camps "juego"
                bd.collection("Usuaris").document(user?.email.toString()).collection("Juegos obtenidos").document(binding.txtNombreVideojuegoMostrar.text.toString()).set(juego)
                .addOnSuccessListener {  }.addOnFailureListener {  }

                //Creem un snackbar per avisar a l'usuari que s'ha afegit a la colecció
                Snackbar.make(it,"Se ha añadido a tu colección", Snackbar.LENGTH_LONG).show()
                imgAfegir = false
            } else {
                imgPrinciapalAfegir.setImageResource(R.drawable.mas)
                //Creem un snackbar per avisar a l'usuari que s'ha tret de la colecció
                Snackbar.make(it,"Se ha quitado de tu colección", Snackbar.LENGTH_LONG).show()
                imgAfegir = true
            }
        }

        //Controlem el clic a l'imatge per afegir el joc al llistat de desitjats
        var imgMarcador = true
        var imgMarcadorAfegir = binding.imgMarcadorAnadir

        // Agrega un listener de clics a la ImageView
        imgMarcadorAfegir.setOnClickListener {
            // Cambia la imagen de la ImageView según el valor de la variable de estado
            if (imgMarcador) {
                imgMarcadorAfegir.setImageResource(R.drawable.marcador_anadido)

                //Creem subcolecció "Juegos deseados" i li afegim la variable que conté els camps "juego"
                bd.collection("Usuaris").document(user?.email.toString()).collection("Juegos deseados").document(binding.txtNombreVideojuegoMostrar.text.toString()).set(juego)
                    .addOnSuccessListener {  }.addOnFailureListener {  }

                //Creem un snackbar per avisar a l'usuari que s'ha afegit al llistat de desitjats
                Snackbar.make(it,"Se ha añadido a tus deseados", Snackbar.LENGTH_LONG).show()

                imgMarcador = false
            } else {
                imgMarcadorAfegir.setImageResource(R.drawable.marcador_anadir)
                //Creem un snackbar per avisar a l'usuari que s'ha tret de la llista de desitjats
                Snackbar.make(it,"Se ha quitado de tus deseados",Snackbar.LENGTH_LONG).show()
                imgMarcador = true
            }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMostrarVideojuegoBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment mostrar_videojuego.
         */

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            mostrar_videojuego().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}