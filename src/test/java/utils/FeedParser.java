package utils;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import domain.Entry;
import domain.Feed;

import java.io.IOException;
import java.io.InputStream;

public class FeedParser {

    private XStream xStream;

    public FeedParser() {
        xStream = new XStream(new DomDriver());
        xStream.alias("feed", Feed.class);
        xStream.alias("entry", Entry.class);
        xStream.ignoreUnknownElements();
        xStream.addImplicitCollection(Feed.class, "entries", "entry", Entry.class);
    }

    public Feed parse(InputStream inputStream) throws IOException {
        try (InputStream stream = inputStream) {
            return (Feed) xStream.fromXML(stream);
        }
    }
}
