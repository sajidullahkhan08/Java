package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationUtility {
    
    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return false;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        
        try {
            Date date = sdf.parse(dateStr.trim());
            // Check if date is not in future
            return !date.after(new Date());
        } catch (ParseException e) {
            return false;
        }
    }
    
    public static Date parseDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        return sdf.parse(dateStr.trim());
    }
    
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.trim().length() >= 2;
    }
    
    public static boolean isValidAge(int age) {
        return age >= 0 && age <= 150;
    }
    
    public static String formatDateForDisplay(Date date) {
        if (date == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        return sdf.format(date);
    }
    
    public static int calculateAge(Date dob) {
        if (dob == null) return 0;
        
        java.util.Calendar now = java.util.Calendar.getInstance();
        java.util.Calendar dobCal = java.util.Calendar.getInstance();
        dobCal.setTime(dob);
        
        int age = now.get(java.util.Calendar.YEAR) - dobCal.get(java.util.Calendar.YEAR);
        
        if (now.get(java.util.Calendar.DAY_OF_YEAR) < dobCal.get(java.util.Calendar.DAY_OF_YEAR)) {
            age--;
        }
        
        return age;
    }
}