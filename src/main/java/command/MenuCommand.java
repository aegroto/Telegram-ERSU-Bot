
package command;

import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import parser.ParserMenu;
import service.RegisterID;

public class MenuCommand extends BotCommand { 
    public MenuCommand() {
        super("menu", "Invia il menù giornaliero della mensa");
    }

    @Override
    public void execute(AbsSender as, User user, Chat chat, String[] strings) {
        SendMessage message = new SendMessage()
                        .setChatId(chat.getId().toString())
                        .setParseMode(ParseMode.HTML)
                        .setText(ParserMenu.getInstance().getMenu());
        if (chat.isUserChat()) message.setReplyMarkup(bot.Bot.generateRKM());
        RegisterID.write(chat.getId().toString());
        try {
            as.execute(message);
        } catch (TelegramApiException ex) {
            org.apache.log4j.Logger.getLogger(MenuCommand.class).error("Errore invio comando menu", ex);
        }
    }
    
}
