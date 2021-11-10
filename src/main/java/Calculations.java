import java.util.List;

public class Calculations {

    private double percentageToClaim;
    private List<List<String>> entries;

    public Calculations(List<List<String>> entries) {
        this.entries = entries;
        getPercentageToClaim();
        calculateClaims();
    }

    private void getPercentageToClaim() {
        String houseArea = entries.get(1).get(1);
        String businessArea = entries.get(2).get(1);
        int houseAreaAmount = houseArea.length() > 0 ? Integer.parseInt(houseArea) : 0;
        int businessAreaAmount = businessArea.length() > 0 ? Integer.parseInt(businessArea) : 0;

        if (houseAreaAmount > 0 && businessAreaAmount > 0) {
            percentageToClaim = (double) businessAreaAmount / (double) houseAreaAmount;
        } else {
            throw new IllegalArgumentException("Area of house and area used for business should not be blank.");
        }
    }

    private void calculateClaims() {
        for (int i = 3; i < entries.size(); i++) {
            String value = entries.get(i).get(1);

            if (value.isEmpty()) {
                continue;
            }

            double claim = 0.0;
            int castedValue = Integer.parseInt(value);
            if (entries.get(i).get(0).equals("Phone / Internet")) {
                claim = castedValue * .5;
            } else {
                claim = castedValue * percentageToClaim;
            }
            entries.get(i).add(String.valueOf(claim));
        }
    }

    public List<List<String>> getEntriesWithClaims() {
        return entries;
    }
    
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (List<String> row : entries) {
            for (String value : row) {
                if (value.isEmpty()) {
                    continue;
                }
                stringBuilder.append(value).append(",");
            }
            stringBuilder.setCharAt(stringBuilder.length() - 1, '\n');
        }

        return stringBuilder.toString();
    }
}
