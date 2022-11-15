public class CurrencyItem {
    private final String code;
    private final float rate;

    public CurrencyItem(String code, float rate) {
        this.code = code;
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public float getRate() {
        return rate;
    }
}
