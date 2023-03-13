/** Author(s): Derek
 *  Purpose: Evaluate hash values by returning a score or a unique name associated to that hash
 */

package com.example.qrcity;

/**
 * This class defines a set of function used for examining a codes hash value
 */

public class ScoringSystem {

    String[][] nameBuilder = {{"Divine", "Master", "Amateur", "Novice"},
                              {"Earth", "Water", "Wind", "Fire"},
                              {"Warrior", "Mage", "Archer", "Thief"}};

    /**
     * This returns an integer score of the given hash value
     * @param hash
     */
    public int getScore(String hash){
        //Ensure the hash is of the correct length
        assert hash.length() == 64;

        //Convert the hash to an iterable character array
        char[] charArray = hash.toCharArray();

        //Initialize some variables for the iteration
        int score = 0;
        int count = 1;

        int current;
        int previous = 0;

        boolean calcScore;

        //Cycle through all elements in the char array
        for (int i = 0; i < 64; i++){

            calcScore = false;

            //Get point value of the current character
            current = hexToInt(charArray[i]);

            //count elements
            if (i == 0){
                previous = current;
                continue;
            }
            else{
                if (current != previous || (i == 63 && current == previous)){
                    calcScore = true;
                    if (i == 63 && current == previous){
                        count++;
                    }
                }
                else{
                    count++;
                }
            }

            //calculate score addition
            if (calcScore){
                int add = 0;

                if (count == 1 && previous == 20) {
                    add = (int)(Math.pow(previous, count-1));
                }
                else if (count > 1) {
                    add = (int)(Math.pow(previous, count-1));
                }

                score += add;

                previous = current;
                count = 1;
            }
        }

        return score;
    }

    /**
     * This generates a unique human readable name based on the first 6 binary bits of the hash
     * @param hash
     */
    public String getName(String hash){
        //Convert the hash into a char array
        char[] charArray = hash.toCharArray();

        //Get the 2 integer values that each represent half the bits of the first character in the hash
        //These two integers represent the first four bits of the hash
        String first = hexToBin(charArray[0]);
        int[] firstID = splitBinArray(first);

        //Get the 2 integer values that each represent half the bits of the second character in the hash
        //These two integers represent the second four bits of the hash
        String second = hexToBin(charArray[1]);
        int[] secondID = splitBinArray(second);

        //Build the name from the nameBuilder array and the first 6 bits of the hash
        String name = nameBuilder[0][firstID[0]];
        name += " " + nameBuilder[1][firstID[1]];
        name += " " + nameBuilder[2][secondID[0]];

        return name;
    }

    /**
     * Helper function - This returns the integer equivalent of the char input
     * @param current
     */
    private int hexToInt(char current){
        if (current <= 57){
            current -= 48;
        }
        else{
            current -= 87;
        }
        if (current == 0){
            current = 20;
        }

        return current;
    }

    /**
     * Helper function - This converts the hexadecimal formatted character into a binary array string with 4 bits
     * @param current
     */
    private String hexToBin(char current){
        //handle the 0 edge case
        if (current == '0'){
            return "0000";
        }

        //Convert to binary string
        String binArray = Integer.toBinaryString(hexToInt(current));

        //Insert leading 0's as needed to reach all 4 bits
        while (binArray.length() < 4) {
            binArray = '0' + binArray;
        }

        return binArray;
    }

    /**
     * Helper function - Reformats the binary string array with 4 bits, into an int array of length 2
     * where each int is the representation of 2 binary bits
     * @param binArray
     */
    private int[] splitBinArray(String binArray){
        //Ensure the length of the binary array is 4
        int n = binArray.length();
        assert n == 4;

        //Convert the string to a char array
        char[] charArray = binArray.toCharArray();

        //Initialize the return array
        int[] val = {0, 0};

        //Calculate the value of 2 integers
        for (int i = 0; i<n; i++)
        {
            val[(int)Math.floor(i/2)] += (charArray[i]-'0')*Math.pow(2, (int)((i+1)%2));
        }

        return val;
    }
}
