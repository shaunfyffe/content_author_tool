package author_tool;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Article {

    private SimpleStringProperty text = new SimpleStringProperty();
    private SimpleStringProperty title = new SimpleStringProperty();
    private SimpleStringProperty metaTitle = new SimpleStringProperty();
    private SimpleStringProperty metaDescription = new SimpleStringProperty();

    public Article() {}

    public Article(String t)
    {
        text = new SimpleStringProperty(t);
    }

    public void setText(String t)
    {
        text.set(t);
    }

    public String getText()
    {
        return text.get();
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setTitle(String t) { title.set(t); }

    public String getTitle() { return title.get(); }

    public StringProperty titleProperty() { return title; }

    public void setMetaTitle(String mt) { metaTitle.set(mt); }

    public String getMetaTitle() { return metaTitle.get(); }

    public StringProperty metaTitleProperty() { return metaTitle; }

    public void setMetaDescription(String md) { metaDescription.set(md); }

    public String getMetaDescription() { return metaDescription.get(); }

    public StringProperty metaDescriptionProperty() { return metaDescription; }
}
