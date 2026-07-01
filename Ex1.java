import java.util.Scanner;

public class Ex41 {
    public static void BubbleSort(int[] arr ){
        int n = arr.length;
        boolean flag = true;
        for(int i=0;i<n-1;i++){
            flag = false;
            for(int j=0;j<n-i-1;j++){
                if(arr[j]<arr[j+1]){
                    int temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                    flag = true;
                }
            }
            if(!flag){
                break;
            }
        }
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập số lượng phần tử của mảng: ");
        int n = sc.nextInt();
        System.out.print("Nhập các phần tử của mảng:\n");
        int[] mang = new int[n];
        for(int i=1;i<=n;i++){
            System.out.printf("Phần tử thứ %d: ",i);
            mang[i-1] = sc.nextInt();
        }
        BubbleSort(mang);
        System.out.println("Mảng sau khi sắp xếp giảm dần");
        for(int i=0;i<n;i++){
            System.out.print(mang[i]+" ");
        }
    }
}
