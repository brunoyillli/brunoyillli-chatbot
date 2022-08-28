package io.github.brunoyillli.brunoyilllichatbot;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
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
				if(!message.getText().isBlank()) {
					System.out.println("Executando comando para " + 
							update.getMessage().getChatId().toString());
					execute(message);
				}
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		} else if (update.hasMessage() && update.getMessage().hasPhoto()) {
			long chat_id = update.getMessage().getChatId();

			List<PhotoSize> photos = update.getMessage().getPhoto();
			String f_id = photos.stream().sorted(Comparator.comparing(PhotoSize::getFileSize).reversed()).findFirst()
					.orElse(null).getFileId();
			int f_width = photos.stream().sorted(Comparator.comparing(PhotoSize::getFileSize).reversed()).findFirst()
					.orElse(null).getWidth();
			int f_height = photos.stream().sorted(Comparator.comparing(PhotoSize::getFileSize).reversed()).findFirst()
					.orElse(null).getHeight();
			String caption = "file_id: " + f_id + "\nwidth: " + Integer.toString(f_width) + "\nheight: "
					+ Integer.toString(f_height);
			SendPhoto msg = new SendPhoto();
			msg.setChatId(chat_id);
			msg.setPhoto(new InputFile(f_id));
			msg.setCaption(caption);
			sendPhoto(msg);
		}
	}

	private SendMessage responder(Update update) {
		String textoMensagem = update.getMessage().getText().toLowerCase();
		String charId = update.getMessage().getChatId().toString();
		FunctionalityBot functionalityBot = new FunctionalityBot();
		String resposta = " ";
		sendMsg(update.getMessage().getChatId().toString(), textoMensagem, update);

		if ("/data".equals(textoMensagem)) {
			resposta = functionalityBot.getData();
		} else if (textoMensagem.startsWith("/hora")) {
			resposta = functionalityBot.getHora();
		} else if (textoMensagem.startsWith("/oi")) {
			resposta = "\uD83E\uDD16  Olá voce esta usando o melhor bot do planeta terra.";
		} else if (textoMensagem.contains("quem é você ?")) {
			resposta = "\uD83E\uDD16  Eu sou um bot!!";
		} else if (textoMensagem.equals("/help")) {
			resposta = "Utilize os seguintes comandos: "
					+ "\n/data\n/oi\n/hora"
					+ "\n/button para selecionar uma opção\nquem é você ?"
					+ "\n/kaneki\n/clearbutton";
		} else if (textoMensagem.equals("/button")) {
			ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
			SendMessage mensagem = new SendMessage();
			mensagem.setChatId(charId);
			mensagem.setText("Selecione uma das caixas");
			List<KeyboardRow> keyboard = new ArrayList<>();
			KeyboardRow row = new KeyboardRow();
			row.add("azul");
			row.add("verde");
			row.add("/kaneki");
			keyboard.add(row);

			row = new KeyboardRow();
			row.add("kiwi");
			row.add("maçã");
			row.add("uva");
			keyboard.add(row);

			keyboardMarkup.setKeyboard(keyboard);
			mensagem.setReplyMarkup(keyboardMarkup);
			sendText(mensagem);
		} else if (textoMensagem.equals("/kaneki")) {
			SendPhoto msg = new SendPhoto();
			msg.setChatId(charId);
			msg.setPhoto(new InputFile(
					"AgACAgEAAxkBAAPmYwug-A-hNgI96vsqkkDL9gSDrLAAAjOrMRtZX2FEhHvs0MK0cOUBAAMCAAN3AAMpBA"));
			msg.setCaption("Kaneki Branco");
			sendPhoto(msg);
		}else if (textoMensagem.equals("/clearbutton")) {
            SendMessage msg = new SendMessage();
            msg.setChatId(charId);
            msg.setText("Keyboard removido use /button para aparecer novas opções");
            ReplyKeyboardRemove keyboardMarkup = new ReplyKeyboardRemove();
            keyboardMarkup.setSelective(true);
            keyboardMarkup.setRemoveKeyboard(true);
            msg.setReplyMarkup(keyboardMarkup);
            sendText(msg);
		}
		else {
			resposta = "\nDigite /help para ver os comandos disponiveis.";
		}

		return SendMessage.builder().text(resposta).chatId(charId).build();
	}

	private void sendText(SendMessage msg) {
		System.out.println("Executando comando para " + msg.getChatId());
		try {
			execute(msg);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}
	
	private void sendPhoto(SendPhoto msg) {
		System.out.println("Enviando a foto ...");
		try {
			execute(msg);
		} catch (TelegramApiException e) {
			e.printStackTrace();
			System.out.println("Problemas para enviar a foto...");
		}
		System.out.println("Foto enviada com sucesso");
	}
	
	public synchronized void sendMsg(String chatId, String message, Update update) {

		if ("/start".equals(message)) {
			String firstName = update.getMessage().getFrom().getFirstName();
			SendMessage response = new SendMessage();
			response.enableMarkdown(true);
			response.setChatId(chatId);
			response.setText("Olá, " + firstName + ", Olá seja bem vindo!");

			try {
				execute(response);
			} catch (TelegramApiException e) {
				e.printStackTrace();
			}
		}
	}
}
