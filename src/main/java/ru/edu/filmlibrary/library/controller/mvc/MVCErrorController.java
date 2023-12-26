package ru.edu.filmlibrary.library.controller.mvc;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import static jakarta.servlet.RequestDispatcher.ERROR_STATUS_CODE;
import static org.springframework.boot.web.servlet.support.ErrorPageFilter.ERROR_REQUEST_URI;


@Controller
@Slf4j
public class MVCErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest httpServletRequest,
                              Model model) {
        Object status = httpServletRequest.getAttribute(ERROR_STATUS_CODE);

        log.error("Ошибка! {}", status);
        model.addAttribute("exception",
                "Ошибка " + status +
                        " в маппинге " + httpServletRequest.getAttribute(ERROR_REQUEST_URI));

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/500";
            }
        }
        return "error/error";
    }
}
