package main;

public class User {
	private String userName;

	public User() { }

	public User(String userName) {
		this.userName = userName;
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
		return userName.hashCode();
	}
}

