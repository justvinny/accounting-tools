public class RegexUtilities {
    public static final String VALID_NUMBER_PATTERN = "\\d+\\.?\\d+|(\\d{1,3}(,|\\s))+\\d{3}(\\.\\d+)?";
    public static final String EXTRACT_NUMBER_PATTERN = "[^0-9.]";

    public static float extractNumber(String str) {
        return Float.parseFloat(str.replaceAll(EXTRACT_NUMBER_PATTERN, ""));
    }
}
