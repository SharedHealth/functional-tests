package domain;


import java.util.ArrayList;
import java.util.List;

public class Feed {

    private List<Entry> entries = new ArrayList<>();

    public List<Entry> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry> entries) {
        this.entries = entries;
    }

    public boolean hasMoreEntriesThan(Feed previous) {
        return entries.size() > previous.getEntries().size() || entries.size() == 1 /*A new page has been created*/;
    }
}
