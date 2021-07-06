package carpool.Services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import carpool.data.Role;
import carpool.data.User;
import carpool.repos.RoleRepository;
import carpool.repos.UserRepository;
import carpool.services.UserService;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)


public class UserServiceTest {

	@MockBean
	private User MockUser; 
	
	@InjectMocks
	private UserService MockUserService;
	
	@MockBean
	private UserRepository MockUserRepo;

	@MockBean
	private RoleRepository MockRoleRepo;
	
	private int MockRoleId = 10;
	private int MockRoleId2 = 11;
	
	private long MockUserId = 10;

	private String roleName ="PASSEGGERO";
	private String roleName2 ="AUTISTA";
	
	private String password="lucavaghi";
	private String password1="lucaluca";

	private String firstName="luca";
	private String firstName1="simone";
	private String lastName="vaghi";
	private String lastName1="alterni";
	private String email="lucavaghi@ejhdje.itg";
	private String email1="dejknjfew@dkjke.ihd";
	private String phone="lucavaghi@ejhdje.itg";
	private String phone1="dejknjfew@dkjke.ihd";
	
	/**
	 * Test method for {@link carpool.services.TripService#bookTrip(carpool.data.Reservation, long)}.
	 */
	@Test
	@DisplayName("test della prenotazione viaggio")
	void testEditUser() {
		
		//creo user

		User user1=new User();
		User user2=new User();

		user1.setFirstName(firstName);
		user2.setFirstName(firstName1);
		
		user1.setLastName(lastName);
		user2.setLastName(lastName1);
		
		user1.setEmail(email);
		user2.setEmail(email1);
		
		user1.setPhoneNumber(phone);
		user2.setPhoneNumber(phone1);
	
		//mock save user 2
		Mockito.when(MockUserRepo.save(user2)).thenReturn(null);
		
		MockUserService.editUser(user1, user2);
		
		assertTrue(user1.getFirstName().equalsIgnoreCase(user2.getFirstName())&&user1.getLastName().equalsIgnoreCase(user2.getLastName()));
	}
	
	@Test
	@DisplayName("test edit Password")
	void testEditPassword() {
		
		//creo user

		User user1=new User();
		user1.setPassword(password);
	
		//mock save user 2
		Mockito.when(MockUserRepo.save(user1)).thenReturn(null);
		
		MockUserService.editUserPassword(password1, user1);
		
		assertTrue(user1.getPassword()!=password);

	}
	
	@Test
	@DisplayName("test delete user")
	void testRemoveUserRole() {
		
		User user1 = new User();
		Role role  =  new Role();
	
		role.setName("prova");
		role.setId(MockRoleId);
		user1.setUserId(MockUserId);
		
		Mockito.when(MockUserRepo.findById(MockUserId)).thenReturn(Optional.of(user1));
		Mockito.when(MockRoleRepo.findById(MockRoleId)).thenReturn(Optional.of(role));
		Mockito.when(MockUserRepo.save(user1)).thenReturn(null);
		Mockito.when(MockRoleRepo.save(role)).thenReturn(null);

		MockUserService.removeUserRole(MockRoleId, MockUserId);
	}
	
	@Test
	@DisplayName("test delete user")
	void testUserRegister()
	{
		User user = new User();
		user.setPassword(password);
		user.setEmail(email);
		
		List<Role> roles = new ArrayList<Role>();
		Role role = new Role();
		role.setName(roleName);
		roles.add(role);
		
	    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	    String encodedPassword = passwordEncoder.encode(password);
		
		//Mock viaggio repo
		Mockito.when(MockUserRepo.findByEmail(email)).thenReturn(user);
		
		//mock res repo
		Mockito.when(MockRoleRepo.findAll()).thenReturn(roles);
		
		//mock save
		Mockito.when(MockUserRepo.save(user)).thenReturn(null);
		
		boolean result = MockUserService.registerUser(user);
		if(result)
		{
			assertTrue(user.isEnabled() && user.getRoles().contains(role) && user.getPassword().equals(encodedPassword));
		}
		else
		{
			
		}
		
	}
	
	@Test
	@DisplayName("test add User")
	void testAddUserRole()
	{
	
		User user = new User();
		user.setUserId(MockUserId);
		
		Role newRole = new Role();
		newRole.setId(MockRoleId2);
		newRole.setName(roleName);
		
		List<Role> existingRoles = new ArrayList<Role>();
		Role existingRole = new Role();
		existingRole.setName(roleName2);
		existingRole.setId(MockRoleId);
		existingRoles.add(existingRole);
		existingRoles.add(newRole);
		
		user.addRole(existingRole);
		
		Mockito.when(MockUserRepo.findById(MockUserId)).thenReturn(Optional.of(user));
		Mockito.when(MockRoleRepo.findById(MockRoleId2)).thenReturn(Optional.of(newRole));
		Mockito.when(MockRoleRepo.findAll()).thenReturn(existingRoles);
		Mockito.when(MockUserRepo.save(user)).thenReturn(null);
		
		//check user.getRoles().add(role);
		boolean result = MockUserService.addUserRole(MockRoleId2,MockUserId);
		if(result)
		{
			assertTrue(user.getRoles().contains(newRole));
		}
		else
		{
			assertTrue(user.getRoles().contains(newRole));
		}
		
	}
		
}
