package cat.copernic.gamespace.Fragments

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import cat.copernic.gamespace.R
import cat.copernic.gamespace.databinding.FragmentMostrarVideojuegoBinding
import cat.copernic.gamespace.model.videojuegos
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


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

    //Juegos obtenidos
    private  val PREFS_NAME = "VideojuegosFavsObtenidos"
    private  val PREF_IMG_STATE = "imagen_estado_obtenido"
    //Juegos deseados
    private  val PREFS_NAME_MAR = "VideojuegosFavsDeseados"
    private  val PREF_IMG_STATE_MAR = "imagen_estado_deseado"

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

        lifecycleScope.launch{
            withContext(Dispatchers.IO){
                ponerJuego()
            }
        }

        //obtenim l'usuari que está autenticat actualment
        val user = auth.currentUser

        //pas de parámetres, Safe Args
        //pasem el titol i l'imatge
        val title = args.titulo
        binding.txtNombreVideojuegoMostrar.text = title

        var imatge = args.imgJuego
        // Cargar imagen con Glide
        Glide.with(this)
            .load(imatge)
            .into(binding.imgVideojuegoMostrar)


        // Obtenemos una instancia de SharedPreferences
        val sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Obtenemos el estado de la imagen de las SharedPreferences
        val imgState = sharedPreferences.getBoolean(PREF_IMG_STATE + title, false)

        // Establece la imagen según el estado de la imagen añadir
        val imgResIdAnadir = if (imgState) R.drawable.tick else R.drawable.mas
        binding.imgAnadir.setImageResource(imgResIdAnadir)


            //establim els camps dels atributs del objecte "videojuego"
            val juego = videojuegos(binding.txtNombreVideojuegoMostrar.text.toString(), binding.txtDescripcionVideojuegoMostrar.text.toString(), binding.txtGenerosMostrar.text.toString())

            //Controlem el clic a l'imatge per afegir el joc al llistat d'obtinguts
            var imgAfegir = false
            var imgPrinciapalAfegir = binding.imgAnadir

            // Agrega un listener de clics a la ImageView de l'imatge afegir
            imgPrinciapalAfegir.setOnClickListener {
                // Cambia la imagen de la ImageView según el valor de la variable de estado
                if (!imgAfegir) {
                    imgPrinciapalAfegir.setImageResource(R.drawable.tick)

                    // Guarda el estado de la imagen en las SharedPreferences
                    sharedPreferences.edit().putBoolean(PREF_IMG_STATE + title, true).apply()

                    //Creem subcolecció "Juegos obtenidos" i li afegim la variable que conté els camps "juego"
                    bd.collection("Usuaris").document(user?.email.toString()).collection("Juegos obtenidos").document(binding.txtNombreVideojuegoMostrar.text.toString()).set(juego)
                    .addOnSuccessListener {

                    }.addOnFailureListener {  }


                    //Creem un snackbar per avisar a l'usuari que s'ha afegit a la colecció
                    Snackbar.make(it,"Se ha añadido a tu colección", Snackbar.LENGTH_LONG).show()
                    imgAfegir = true
                } else {
                    imgPrinciapalAfegir.setImageResource(R.drawable.mas)

                    // Guarda el estado de la imagen en las SharedPreferences
                    sharedPreferences.edit().putBoolean(PREF_IMG_STATE + title, false).apply()


                    //Eliminar el documento correspondiente a ese videojuego de la colección "Juegos obtenidos" en la base de datos
                    bd.collection("Usuaris").document(user?.email.toString()).collection("Juegos obtenidos").document(binding.txtNombreVideojuegoMostrar.text.toString()).delete()
                        .addOnSuccessListener {
                            // Aquí se podría agregar código adicional en caso de que la operación sea exitosa
                        }.addOnFailureListener {
                            // Aquí se podría agregar código adicional en caso de que la operación falle
                        }

                    //Creem un snackbar per avisar a l'usuari que s'ha tret de la colecció
                    Snackbar.make(it,"Se ha quitado de tu colección", Snackbar.LENGTH_LONG).show()
                    imgAfegir = false
                }
            }

        // Obtenemos una instancia de SharedPreferences
        val sharedPreferencesMar = requireContext().getSharedPreferences(PREFS_NAME_MAR, Context.MODE_PRIVATE)

        // Obtenemos el estado de la imagen de las SharedPreferences
        val imgStateMar = sharedPreferencesMar.getBoolean(PREF_IMG_STATE_MAR + title, false)

        // Establece la imagen según el estado de la imagen marcador
        val imgResIdMarcador = if (imgStateMar) R.drawable.marcador_anadido else R.drawable.marcador_anadir
        binding.imgMarcadorAnadir.setImageResource(imgResIdMarcador)

        //Controlem el clic a l'imatge per afegir el joc al llistat de desitjats
        var imgMarcador = false
        var imgMarcadorAfegir = binding.imgMarcadorAnadir

        // Agrega un listener de clics a la ImageView
        imgMarcadorAfegir.setOnClickListener {
            // Cambia la imagen de la ImageView según el valor de la variable de estado
            if (!imgMarcador) {
                imgMarcadorAfegir.setImageResource(R.drawable.marcador_anadido)

                // Guarda el estado de la imagen en las SharedPreferences
                sharedPreferencesMar.edit().putBoolean(PREF_IMG_STATE_MAR + title, true).apply()

                //Creem subcolecció "Juegos deseados" i li afegim la variable que conté els camps "juego"
                bd.collection("Usuaris").document(user?.email.toString()).collection("Juegos deseados").document(binding.txtNombreVideojuegoMostrar.text.toString()).set(juego)
                    .addOnSuccessListener {  }.addOnFailureListener {  }

                //Creem un snackbar per avisar a l'usuari que s'ha afegit al llistat de desitjats
                Snackbar.make(it,"Se ha añadido a tus deseados", Snackbar.LENGTH_LONG).show()
                imgMarcador = true
            } else {
                imgMarcadorAfegir.setImageResource(R.drawable.marcador_anadir)

                // Guarda el estado de la imagen en las SharedPreferences
                sharedPreferencesMar.edit().putBoolean(PREF_IMG_STATE_MAR + title, false).apply()

                //Eliminar el documento correspondiente a ese videojuego de la colección "Juegos obtenidos" en la base de datos
                bd.collection("Usuaris").document(user?.email.toString()).collection("Juegos deseados").document(binding.txtNombreVideojuegoMostrar.text.toString()).delete()
                    .addOnSuccessListener {
                        // Aquí se podría agregar código adicional en caso de que la operación sea exitosa
                    }.addOnFailureListener {
                        // Aquí se podría agregar código adicional en caso de que la operación falle
                    }

                //Creem un snackbar per avisar a l'usuari que s'ha tret de la llista de desitjats
                Snackbar.make(it,"Se ha quitado de tus deseados",Snackbar.LENGTH_LONG).show()
                imgMarcador = false
            }
        }

    }

    //Llegim i posem les dades de la base de dades del videojoc
    fun ponerJuego() {

        //Leer datos
        bd.collection("Videojocs").document(args.titulo).get().addOnSuccessListener { documentSnapshot ->
            binding.txtDescripcionVideojuegoMostrar.setText(documentSnapshot.get("descripción").toString())
            binding.txtTipoGenero.setText(documentSnapshot.get("género").toString())


            //imatge
            val text = args.imgJuego
            val storageRef = FirebaseStorage.getInstance().reference.child("imatges/videojocs/$text" + ".jpeg")
            val localfile = File.createTempFile("tempImage", "jpeg")
            storageRef.getFile(localfile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                binding.imgVideojuegoMostrar.setImageBitmap(bitmap)
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