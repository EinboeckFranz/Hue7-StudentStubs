package net.eaustria.webcrawler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ForkJoinPool;

/**
 * @author bmayr
 */
public class WebCrawler7 implements ILinkHandler {

    private final Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<>());
    private String url;
    private ForkJoinPool mainPool;

    public WebCrawler7(String startingURL, int maxThreads) {
        this.url = startingURL;
        mainPool = new ForkJoinPool(maxThreads);
    }

    private void startCrawling() {
        LinkFinderAction linkFinderAction = new LinkFinderAction(url, this);
        mainPool.invoke(linkFinderAction);
    }

    @Override
    public int size() {
        return visitedLinks.size();
    }

    @Override
    public void addVisited(String s) {
        visitedLinks.add(s);
    }

    @Override
    public boolean visited(String s) {
        return visitedLinks.contains(s);
    }

    public static void main(String[] args) {
        new WebCrawler7("http://www.orf.at", 64).startCrawling();
    }

    //Just override - we do not need this when using forkJoinPool
    @Override
    public void queueLink(String link) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

