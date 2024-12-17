package Entities;

public class Admin {
    private int adminId;
    private String adminName;

    public Admin() {
    }

    public Admin(int adminId, String adminName) {
        this.adminId = adminId;
        this.adminName = adminName;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public void createStudent(int studentId, String studentName) {}

    public void deleteStudent(int studentId) {}

    public void updateStudent(int studentId, String studentName) {}
}
