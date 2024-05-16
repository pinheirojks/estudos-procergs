package estudos.procergs;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Pessoa extends PanacheEntity {
 
    @Column(name="first_name")
    public String firstName;

	@Column(name="last_name")
    public String lastName;

    public String salutation;
}
