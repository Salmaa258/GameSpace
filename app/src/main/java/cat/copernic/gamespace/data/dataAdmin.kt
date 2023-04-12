package cat.copernic.gamespace.data

import android.net.Uri
import com.google.firebase.storage.StorageReference

data class dataAdmin(
    var titulo: String = "",
    var descripcion: String = "",
    var genero: String = "",
    var imagen: String = ""
)