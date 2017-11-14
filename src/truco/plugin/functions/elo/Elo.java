package truco.plugin.functions.elo;

public class Elo {

    public static int ganhador(int winner, int loser) {
        double prob;
        int diff = Math.abs(winner - loser);
        if (winner > loser) {
            prob = strongProbability(diff);
        } else {
            prob = weakProbability(diff);
        }
        int rateChange = formula(prob, 1);
        return winner + rateChange;
    }

    public static int perdedor(int winner, int loser) {
        double prob;
        int diff = Math.abs(winner - loser);
        if (loser > winner) {
            prob = strongProbability(diff);
        } else {
            prob = weakProbability(diff);
        }
        int rateChange = formula(prob, 0);
        return loser + rateChange;
    }

    private static double strongProbability(int diff) {
        double prob;
        if (diff > 3500) {
            prob = 0.99;
        } else if (diff > 3000) {
            prob = 0.96;
        } else if (diff > 2500) {
            prob = 0.94;
        } else if (diff > 2000) {
            prob = 0.92;
        } else if (diff > 1500) {
            prob = 0.89;
        } else if (diff > 1250) {
            prob = 0.85;
        } else if (diff > 1000) {
            prob = 0.81;
        } else if (diff > 750) {
            prob = 0.76;
        } else if (diff > 500) {
            prob = 0.7;
        } else if (diff > 250) {
            prob = 0.64;
        } else if (diff > 100) {
            prob = 0.57;
        } else if (diff > 50) {
            prob = 0.53;
        } else {
            prob = 0.5;
        }
        return prob;
    }

    private static double weakProbability(int diff) {
        double prob;
        if (diff > 3500) {
            prob = 0.01;
        } else if (diff > 3000) {
            prob = 0.04;
        } else if (diff > 2500) {
            prob = 0.06;
        } else if (diff > 2000) {
            prob = 0.08;
        } else if (diff > 1500) {
            prob = 0.11;
        } else if (diff > 1250) {
            prob = 0.15;
        } else if (diff > 1000) {
            prob = 0.19;
        } else if (diff > 750) {
            prob = 0.24;
        } else if (diff > 500) {
            prob = 0.3;
        } else if (diff > 250) {
            prob = 0.36;
        } else if (diff > 100) {
            prob = 0.43;
        } else if (diff > 50) {
            prob = 0.47;
        } else {
            prob = 0.5;
        }
        return prob;
    }

	// The formula for figuring out how much to change
    // Outcome is 1 for win and 0 for loss.
    private static int formula(double prob, double outcome) {
        return (int) (K * (outcome - prob));
    }

    // K-factor determines how large or small the change is in ranks.
    public final static int K = 50;
}
