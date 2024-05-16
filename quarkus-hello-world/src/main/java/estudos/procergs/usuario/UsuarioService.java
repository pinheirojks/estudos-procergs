package estudos.procergs.usuario;


import java.util.List;

import estudos.procergs.entity.Usuario;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class UsuarioService {

    public List<Usuario> listaTodos(){
        return Usuario.findAll().list();
    }

}
