package estudos.procergs.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import estudos.procergs.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;

@RequestScoped
public class UsuarioService {

    public List<Usuario> listar(Usuario pesq) {
        List<String> restricoes = new ArrayList<>();
        Map<String, Object> parametros = new HashMap<String, Object>();
        
        if (pesq.getLogin() != null) {
            restricoes.add("login like :login");
            String login = String.format("%%s%", pesq.getLogin());
            parametros.put("login", login);
        }        
        if (pesq.getAtivo() != null) {
            restricoes.add("ativo = :ativo");
            parametros.put("ativo", pesq.getAtivo());
        }
        String stringRestricoes = restricoes.stream()
            .collect(Collectors.joining(" and "));

		Sort ordenacao = Sort.ascending("ativo", "login");
		PanacheQuery<Usuario> query = Usuario.find(stringRestricoes, parametros, ordenacao);
        return query.list();
    }

    public Usuario consultar(Long id) {
        return Usuario.findById(id);
    }

    @Transactional
    public Usuario incluir(Usuario usuario) {
        this.exigirLogin(usuario);
        this.exigirSenha(usuario);

        usuario.setAtivo(true);
        usuario.persist();
        return usuario;
    }

    @Transactional
    public Usuario alterar(Long id, Usuario u) {
        this.exigirLogin(u);
        this.exigirSenha(u);
        this.exigirAtivo(u);

        Usuario usuario = Usuario.findById(id);

        usuario.setLogin(u.getLogin());
        usuario.setSenha(u.getSenha());
        usuario.setAtivo(u.getAtivo());
        return usuario;
    }

    @Transactional
    public void excluir(Long id) {
        Usuario usuario = Usuario.findById(id);
        usuario.delete();
    }

    private void exigirLogin(Usuario usuario) {
        if (usuario.getLogin() == null) {
            throw new WebApplicationException("Informe o login.");
        }
    }

    private void exigirSenha(Usuario usuario) {
        if (usuario.getSenha() == null) {
            throw new WebApplicationException("Informe a senha.");
        }
    }

    private void exigirAtivo(Usuario usuario) {
        if (usuario.getAtivo() == null) {
            throw new WebApplicationException("Informe se está ativo.");
        }
    }
}
