package Dictionary;

public class Utils {
    protected static void checkIfStringIsValid(String string) {
        if (string == null || string.isBlank())
            throw new IllegalArgumentException("Value provided should not be null nor blank");

    }
}
