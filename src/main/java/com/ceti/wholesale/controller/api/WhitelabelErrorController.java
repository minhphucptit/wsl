package com.ceti.wholesale.controller.api;

import com.ceti.wholesale.common.error.WhitelabelErrorException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WhitelabelErrorController implements ErrorController {

    @RequestMapping("/error")
    public void handleError() {
        throw new WhitelabelErrorException();
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
