package ru.edu.filmlibrary.library.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.edu.filmlibrary.library.service.UsersService;
import ru.edu.filmlibrary.library.utils.MailUtils;

import java.util.List;

@Component
@Slf4j
public class MailScheduler {

    private final UsersService usersService;
    private final JavaMailSender javaMailSender;

    public MailScheduler(UsersService usersService, JavaMailSender javaMailSender) {
        this.usersService = usersService;
        this.javaMailSender = javaMailSender;
    }

    @Scheduled(cron = "0 0 6 * * ?") // каждый день в 6 утра
    public void sentMailsToDebtors() {
        log.info("Запуск планировщика по проверки должников....");
        List<String> emails = usersService.getUserEmailsWithDelayedRentDate();
        if (emails.size() > 0) {
            SimpleMailMessage simpleMailMessage = MailUtils.createMailMessage(
                    emails,
                    "Напоминание о просрочке фильма(ов)",
                    "За тобой выехали"
            );
            javaMailSender.send(simpleMailMessage);
        }
    }
}
