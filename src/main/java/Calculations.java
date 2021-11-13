import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class Calculations {

    private BigDecimal percentageToClaim;
    private BigDecimal totalClaims;
    private BigDecimal totalValue;

    private List<List<String>> entries;

    public Calculations(List<List<String>> entries) throws IllegalAcroFormInputException {
        this.entries = entries;
        getPercentageToClaim();
        calculateClaims();
        entries.remove(0); // Remove address
    }

    private void getPercentageToClaim() throws IllegalAcroFormInputException {
        String houseArea = entries.get(1).get(1);
        String businessArea = entries.get(2).get(1);
        BigDecimal houseAreaAmount = houseArea.length() > 0 ? new BigDecimal(houseArea) : new BigDecimal("0");
        BigDecimal businessAreaAmount = businessArea.length() > 0 ? new BigDecimal(businessArea) : new BigDecimal("0");

        if (houseAreaAmount.compareTo(new BigDecimal("0")) > 0 && businessAreaAmount.compareTo(new BigDecimal("0")) > 0) {
            percentageToClaim = businessAreaAmount.divide(houseAreaAmount, 4, RoundingMode.HALF_EVEN);
        } else {
            throw new IllegalAcroFormInputException("Area of house and area used for business should not be blank.");
        }

        entries.get(3).add(percentageToClaim.toString());
    }

    private void calculateClaims() throws IllegalAcroFormInputException {
        totalValue = new BigDecimal("0");
        totalClaims = new BigDecimal("0");
        for (int i = 4; i < entries.size(); i++) {
            String value = entries.get(i).get(1);

            if (value.isEmpty()) {
                continue;
            }

            if (!value.matches(RegexUtilities.VALID_NUMBER_PATTERN)) {
                throw new IllegalAcroFormInputException(String.format("%s is an invalid number.", value));
            }

            BigDecimal decimalValue = new BigDecimal(value);
            BigDecimal claim;
            try {
                BigDecimal castedValue = RegexUtilities.extractNumber(value);
                totalValue = totalValue.add(decimalValue);
                entries.get(i).remove(1);
                entries.get(i).add(castedValue.setScale(2, RoundingMode.HALF_EVEN).toString());
                if (entries.get(i).get(0).equals("Phone / Internet")) {
                    claim = decimalValue.multiply(new BigDecimal("0.5"));
                } else {
                    claim = decimalValue.multiply(percentageToClaim);
                }
                totalClaims = totalClaims.add(claim);
                entries.get(i).add(claim.setScale(2, RoundingMode.HALF_EVEN).toString());
            } catch (NumberFormatException ex) {
                throw new IllegalAcroFormInputException(String.format("Can't calculate. %s is an invalid number.", value));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Client name\n");
        stringBuilder.append("Home office calculations\n");
        stringBuilder.append("For the year ended\n");
        stringBuilder.append("\n\n\n");

        for (int i = 0; i < 3; i++) {
            List<String> row = entries.get(i);
            for (String value : row) {
                if (value.isEmpty()) {
                    continue;
                }
                stringBuilder.append(value).append(",");
            }
            stringBuilder.setCharAt(stringBuilder.length() - 1, '\n');
        }

        stringBuilder.append("\n\nProperty Expenses,Amount,Claim\n");

        for (int i = 3; i < entries.size(); i++) {
            List<String> row = entries.get(i);
            for (String value : row) {
                if (value.isEmpty()) {
                    continue;
                }
                stringBuilder.append(value).append(",");
            }
            stringBuilder.setCharAt(stringBuilder.length() - 1, '\n');
        }

        stringBuilder.append(String.format("Total,%s,%s", totalValue.setScale(2, RoundingMode.HALF_EVEN), totalClaims.setScale(2, RoundingMode.HALF_EVEN)));

        return stringBuilder.toString();
    }
}
