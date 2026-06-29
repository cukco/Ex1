import java.util.Scanner;
public class Ex21 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int sum=0;
        for(int i=1;i<=n;i++){
            sum+=i;
        }
        System.out.printf("Tổng của các số từ 1 đến %d là: %d",n,sum);
    }
}
