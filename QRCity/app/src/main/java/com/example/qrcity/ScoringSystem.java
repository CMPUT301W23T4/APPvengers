package com.example.qrcity;

// WIP

public class ScoringSystem {

    public int getScore(String hash){
        assert hash.length() == 64;
        char[] charArray = hash.toCharArray();
        int score = 0;
        int count = 1;

        char Current;
        char Previous;

        boolean addScore;

        for (int i=1; i<64; i++){

            //Get the current and previous characters in the array
            Current = charArray[i];
            Previous = charArray[i-1];
            addScore = false;

            //Check if we should calculate the score for a number streak
            if (Current != Previous){   //Streak is broken
                addScore = true;
            } else {
                count++;
                if (i == 63){   //Reached the last element
                    addScore = true;
                }
            }

            //calculate the score for a number streak
            if (addScore) {
                if (count>1){   //Streak is larger than 1
                    switch (Previous){
                        case '0':
                            score += Math.pow(20, (count-1));
                            break;
                        case '1':
                            score += Math.pow(1, (count-1));
                            break;
                        case '2':
                            score += Math.pow(2, (count-1));
                            break;
                        case '3':
                            score += Math.pow(3, (count-1));
                            break;
                        case '4':
                            score += Math.pow(4, (count-1));
                            break;
                        case '5':
                            score += Math.pow(5, (count-1));
                            break;
                        case '6':
                            score += Math.pow(6, (count-1));
                            break;
                        case '7':
                            score += Math.pow(7, (count-1));
                            break;
                        case '8':
                            score += Math.pow(8, (count-1));
                            break;
                        case '9':
                            score += Math.pow(9, (count-1));
                            break;
                        case 'a':
                            score += Math.pow(10, (count-1));
                            break;
                        case 'b':
                            score += Math.pow(11, (count-1));
                            break;
                        case 'c':
                            score += Math.pow(12, (count-1));
                            break;
                        case 'd':
                            score += Math.pow(13, (count-1));
                            break;
                        case 'e':
                            score += Math.pow(14, (count-1));
                            break;
                        case 'f':
                            score += Math.pow(15, (count-1));
                            break;
                    }
                } else if (Previous == '0') {   //The previous value was a 0
                    score++;
                }
                count = 0;
            }

        }

        return score;
    }
}
