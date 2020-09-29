import java.util.*;

public class Main {
    public static void main(String[] args) {
        var array = new Scanner(System.in).nextLine().split(" ");

        var swaps = 0;
        for (var i = 0; i < array.length - 1; i++) {
            for (var j = 0; j < array.length - i - 1; j++) {
                if (Integer.parseInt(array[j]) > Integer.parseInt(array[j + 1])) {
                    final var temp = array[j + 1];
                    array[j + 1] = array[j];
                    array[j] = temp;
                    swaps++;
                }
            }
        }

        System.out.println(swaps);
    }
}