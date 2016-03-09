package author_tool;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ArticleStats {
    private SimpleIntegerProperty wordCount = new SimpleIntegerProperty();
    private SimpleIntegerProperty totalWordCount = new SimpleIntegerProperty();
    private SimpleIntegerProperty sentenceCount = new SimpleIntegerProperty();
    private SimpleIntegerProperty paragraphCount = new SimpleIntegerProperty();
    private SimpleIntegerProperty letterCount = new SimpleIntegerProperty();
    private SimpleDoubleProperty coleman = new SimpleDoubleProperty();
    private SimpleDoubleProperty ari = new SimpleDoubleProperty();
    private SimpleDoubleProperty readingGradeLevel = new SimpleDoubleProperty();
    private SimpleDoubleProperty sentPerPara = new SimpleDoubleProperty();
    private SimpleDoubleProperty costPerWord = new SimpleDoubleProperty();
    private SimpleDoubleProperty articleCost = new SimpleDoubleProperty();

    public ArticleStats() {}

    // Increments word count
    private int calcWordCount(String t, int wc, int s) {
        if (t.charAt(s) >= 'a' && t.charAt(s) <= 'z') {
            wc++;
        }
        return wc;
    }

    public void setWordCount(int wc) {
        wordCount.set(wc);
    }

    public int getWordCount() {
        return wordCount.get();
    }

    public SimpleIntegerProperty wordCountProperty() {
        return wordCount;
    }

    public void setTotalWordCount(int twc) { totalWordCount.set(twc); }

    public int getTotalWordCount() { return totalWordCount.get(); }

    public SimpleIntegerProperty totalWordCountProperty() { return totalWordCount; }

    // Increments count for number of sentences in content
    private int calcSentenceCount(String t, int sc, int i, int s) {
        if (Character.isWhitespace(t.charAt(i))) {
            if (t.charAt(i - 1) == '.' || t.charAt(i - 1) == '!'
                    || t.charAt(i - 1) == '?') {
                sc++;
            }
        } else if (s < t.length() && i == t.length() - 1) {
            if (t.charAt(i) == '.' || t.charAt(i) == '!' || t.charAt(i) == '?') {
                sc++;
            }
        }
        return sc;
    }

    public void setSentenceCount(int sc) {
        sentenceCount.set(sc);
    }

    public int getSentenceCount() {
        return sentenceCount.get();
    }

    public SimpleIntegerProperty sentenceCountProperty() {
        return sentenceCount;
    }

    public void calcParagraphCount(ObservableList<CharSequence> para) {
        int pCount = 0;
        for (CharSequence p : para) {
            if (p.length() != 0) {
                pCount++;
            }
        }
        paragraphCount.set(pCount);
    }

    public void setParagraphCount(int pc) {
        paragraphCount.set(pc);
    }

    public int getParagraphCount() {
        return paragraphCount.get();
    }

    public SimpleIntegerProperty paragraphCountProperty() {
        return paragraphCount;
    }

    // Increments count for number of letters in content
    private int calcLetterCount(String text, int letterCount, int index) {
        if (text.charAt(index) >= 'a' && text.charAt(index) <= 'z') {
            letterCount++;
        }
        return letterCount;
    }

    public void setLetterCount(int lc) { letterCount.set(lc); }

    public int getLetterCount() {
        return letterCount.get();
    }

    public SimpleIntegerProperty letterCountProperty() {
        return letterCount;
    }

    // Calculates the Coleman-Liau Grade Level Reading Index
    private void calcColeman() {
        if (getWordCount() > 0) {
            coleman.set(0.0588 * ((double) getLetterCount() / getWordCount() * 100) - 0.296 *
                    ((double) getSentenceCount() / getWordCount() * 100) - 15.8);
        }
    }

    public double getColeman() {
        return coleman.get();
    }

    public SimpleDoubleProperty colemanProperty() {
        return coleman;
    }

    // Calculates the Automated Reading Index
    private void calcAri() {
        if (getWordCount() > 0 && getSentenceCount() > 0) {
            ari.set(4.71 * (getLetterCount() / getWordCount()) + 0.5 * (getWordCount() / getSentenceCount()) - 21.43);
        }
    }

    public double getAri() {
        return ari.get();
    }

    public SimpleDoubleProperty ariProperty() {
        return ari;
    }

    // Calculates average grade reading level using Coleman-Liau and ARI
    public void calcReadingGradeLevel() {
        BigDecimal ari = new BigDecimal(getAri());
        BigDecimal cole = new BigDecimal(getColeman());
        BigDecimal two = new BigDecimal("2");
        Double gradeLevel = ((ari.add(cole)).divide(two, 2, RoundingMode.CEILING)).doubleValue();
        if (gradeLevel < 0) {
            readingGradeLevel.set(0.0);
        } else {
            readingGradeLevel.set(gradeLevel);
        }
    }

    public double getReadingGradeLevel() { return readingGradeLevel.get(); }

    public SimpleDoubleProperty readingGradeLevelProperty() { return readingGradeLevel; }

    // Calculates number of sentences per paragraph
    public void calcSpp() {
        if (getParagraphCount() > 0) {
            sentPerPara.set((double) getSentenceCount() / getParagraphCount());
        }
    }

    public double getSpp() {
        return sentPerPara.get();
    }

    public SimpleDoubleProperty sppProperty() {
        return sentPerPara;
    }

    public void setCostPerWord (double cpw) {
        costPerWord.set(cpw);
    }

    public double getCostPerWord() {
        return costPerWord.get();
    }

    public SimpleDoubleProperty cpwProperty() { return costPerWord; }

    public void setArticlePrice(double sap) { articleCost.set(sap); }

    public double getArticlePrice() { return articleCost.get(); }

    public SimpleDoubleProperty apProperty() { return articleCost; }

    // Sets all relevant article data
    public void calcArticleData(String text, String title, String mTitle, String mDesc) {
        int wc = 0, twc = 0, lc = 0, sc = 0, start = 0;
        text = text.toLowerCase();
        title = title.toLowerCase();
        mTitle = mTitle.toLowerCase();
        mDesc = mDesc.toLowerCase();

        for (int index = 0; index < text.length(); index++)
        {
            lc = calcLetterCount(text, lc, index);
            if (Character.isWhitespace(text.charAt(index))) {
                wc = calcWordCount(text, wc, start);
                sc = calcSentenceCount(text, sc, index, start);
                start = index + 1;
            }
            if (start < text.length() && index == text.length() - 1) {
                wc = calcWordCount(text, wc, start);
                sc = calcSentenceCount(text, sc, index, start);
            }
        }

        this.setLetterCount(lc);
        this.setWordCount(wc);
        this.setSentenceCount(sc);
        twc = this.getWordCount() + traverseText(title) + traverseText(mTitle) + traverseText(mDesc);
        this.setTotalWordCount(twc);
        calcSpp();
        calcColeman();
        calcAri();
        calcReadingGradeLevel();
    }

    // Traverses text to count number of words in titles and META
    public int traverseText(String text) {
        int localWordCount = 0, countStart = 0;
        for (int i = 0; i < text.length(); i++) {
            if (Character.isWhitespace(text.charAt(i))) {
                localWordCount = calcWordCount(text, localWordCount, countStart);
                countStart = i + 1;
            }
            if (countStart < text.length() && i == text.length() - 1) {
                localWordCount = calcWordCount(text, localWordCount, countStart);
            }
        }
        return localWordCount;
    }
}
