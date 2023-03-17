package cat.copernic.gamespace.dataLists

import cat.copernic.gamespace.R
import cat.copernic.gamespace.data.dataPantallaPrincipal
import java.util.ArrayList

class PantallaPrincipalList {
    companion object{
        val PantallaPrincipalList = listOf<dataPantallaPrincipal>(
            dataPantallaPrincipal(R.drawable.mando, "Titulo Videojuego"),
            dataPantallaPrincipal(R.drawable.mando, "Titulo Videojuego2"),
            dataPantallaPrincipal(R.drawable.mando, "Titulo Videojuego3"),
            dataPantallaPrincipal(R.drawable.mando, "Titulo Videojuego4"),
            dataPantallaPrincipal(R.drawable.mando, "Titulo Videojuego5")
        )
    }
}