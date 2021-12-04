package advisor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewRelease {
    private String title;
    private List<String> authors = new ArrayList<>();
    private String href;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return title + "\n"
                + authors.toString() + "\n"
                + href + "\n";
    }
}
