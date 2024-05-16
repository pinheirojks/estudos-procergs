package estudos.procergs.entity;

import estudos.procergs.dto.UsuarioDTO;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuario")
public class Usuario extends PanacheEntity {

    public String login;

    public String senha;

    public Boolean ativo;

    public Usuario() {
    }

    public Usuario(UsuarioDTO dto) {
        this.id = dto.id;
        this.login = dto.login;
        this.senha = dto.senha;
        this.ativo = dto.ativo;
    }
}
