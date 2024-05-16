package estudos.procergs;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class Usuario extends PanacheEntity{
    
    @NotNull(message = "Obrigatório Informar o Nome")
    private String nome;
    private String email;

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}
