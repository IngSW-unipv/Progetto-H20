package carpool.data;

import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
	@Id
	@Column(name = "role_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	
	@ManyToMany(mappedBy = "roles")
	Set<User> role_roles;
	
	public Set<User> getUsers() {
		return role_roles;
	}
	
	public Integer getId() {
	        return id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
