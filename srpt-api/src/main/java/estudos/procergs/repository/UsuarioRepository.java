package estudos.procergs.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import estudos.procergs.entity.Usuario;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;

@ApplicationScoped
public class UsuarioRepository implements PanacheRepository<Usuario> {

    public List<Usuario> listar(Usuario pesq) {
        List<String> clausulas = new ArrayList<>();
        Map<String, Object> parametros = new HashMap<String, Object>();

        if (pesq.getLogin() != null) {
            clausulas.add("login like :login");
            parametros.put("login", "%" + pesq.getLogin() + "%");
        }
        if (pesq.getAtivo() != null) {
            clausulas.add("ativo = :ativo");
            parametros.put("ativo", pesq.getAtivo());
        }
        String restricoes = clausulas.stream()
                .collect(Collectors.joining(" and "));

        Sort ordenacao = Sort.ascending("login");
        PanacheQuery<Usuario> query = this.find(restricoes, ordenacao, parametros);

        return query.list();
    }

    public Usuario consultar(String login, String senha) {
        try {
            List<String> clausulas = new ArrayList<>();

            Map<String, Object> parametros = new HashMap<String, Object>();

            clausulas.add("login = :login");
            parametros.put("login", login);

            clausulas.add("senha = :senha");
            parametros.put("senha", senha);

            clausulas.add("ativo = :ativo");
            parametros.put("ativo", true);

            String restricoes = clausulas.stream()
                    .collect(Collectors.joining(" and "));

            PanacheQuery<Usuario> query = this.find(restricoes, parametros);
            return query.singleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    public List<Usuario> listarDuplicados(String login) {
        Usuario pesq = new Usuario();
        pesq.setAtivo(true);
        pesq.setLogin(login);
        return this.listar(pesq);
    }
}
