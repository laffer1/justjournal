package com.justjournal.ctl;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

/**
 * Mitigation for spring4shell
 */
@ControllerAdvice
@Order(10000)
public class BinderControllerAdvise {
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        String [] denylist = new String[] {
                "class.*", "Class.*", "*.class.*", "*.Class.*"
        };

        dataBinder.setDisallowedFields(denylist);
    }
}
