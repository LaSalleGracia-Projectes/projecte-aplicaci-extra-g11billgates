package org.example.teamup.models;

import java.util.List;

public class UsuarioDTO {
    public int id;
    public String Nombre;
    public String email;
    public String FotoPerfil;
    public int Edad;
    public String Region;
    public List<JuegoDTO> Juegos;
    public UsuarioDTO() {
    }
}
