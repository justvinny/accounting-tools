public class IllegalAcroFormInputException extends IllegalArgumentException {

    public IllegalAcroFormInputException(String message) {
        super(message);
    }

    public IllegalAcroFormInputException() {
        this("Invalid field entered.");
    }

}
