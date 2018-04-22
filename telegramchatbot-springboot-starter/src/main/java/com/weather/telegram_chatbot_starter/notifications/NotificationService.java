package com.weather.telegram_chatbot_starter.notifications;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

@Service
public class NotificationService {

	final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	private TelegramBot bot;
	
	public NotificationService(TelegramBot bot) {
		this.bot = bot;
		
	}

	public void notifyOnceADay(int chatId) {

		int hours = 15;
		int minutes = 34;

		final Runnable beeper = new Runnable() {
			public void run() {
				System.out.println("beep");
				SendMessage sendMessage = new SendMessage(chatId, "Notification").parseMode(ParseMode.HTML)
						.disableNotification(false).replyMarkup(new ForceReply());
				bot.execute(sendMessage);
			}
		};

		LocalDateTime localNow = LocalDateTime.now();
		ZoneId currentZone = ZoneId.of("Europe/Bucharest");
		ZonedDateTime zonedNow = ZonedDateTime.of(localNow, currentZone);
		ZonedDateTime zonedNext5;
		zonedNext5 = zonedNow.withHour(hours).withMinute(minutes).withSecond(0);
		if (zonedNow.compareTo(zonedNext5) > 0)
			zonedNext5 = zonedNext5.plusDays(1);

		Duration duration = Duration.between(zonedNow, zonedNext5);
		long initalDelay = duration.getSeconds();

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(beeper, initalDelay, 24 * 60 * 60, TimeUnit.SECONDS);
		
	}
}
