package estudos.procergs.service;

import java.util.List;

import estudos.procergs.entity.Usuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@RequestScoped
public class UsuarioService {

    public List<Usuario> listaTodos() {
        return Usuario.findAll().list();
    }

    @Transactional
    public Usuario criar(Usuario usuario) {
        if (usuario.getLogin() == null) {
            throw new WebApplicationException("Informe o login.");
        }
        if (usuario.getSenha() == null) {
            throw new WebApplicationException("Informe a senha.");
        }
        usuario.setAtivo(true);
        usuario.persist();
        return usuario;
    }
}
