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
    public Usuario criar(Usuario u) {
        if (u.login == null) {
            throw new WebApplicationException("Informe o login.");
        }
        if (u.senha == null) {
            throw new WebApplicationException("Informe a senha.");
        }
        u.ativo = true;
        u.persist();
        return u;
    }
}
