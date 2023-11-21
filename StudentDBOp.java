import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.sql.Date;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Scanner;

public class StudentDBOp {
    private final String url = "jdbc:postgresql://localhost:5432/Assignment4DB";
    private final String user = "postgres";
    private final String password = "postgres";

    public void getAllStudents(){
        System.out.println("\nPrinting all students....");

        // SQL
        String SQL = "SELECT * FROM students";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int studentId = rs.getInt("student_id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                Date enrollmentDate = rs.getDate("enrollment_date");

                // printing student info
                System.out.println("Student ID: " + studentId);
                System.out.println("Name: " + firstName + " " + lastName);
                System.out.println("EMAIL: " + email);
                System.out.println("Enrollment Date: " + enrollmentDate);
                System.out.println("--------------------------");
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void addStudent(String first_name,String last_name,String email,Date enrollment_date){
        System.out.println("\nadding new student");
        System.out.println("With name: " + first_name + " " + last_name);
        System.out.println("EMAIL: " + email);
        System.out.println("Enrollment_date: " + enrollment_date);

        String SQL = "INSERT INTO students(first_name,last_name,email,enrollment_date) VALUES(?,?,?,?)";

        //fill in the data
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, first_name);
            pstmt.setString(2, last_name);
            pstmt.setString(3, email);
            pstmt.setDate(4, enrollment_date);
            pstmt.executeUpdate();
            System.out.println("Student added successfully!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void updateStudentEmail(int student_id,String new_email){
        System.out.println("\nupdating student Email");
        System.out.println("student ID: " + student_id);
        System.out.println("NEW EMAIL: " + new_email);

        String SQL = "UPDATE students SET email=? WHERE student_id=?";
        //fill in the data
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setString(1, new_email);
            pstmt.setInt(2, student_id);
            pstmt.executeUpdate();
            System.out.println("User email updated!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void deleteStudent(int student_id){
        System.out.println("\ndeleting student where student_id: " + student_id);

        String SQL = "DELETE FROM students WHERE student_id=?";
        //fill in the data
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(SQL)) {

            pstmt.setInt(1, student_id);
            pstmt.executeUpdate();
            System.out.println("User deleted!");

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void main(String[] args) {
        StudentDBOp stuOps = new StudentDBOp();
        Scanner scanner = new Scanner(System.in);

        String first_name,last_name,email,dateString;
        Date enrollment_date;
        int id;
        int choose = -1;

        //main menu loop until user choose 0
        System.out.println("Hello!");
        System.out.println("What can I do for you?");
        while(true) {
            System.out.println("--------------------------");
            System.out.println("1. getAllStudents");
            System.out.println("2. addStudent");
            System.out.println("3. updateStudentEmail");
            System.out.println("4. deleteStudent");
            System.out.println("0. EXIT");

            choose = scanner.nextInt();
            scanner.nextLine();

            switch(choose){
                case 1: //getAllStudents
                    stuOps.getAllStudents();
                    break;
                case 2: //addStudent
                    System.out.print("First name: ");
                    first_name = scanner.nextLine();
                    System.out.print("Last name: ");
                    last_name = scanner.nextLine();
                    System.out.print("Email: ");
                    email = scanner.nextLine();
                    System.out.print("Enrollment date: (yyyy-MM-dd)");
                    dateString = scanner.nextLine();

                    //change String date into Date
                    try {
                        java.util.Date utilDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
                        enrollment_date = new java.sql.Date(utilDate.getTime());
                    } catch (ParseException e) {
                        System.out.println("ERROR!! invalid date format.");
                        break;
                    }

                    stuOps.addStudent(first_name, last_name, email, enrollment_date);

                    break;
                case 3: //updateStudentEmail
                    System.out.print("Student ID: ");
                    id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("New email: ");
                    email = scanner.nextLine();
                    stuOps.updateStudentEmail(id, email);

                    break;
                case 4: //deleteStudent

                    System.out.print("Student ID to delete: ");
                    id = scanner.nextInt();

                    scanner.nextLine();
                    stuOps.deleteStudent(id);
                    
                    break;
                case 0: //exit
                    System.out.println("good bye!");
                    scanner.close();
                    return;
            }
        }
    }
}
