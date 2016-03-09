package author_tool;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Keyword {

    private SimpleStringProperty keyword = new SimpleStringProperty("");
    private SimpleIntegerProperty count = new SimpleIntegerProperty();
    private SimpleDoubleProperty density = new SimpleDoubleProperty();

    public Keyword(String k) {
        keyword = new SimpleStringProperty(k);
        count = new SimpleIntegerProperty(0);
        density = new SimpleDoubleProperty(0.0);
    }

    public Keyword(String k, int c, double d, String de)
    {
        keyword = new SimpleStringProperty(k);
        count = new SimpleIntegerProperty(c);
        density = new SimpleDoubleProperty(d);
    }

    public String getKeyword() {
        return keyword.get();
    }

    public void setKeyword(String key) {
        keyword.set(key);
    }

    public SimpleStringProperty keywordProperty()
    {
        return keyword;
    }

    public int getCount() {
        return count.get();
    }

    public void setCount(int k) {
        count.set(k);
    }

    public SimpleIntegerProperty countProperty()
    {
        return count;
    }

    public double getDensity() {
        return density.get();
    }

    public void setDensity(int kC, int wC) {
        BigDecimal kCount = new BigDecimal(kC);
        BigDecimal wCount = new BigDecimal(wC);
        density.set(kCount.divide(wCount, 4, RoundingMode.CEILING).doubleValue());
    }

    public SimpleDoubleProperty densityProperty()
    {
        return density;
    }

    // Calculates keyword count (number of times keyword appears by itself in the content
    public int calcKeyData(Article t) {
        int size = this.getKeyword().length();
        int kCount = 0;
        String text = t.getText();
        for (int i = 0; i < (text.length() - size); i++) {
            if ((text.substring(i, i + size).equalsIgnoreCase(this.getKeyword()))) {
                if (i == 0 && (text.charAt(i + size) == '.'
                        || text.charAt((i + size)) == ','
                        || text.charAt((i + size)) == ' '
                        || text.charAt((i + size)) == '!'
                        || text.charAt((i + size)) == '?'
                        || text.charAt((i + size)) == '\''))
                {
                    kCount++;
                }
                if (i > 0 && (text.charAt(i - 1) == ' ' || text.charAt(i - 1) == '\n') && (text.charAt(i + size) == '.'
                        || text.charAt(i + size) == ','
                        || text.charAt(i + size) == ' '
                        || text.charAt(i + size) == '!'
                        || text.charAt(i + size) == '?'
                        || text.charAt(i + size) == '\'')) {
                    kCount++;
                }
            }
        }
        return kCount;
    }
}