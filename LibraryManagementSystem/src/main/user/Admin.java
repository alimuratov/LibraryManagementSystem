package main.user;

public class Admin extends User {
    private String adminId;

    public Admin() {
        super();
    }

    public Admin(String userName, String adminId) {
        super(userName);
        this.adminId = adminId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    @Override
    public void performRole() {
        System.out.println("Admin " + getUserName() + " is managing the system.");
    }

    @Override
    public String toString() {
        return "Admin [userName=" + getUserName() + ", adminId=" + adminId + "]";
    }
}
