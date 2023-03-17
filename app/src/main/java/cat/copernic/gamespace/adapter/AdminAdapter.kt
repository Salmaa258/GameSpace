package cat.copernic.gamespace.adapter

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import cat.copernic.gamespace.R
import cat.copernic.gamespace.Utils.Utils
import cat.copernic.gamespace.data.dataAdmin
import cat.copernic.gamespace.databinding.ItemAdminBinding


class AdminAdapter (private val AdminList:MutableList<dataAdmin>)  : RecyclerView.Adapter<AdminAdapter.gamesholder>(){

    inner class gamesholder(val binding: ItemAdminBinding): RecyclerView.ViewHolder(binding.root){

        //Implementació OnClick per eliminar un CardView del RecyclerView
        init {
            binding.imgDeleteGame.setOnClickListener {
                // obtiene la posición del CardView en la lista
                val position = adapterPosition
                //llamamos al método en Utils para verificar con un alertDialog
                Utils.verificaElimina(itemView.context) {
                    // elimina los datos de la lista
                    AdminList.removeAt(position)
                    // notifica al adaptador del RecyclerView que se eliminó un elemento
                    notifyItemRemoved(position)
                }
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
                binding.txtGame.text = this.txt_game
                binding.imageGame.setImageResource(this.img_game)
            }
            binding.imgEditGame.setOnClickListener{ view ->
                view.findNavController().navigate(R.id.admin_modificar_videojuego)

            }
        }
    }

    override fun getItemCount(): Int = AdminList.size
}