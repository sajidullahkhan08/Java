public class Palindrome {
    public static void main(String[] args) {
        int number = 412343214;

        String numberS = String.valueOf(number);

        int[] numberArray = new int[numberS.length()];

        for (int i = 0; i < numberS.length(); i++) {
            numberArray[i] = Character.getNumericValue(numberS.charAt(i));
        }
        boolean flag = true;
        for (int i = numberArray.length-1, j = 0; j < numberArray.length/2; i--, j++) {
            if(numberArray[i] != numberArray[j]) {
                flag = false;
                break;
            }
        }

        if(flag == true) {
            System.out.println("The number is a palindrome...");
        }
        else {
            System.out.println("The number is not a palindrome..");
        }
    }
}
