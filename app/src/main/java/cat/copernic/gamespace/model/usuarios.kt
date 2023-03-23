package cat.copernic.gamespace.model

 data class usuarios(
    val nombre: String = "",
    val apellidos: String = "",
    val correo: String = "",
    val admin: Boolean = false
)