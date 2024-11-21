package main;

import java.util.Map;

public class User {
	private String userName;
	private Integer id;
	private Map<String, Double> profileVector;

	public User() { }

	public User(String userName) {
		this.userName = userName;
	}
	
	public User(String userName, Integer id) {
		this.userName = userName;
		this.id = id;
	}

	public void setProfileVector(Map<String, Double> profileVector) {
		this.profileVector = profileVector;
	}
	
	public Map<String, Double>  getProfileVector() {
		return this.profileVector;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "User [userName=" + userName + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof User)) return false;
		User other = (User) obj;
		return userName.equals(other.userName);
	}
	
	@Override
	public int hashCode() {
		if (id == null) {
			return userName.hashCode();
		}
		return id.hashCode();
	}
}

