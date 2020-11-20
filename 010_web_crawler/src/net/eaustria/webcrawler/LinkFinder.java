package net.eaustria.webcrawler;

/**
 * @author bmayr
 */

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLHandshakeException;
import java.net.ConnectException;
import java.net.UnknownHostException;

import static net.eaustria.webcrawler.STATIC.printErrorMSG;

public class LinkFinder implements Runnable {
    private ILinkHandler linkHandler;
    private String url;
    /**
     * Used fot statistics
     */
    private static final long t0 = System.nanoTime();
    static boolean found1500Links = false;
    static boolean found3000Links = false;

    public LinkFinder(String url, ILinkHandler handler) {
        this.url = url;
        this.linkHandler = handler;
    }

    @Override
    public void run() {
        getSimpleLinks(url);
    }

    private void getSimpleLinks(String url) {
        int foundLinks = linkHandler.size();
        if((foundLinks == 1500 && !found1500Links) || (foundLinks == 3000 && !found3000Links)) {
            STATIC.printStatistics(foundLinks, t0);
            if(foundLinks == 1500) found1500Links = true;
            else found3000Links = true;
        }

        if(!linkHandler.visited(url)) {
            try {
                try {
                    Document htmlDoc = Jsoup.connect(url).get();
                    Elements links = htmlDoc.select("a[href]");
                    for (Element link : links) {
                        if(link.attr("href").startsWith("http"))
                            linkHandler.queueLink(link.attr("href"));
                    }
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
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                linkHandler.addVisited(url);
            }
        }
    }
}

