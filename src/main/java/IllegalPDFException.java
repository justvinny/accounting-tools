public class IllegalPDFException extends IllegalArgumentException {

    public IllegalPDFException(String message) {
        super(message);
    }

    public IllegalPDFException() {
        this("Invalid PDF file. Please enter valid Home Office Questionnaire.");
    }
}
