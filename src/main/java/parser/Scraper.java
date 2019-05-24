package parser;

import bot.Bot;
import bot.YmlResolver;
import java.io.IOException;
import java.util.TimerTask;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Scraper extends TimerTask {

    private Document doc;
    private Bot bot;

    public Scraper(Bot bot) {
        this.bot = bot;
        try {
            doc = Jsoup.connect("http://www.ersucatania.gov.it/blog/").get();
        } catch (IOException ex) {
            System.err.println("Connessione sito ersu fallita");
        }
    }

    public void checkNews() throws TelegramApiException {
        final String content = FileManager.read(); // Deve 
        Elements elements = doc.getElementsByClass("half");
        for (Element e1 : elements) {
            String link = e1.child(0).attributes().get("href");
            if (!content.contains(link)) {
                bot.execute(
                        new SendMessage()
                        .enableHtml(true)
                        .setChatId(YmlResolver.getInstance().getValue("news_channel"))
                        .setText(getNews(link))
                );
            }
        }
    }

    private String getNews(String link) {

        Document news = null;
        try {
            news = Jsoup.connect(link).get();
        } catch (IOException ex) {
            // Logging
        }
        if (news == null) {
            return "Errore creazione news";
        }
        FileManager.write(link);
        return getContentNews(news);

    }

    private String getContentNews(Document doc) {
        String section = doc.getElementsByAttributeValueContaining("property", "article:section").attr("content");
        String title = doc.getElementsByAttributeValueContaining("property", "og:title").attr("content");
        String description = doc.getElementsByAttributeValueContaining("property", "og:description").attr("content");
        String url = doc.getElementsByAttributeValueContaining("property", "og:url").attr("content");

        String news = "<b>[" + section + "]</b>" + "\n"
                + url + "\n\n"
                + "<b>" + title + "</b>" + "\n"
                + description;

        return news;
    }

    @Override
    public void run() {
        try {
            checkNews();
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

}