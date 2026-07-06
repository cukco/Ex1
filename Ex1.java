import java.util.Scanner;

public class Ex51 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nhập địa chỉ email: ");
        String email = scanner.nextLine().trim();
        String emailRegex = "^[a-zA-Z0-9._]+@[a-zA-Z0-9.]+\\.[a-zA-Z]{2,6}$";
        if (email.matches(emailRegex)) {
            System.out.println("=>Email hợp lệ.");
        } else {
            System.out.println("Email không hợp lệ.");
        }
        scanner.close();
    }
}
