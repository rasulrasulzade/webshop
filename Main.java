import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


public class Main {
    private final Set<VatItem> vatSet = new HashSet<>();
    private final Set<CurrencyItem> currencies = new HashSet<>();

    public Main() {
        fillVatSet();
        fillCurrencies();
    }

    private void fillVatSet() {
        vatSet.add(new VatItem("Scandinavia", new HashSet<>(Arrays.asList("DK", "NO", "SE")), "All", 25));
        vatSet.add(new VatItem("Great Britain", Collections.singleton("GB"), "All", 20));
        vatSet.add(new VatItem("Germany", Collections.singleton("DE"), "Online", 19));
        vatSet.add(new VatItem("Germany", Collections.singleton("DE"), "Book", 12));
    }

    private void fillCurrencies() {
        currencies.add(new CurrencyItem("DKK", 100));
        currencies.add(new CurrencyItem("NOK", 73.50f));
        currencies.add(new CurrencyItem("SEK", 70.23f));
        currencies.add(new CurrencyItem("GBP", 891.07f));
        currencies.add(new CurrencyItem("EUR", 743.93f));
    }

    public Double getFreight(Integer count) {
        //find number of decimals
        double decimalCount = Math.ceil(count / 10);
        if (decimalCount == 0) return 0.0;
        //freight calculation
        return 75 + (decimalCount - 1) * 25;
    }

    public BigDecimal calculateWithVat(BigDecimal amount, String code, String type) {
        VatItem vatItem = vatSet.stream()
                .filter(el -> el.getCodes().contains(code) && (el.getType().equals("All") || el.getType().equals(type)))
                .findFirst()
                .orElse(null);
        if (vatItem == null) return amount;
        return amount.multiply(BigDecimal.valueOf((vatItem.getVatRate() / 100) + 1));
    }

    public BigDecimal convertToCurrency(BigDecimal amount, String code) {
        CurrencyItem currencyItem = currencies.stream().filter(el -> el.getCode().equals(code)).findFirst().orElse(null);
        if (currencyItem == null) return amount;
        return amount.multiply(new BigDecimal("100")).divide(BigDecimal.valueOf(currencyItem.getRate()), 2, RoundingMode.UP);
    }

    public String getValue(String[] args, String key) {
        String value = "";
        for (String arg : args) {
            String[] parts = arg.split("=");
            if (parts[0].replace("--", "").equals(key)) return parts[1];
        }
        return value;
    }


    public static void main(String[] args) {

        Main obj = new Main();
        int count = Integer.parseInt(args[0]);
        BigDecimal price = new BigDecimal(args[1]);
        String productType = args[2];
        String vatCode = obj.getValue(args, "vat");
        String inputCurrency = obj.getValue(args, "input-currency");
        String outputCurrency = obj.getValue(args, "output-currency");

        Double freight = obj.getFreight(count);
        BigDecimal totalPrice = price.multiply(BigDecimal.valueOf(count)).add(BigDecimal.valueOf(freight));
        if (!"".equals(vatCode))
            totalPrice = obj.calculateWithVat(totalPrice, vatCode, productType);

        if (!"".equals(outputCurrency))
            totalPrice = obj.convertToCurrency(totalPrice, outputCurrency);
        System.out.println(totalPrice);
    }
}