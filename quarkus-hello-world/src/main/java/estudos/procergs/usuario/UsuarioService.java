package estudos.procergs.usuario;


import java.util.List;

import estudos.procergs.entity.Usuario;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;

@RequestScoped
public class UsuarioService {

    public List<Usuario> listaTodos(){
        return Usuario.findAll().list();
    }

    @Transactional
    public Usuario criar(Usuario u) {
        if (u.login == null)  {
            throw new WebApplicationException("Informe o login.");
            }
            if (u.senha == null)  {
                throw new WebApplicationException("Informe a senha.");
            }
            u.ativo = true;
            u.persist();
            return u;
        }

    }
