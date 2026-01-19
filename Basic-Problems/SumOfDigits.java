public class SumOfDigits {
    public static void main(String[] args) {
        int number = 234242;

        String numberS = String.valueOf(number);

        int[] digits = new int[numberS.length()];

        for(int i = 0; i < numberS.length(); i++) {
            digits[i] = Character.getNumericValue(numberS.charAt(i));
        }

        int sum = digits[0];

        for(int i = 0; i < digits.length-1; i++) {
            sum += digits[i+1];
        }

        System.out.println("The sum of digits in that number is: " + sum);
    }
}