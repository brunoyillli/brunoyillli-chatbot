package io.github.brunoyillli.brunoyilllichatbot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FunctionalityBot {
	
	public String getData() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyy");
		return "A data atual: " + simpleDateFormat.format(new Date());
	}
	
	public String getHora() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
		return "A hora atual: " + simpleDateFormat.format(new Date());
	}
}
