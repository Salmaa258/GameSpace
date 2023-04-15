package cat.copernic.gamespace.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.gamespace.Fragments.principal_administradorDirections
import cat.copernic.gamespace.data.dataAdmin
import cat.copernic.gamespace.databinding.ItemAdminBinding
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class AdminAdapter (private val AdminList:MutableList<dataAdmin>)  : RecyclerView.Adapter<AdminAdapter.gamesholder>(){

    inner class gamesholder(val binding: ItemAdminBinding): RecyclerView.ViewHolder(binding.root){

        //Implementació OnClick per eliminar un CardView del RecyclerView
        init {
            binding.imgDeleteGame.setOnClickListener {

                val position = adapterPosition

                val db = FirebaseFirestore.getInstance()
                val activity  = it.context as AppCompatActivity

                //Verifica si l'usuari vol eliminar el videojoc
                val builder = AlertDialog.Builder(activity)
                builder.setTitle("Eliminar")
                builder.setMessage("¿Estás seguro que quieres eliminar este videojuego?")
                builder.setPositiveButton("Sí") { _, _ ->
                    val selectedGame = AdminList[position]
                    val gameId = selectedGame.titulo
                    val imageURL = selectedGame.imagen

                    val elim = db.collection("Videojocs").document(gameId)
                    db.runBatch{ batch ->
                        batch.delete(elim)

                        // elimina la imagen del storage
                        val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(imageURL)
                        storageRef.delete().addOnSuccessListener {
                        }
                        AdminList.removeAt(position)
                        // notifica al adaptador del RecyclerView que se eliminó un elemento
                        notifyDataSetChanged()

                    }
                }
                builder.setNegativeButton("No") { _, _ ->
                }
                val alertDialog = builder.create()
                alertDialog.show()
                }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): gamesholder {
        val binding = ItemAdminBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return gamesholder(binding)
    }

    override fun onBindViewHolder(holder: gamesholder, position: Int) {
        with(holder) {
            with(AdminList[position]) {
                binding.txtGame.text = this.titulo
                Glide.with(binding.root)
                    .load(this.imagen)
                    .into(binding.imageGame)
            }
            binding.imgEditGame.setOnClickListener{ view ->
                navigation(view, AdminList[position].titulo)
            }
        }
    }

    private fun navigation(view: View, idDocumento: String ){
        val action = principal_administradorDirections.actionPrincipalAdministradorToAdminModificarVideojuego(idDocumento)
        view.findNavController().navigate(action)
    }

    override fun getItemCount(): Int = AdminList.size
}