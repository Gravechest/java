import java.util.Scanner;

public class source {
    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Geef een geheel positief getal: ");
        int input = Math.abs(scan.nextInt());
        int even = 0,oneven = 0;
        for(int i = 0;i < input;i+=2){
            even += i;
        }
        for(int i = 1;i < input;i+=2){
            oneven += i;
        }
        System.out.print("Som van oneven getallen: ");
        System.out.println(oneven);
        System.out.print("Som van even getallen: ");
        System.out.println(even);
        System.out.print("Som van even getallen: ");
        System.out.println(Math.abs(oneven-even));
    }
}
