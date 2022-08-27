package io.github.brunoyillli.brunoyilllichatbot;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import io.github.brunoyillli.brunoyilllichatbot.config.DadosBot;
import io.github.brunoyillli.brunoyilllichatbot.util.FunctionalityBot;

public class EchoBot extends TelegramLongPollingBot {

	@Override
	public String getBotUsername() {
		return DadosBot.BOT_USERNAME;
	}

	@Override
	public String getBotToken() {
		return DadosBot.BOT_TOKEN;
	}

	@Override
	public void onUpdateReceived(Update update) {
		if (update.hasMessage() && update.getMessage().hasText()) {
			SendMessage message = responder(update);
			try {
				execute(message);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}

	private SendMessage responder(Update update) {
		String textoMensagem = update.getMessage().getText().toLowerCase();
		String charId = update.getMessage().getChatId().toString();
		FunctionalityBot functionalityBot = new FunctionalityBot();
		String resposta = " ";

		if ("/data".equals(textoMensagem)) {
			resposta = functionalityBot.getData();
		} else if (textoMensagem.startsWith("/hora")) {
			resposta = functionalityBot.getHora();
		} else if (textoMensagem.startsWith("/oi")) {
			resposta = "\uD83E\uDD16  Olá voce esta usando o melhor bot do planeta terra.";
		} else if (textoMensagem.contains("quem é você ?")) {
			resposta = "\uD83E\uDD16  Eu sou um bot!!";
		} else if (textoMensagem.equals("/help")) {
			resposta = "Utilize os seguintes comandos: \n/data\n/oi\n/hora\n/rows\nquem é você ?";
		} else if (textoMensagem.equals("/rows")) {
            SendMessage mensagem = new SendMessage();
            mensagem.setChatId(charId);
            mensagem.setText("Selecione uma das caixas");
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboard = new ArrayList<>();
            KeyboardRow row = new KeyboardRow();
            row.add("azul");
            row.add("verde");
            row.add("vermelho");
            keyboard.add(row);
            
            row = new KeyboardRow();
            row.add("kiwi");
            row.add("maçã");
            row.add("uva");
            keyboard.add(row);
            
            keyboardMarkup.setKeyboard(keyboard);
            mensagem.setReplyMarkup(keyboardMarkup);
            return mensagem;
		}else {
			resposta = "\nDigite /help para ver os comandos disponiveis.";
		}

		return SendMessage.builder().text(resposta).chatId(charId).build();
	}

}
