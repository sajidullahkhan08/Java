public class ReverseNumber {
    public static void main(String[] args) {
        int number = 34341234;
        
        String numberS = String.valueOf(number);
        int[] numberArray = new int[numberS.length()];
        
        for(int i = 0; i < numberS.length(); i++) {
            numberArray[i] = Character.getNumericValue(numberS.charAt(i));
        }
        
        int[] reverseArray = new int[numberS.length()];
        
        for(int i = numberArray.length - 1, j = 0; i >= 0; i--, j++) {
            reverseArray[j] = numberArray[i];
        }
        
        StringBuilder finalNumber = new StringBuilder();
        for(int i = 0; i < reverseArray.length; i++) {
            finalNumber.append(reverseArray[i]);
        }
        
        System.out.println("The reverse is: " + finalNumber.toString());
    }
}