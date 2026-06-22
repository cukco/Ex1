import java.util.Scanner;
import java.lang.Math;
public class Ex1 {
    public static void main(String[] args) {
        float radius;
        Scanner scanner = new Scanner(System.in);
        System.out.print("bán kính : ");
        radius = scanner.nextFloat();
        System.out.printf("Diện tích: %.2f",radius*Math.PI);
    }
}
