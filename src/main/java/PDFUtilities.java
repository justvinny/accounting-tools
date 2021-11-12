import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PDFUtilities {

    private static final String[] keys = {
            "Property Address",
            "Area of House (m2)",
            "Area used for business (m2)",
            "Percentage to claim",
            "Gardening",
            "Insurance",
            "Repairs & Maintenance (attached details of amounts)",
            "Property Manager Costs (attached reports if held)",
            "Legal Expenses",
            "Motor Vehicles Expenses (visiting property)",
            "Power / Gas",
            "Phone / Internet",
            "Other Costs (1)",
            "Other Costs (2)",
            "Other Costs (3)",
            "Interest Expenses (attached certificate from bank)",
            "Rental Cost (if applicable)",
            "Rates",
    };
    private static final int VALID_FIELD_SIZE = 18; // Number of acro form fields in correct PDF.

    public static List<List<String>> getFormEntries(File file) throws IllegalPDFException {
        List<List<String>> amounts = new LinkedList<>();

        PDDocument document = null;
        try {
            document = PDDocument.load(file);
            PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDAcroForm form = catalog.getAcroForm();

            // Iterators
            Iterator<String> keyIterator = Arrays.asList(keys).iterator();
            Iterator<PDField> valueIterator = form.getFieldIterator();

            while (valueIterator.hasNext() && keyIterator.hasNext()) {
                List<String> entries = new LinkedList<>();

                // Key
                String key = keyIterator.next();

                // Value
                PDField field = valueIterator.next();
                String value = field.getValueAsString().trim();

                entries.add(key);
                if (!key.equals("Percentage to claim")) {
                    entries.add(value);
                }
                amounts.add(entries);
            }

            if (!checkValidPDF(amounts)) {
                throw new IllegalPDFException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (document != null) {
                try {
                    document.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return amounts;
    }

    private static boolean checkValidPDF(List<List<String>> amounts) {
        return amounts.size() == VALID_FIELD_SIZE;
    }
}
