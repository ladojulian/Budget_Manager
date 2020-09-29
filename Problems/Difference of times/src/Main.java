import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        var firstHours = scanner.nextInt();
        var firstMinutes = scanner.nextInt();
        var firstSeconds = scanner.nextInt();

        var secondHours = scanner.nextInt();
        var secondMinutes = scanner.nextInt();
        var secondSeconds = scanner.nextInt();

        System.out.println(secondHours * 3600 + secondMinutes * 60 + secondSeconds
                - (firstHours * 3600 + firstMinutes * 60 + firstSeconds));
    }
}