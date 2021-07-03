

package carpool.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long userId;
	
	@Column(nullable = false, unique = true, length = 45)
	private String email;
	
	@Column(nullable = false, length = 64)
	private String password;
	
	@Column(name = "first_name", nullable = false, length = 20)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 20)
	private String lastName;
	
	private boolean enabled;
	
	private String country;
	
	@Column(nullable = true, length = 64)
    private String photos;
	
	@Column(name = "phoneNumber", length = 20)
	private String phoneNumber;
	
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
            )
	private Set<Role> roles = new HashSet<>(); 
	
	public Set<Role> getRoles() {
		return roles;
	}
	
	public void removeRole(Role role) {
		String n;
		System.out.println(n = role.getName());
		
		
		for (Role i : roles) {
			String m = i.getName();
			System.out.println(m);
			if (m.equals(n)) {
				roles.remove(i);
			}
		}
		
    }
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long id) {
		this.userId = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

  public String getCountry() {
	  return country;
	  }
  public void setCountry(String country) {
	  this.country = country;
	  }
  
  public String getPhotos() {
	return photos;
	}
  
  public void setPhotos(String photos) {
	  this.photos = photos;
	}
  
	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
  
  public String getPhotosImagePath() {
      if (photos == null || userId == null) return null;
       
      return "/user-photos/" + userId + "/" + photos;
  }

}
