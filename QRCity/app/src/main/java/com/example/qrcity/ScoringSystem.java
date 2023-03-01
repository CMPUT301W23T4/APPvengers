package com.example.qrcity;

public class ScoringSystem {

    public int getScore(String hash){
        assert hash.length() == 64;
        char[] charArray = hash.toCharArray();
        int score = 0;
        int count = 1;

        int current;
        int previous = 0;

        boolean calcScore;

        //Cycle through all elements in the char array
        for (int i=0; i<64; i++){

            calcScore = false;

            //Get point value of the current character
            current = charArray[i];

            if (current <=57){
                current -= 48;
            }
            else{
                current -= 87;
            }

            if (current == 0){
                current = 20;
            }

            //count elements
            if (i == 0){
                previous = current;
                continue;
            }
            else{
                if (current != previous || i == 63){
                    calcScore = true;
                }
                else{
                    count++;
                }
            }

            //calculate score addition
            if (calcScore){
                int add = 0;

                /*if (count == 1 && previous == 20) {
                    add = (int)(Math.pow(previous, count-1));
                }
                else*/ if (count > 1) {
                    add = (int)(Math.pow(previous, count-1));
                }

                score += add;

                previous = current;
                count = 1;
            }
        }

        return score;
    }
}
