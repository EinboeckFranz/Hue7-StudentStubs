package net.eaustria.webcrawler;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author bmayr
 */
public class WebCrawler6 implements ILinkHandler {

    private final Collection<String> visitedLinks = Collections.synchronizedSet(new HashSet<>());
    private String url;
    private ExecutorService execService;

    public WebCrawler6(String startingURL, int maxThreads) {
        this.url = startingURL;
        execService = Executors.newFixedThreadPool(maxThreads);
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

    private void startNewThread(String link) {
        LinkFinder linkFinder = new LinkFinder(link, this);
        execService.execute(linkFinder);
    }

    private void startCrawling() {
        startNewThread(this.url);
    }

    public static void main(String[] args) {
        new WebCrawler6("http://www.orf.at", 64).startCrawling();
    }

    @Override
    public void queueLink(String link) {
        startNewThread(link);
    }
}