package g.tools.others;

import java.util.Random;

public class STool {

    public static void main(String[] args) {
        String [] games = {
                "Doom"
        };

        System.out.println(chooseRandom(games));
    }

    public static String chooseRandom(String[] set) {
        int size = set.length;
        Random random = new Random();
        int i = Math.abs(random.nextInt());

        return set[i % size];
    }

}
