package estudos.procergs.entity;

import org.hibernate.boot.model.source.spi.Sortable;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
