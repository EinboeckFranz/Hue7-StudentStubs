package net.eaustria.webcrawler;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import static net.eaustria.webcrawler.STATIC.printErrorMSG;

/**
 * @author bmayr
 */

// Recursive Action for forkJoinFramework from Java7
public class LinkFinderAction extends RecursiveAction {
    private ILinkHandler cr;
    private String url;
    /**
     * Used for statistics
     */
    private static final long t0 = System.nanoTime();
    static boolean found1500Links = false;
    static boolean found3000Links = false;

    public LinkFinderAction(String url, ILinkHandler cr) {
        this.url = url;
        this.cr = cr;
    }

    @Override
    public void compute() {
        int foundLinks = cr.size();
        if((foundLinks == 1500 && !found1500Links) || (foundLinks == 3000 && !found3000Links)) {
            STATIC.printStatistics(foundLinks, t0);
            if(foundLinks == 1500) found1500Links = true;
            else found3000Links = true;
        }

        if(!cr.visited(url)) {
            List<LinkFinderAction> newLinkFinders = new LinkedList<>();
            try {
                try {
                    Document htmlDoc = Jsoup.connect(url).get();
                    Elements links = htmlDoc.select("a[href]");
                    for (Element link : links) {
                        if (link.attr("href").startsWith("http"))
                            newLinkFinders.add(new LinkFinderAction(link.attr("href"), cr));
                    }
                    while(newLinkFinders.contains(null))
                        newLinkFinders.remove(null);
                    invokeAll(newLinkFinders);
                } catch (HttpStatusException httpStatusException) {
                    //printErrorMSG(httpStatusException.getUrl() + " resulted a not OK-HTTP response!");
                } catch (UnknownHostException unknownHostException) {
                    //printErrorMSG("The IP Address of host " + unknownHostException.getMessage() + " could not be resolved!");
                } catch (UnsupportedMimeTypeException unsupportedMimeTypeException) {
                    //printErrorMSG(unsupportedMimeTypeException.getUrl() + " contained an unsupported Mime-Type!");
                } catch (SSLHandshakeException sslHandshakeException) {
                    //printErrorMSG("SSL Error: " + sslHandshakeException.getMessage() + " certification path was not valid!");
                } catch (ConnectException connectException) {
                    //printErrorMSG("An connection timed out.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                cr.addVisited(url);
            }
        }
    }
}