package job;

import bot.Bot;
import bot.YmlResolver;
import java.io.File;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import parser.ParserMenu;
import parser.Scraper;

public class JobMensa extends TimerTask {
    
    private final String PATH = YmlResolver.getInstance().getValue("path_mensa");
    
    private Bot bot;
    private final ParserMenu p = new ParserMenu(PATH);
    
    public JobMensa(Bot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        String textMenu = p.getMenu(); 
        if (textMenu.contains("mensa non disponibile")) return; 
        try {
            SendMessage message = new SendMessage()
                    .setChatId(YmlResolver.getInstance().getValue("mensa_channel"))
                    .setText(textMenu)
                    .enableHtml(true);
            bot.execute(message);
        } catch (TelegramApiException ex) {
            org.apache.log4j.Logger.getLogger(Scraper.class).error("Errore invio menù canale", ex);
        }
             
    }
    
}
