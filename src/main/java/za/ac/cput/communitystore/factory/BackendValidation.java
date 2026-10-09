package za.ac.cput.communitystore.factory;
import java.math.BigDecimal;
public final class BackendValidation {
    private BackendValidation() { }
    public static String text(String value, String field, int max) {
        if (value == null || value.isBlank() || value.trim().length() > max)
            throw new IllegalArgumentException(field + " is required and must be at most " + max + " characters");
        return value.trim();
    }
    public static BigDecimal money(BigDecimal value) {
        if (value == null || value.signum() < 0 || value.stripTrailingZeros().scale() > 2
                || value.compareTo(new BigDecimal("99999999.99")) > 0)
            throw new IllegalArgumentException("amount must be non-negative, at most 99999999.99, with at most two decimal places");
        return value.setScale(2);
    }
}
