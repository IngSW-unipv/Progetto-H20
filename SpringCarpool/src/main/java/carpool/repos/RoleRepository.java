package carpool.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import carpool.data.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

	@Query("SELECT u FROM Role u WHERE u.id = ?1")
	public Role findByuserId(Long userId);
	
	Role getRoleById(Integer id);

}
