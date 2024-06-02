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

        if (pesq.getMatricula() != null) {
            clausulas.add("matricula = :matricula");
            parametros.put("matricula", pesq.getMatricula());
        }
        if (pesq.getNome() != null) {
            clausulas.add("nome like :nome");
            parametros.put("nome", "%" + pesq.getNome() + "%");
        }
        if (pesq.getAtivo() != null) {
            clausulas.add("ativo = :ativo");
            parametros.put("ativo", pesq.getAtivo());
        }
        String restricoes = clausulas.stream()
                .collect(Collectors.joining(" and "));

        Sort ordenacao = Sort.ascending("nome");
        PanacheQuery<Usuario> query = this.find(restricoes, ordenacao, parametros);

        return query.list();
    }

    public Usuario consultar(Long matricula, String senha) {
        try {
            List<String> clausulas = new ArrayList<>();

            Map<String, Object> parametros = new HashMap<String, Object>();

            clausulas.add("matricula = :matricula");
            parametros.put("matricula", matricula);

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

    public List<Usuario> listarDuplicados(Long matricula) {
        Usuario pesq = new Usuario();
        pesq.setAtivo(true);
        pesq.setMatricula(matricula);
        return this.listar(pesq);
    }
}
