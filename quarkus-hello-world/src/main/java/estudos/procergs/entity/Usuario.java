package estudos.procergs.entity;

import org.hibernate.boot.model.source.spi.Sortable;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "usuario")
public class Usuario extends PanacheEntity implements Sortable {

    private String login;

    private String senha;

    private Boolean ativo;

    @Override
    public String getComparatorName() {
        return login;
    }

    @Override
    public boolean isSorted() {
        return true;
    }

}
