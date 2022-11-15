import java.util.Set;

public class VatItem {
    private final String area;
    private final Set<String> codes;
    private final String type;
    private final float rate;

    public VatItem(String area, Set<String> codes, String type, float vatRate) {
        this.area = area;
        this.codes = codes;
        this.type = type;
        this.rate = vatRate;
    }

    public String getArea() {
        return area;
    }

    public Set<String> getCodes() {
        return codes;
    }

    public String getType() {
        return type;
    }

    public float getVatRate() {
        return rate;
    }
}
