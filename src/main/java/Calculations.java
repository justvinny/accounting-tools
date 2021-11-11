import java.util.List;

public class Calculations {

    private double percentageToClaim;
    private double totalClaims;
    private double totalValue;

    private List<List<String>> entries;

    public Calculations(List<List<String>> entries) throws IllegalArgumentException {
        this.entries = entries;
        getPercentageToClaim();
        calculateClaims();
        entries.remove(0); // Remove address
    }

    private void getPercentageToClaim() throws IllegalArgumentException {
        String houseArea = entries.get(1).get(1);
        String businessArea = entries.get(2).get(1);
        int houseAreaAmount = houseArea.length() > 0 ? Integer.parseInt(houseArea) : 0;
        int businessAreaAmount = businessArea.length() > 0 ? Integer.parseInt(businessArea) : 0;

        if (houseAreaAmount > 0 && businessAreaAmount > 0) {
            percentageToClaim = (double) businessAreaAmount / (double) houseAreaAmount;
        } else {
            throw new IllegalArgumentException("Area of house and area used for business should not be blank.");
        }

        entries.get(3).add(String.valueOf(percentageToClaim));
    }

    private void calculateClaims() throws IllegalArgumentException {
        for (int i = 3; i < entries.size(); i++) {
            String value = entries.get(i).get(1);

            if (value.isEmpty()) {
                continue;
            }

            if (!value.matches(RegexUtilities.VALID_NUMBER_PATTERN)) {
                throw new IllegalArgumentException(String.format("%s is an invalid number..", value));
            }

            double claim = 0.0;
            try {
                double castedValue = RegexUtilities.extractNumber(value);
                totalValue += castedValue;
                entries.get(i).remove(1);
                entries.get(i).add(String.format("%.2f", castedValue));
                if (entries.get(i).get(0).equals("Phone / Internet")) {
                    claim = castedValue * .5;
                } else {
                    claim = castedValue * percentageToClaim;
                }
                totalClaims += claim;
                entries.get(i).add(String.format("%.2f", claim));
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException(String.format("Can't calculate. %s is an invalid number.", value));
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

        stringBuilder.append(String.format("Total,%.02f,%.02f", totalValue, totalClaims));

        return stringBuilder.toString();
    }
}
