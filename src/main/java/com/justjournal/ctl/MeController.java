package com.justjournal.ctl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * This tells the server that it can use the token to access a "/me"
 * endpoint and use that to derive authentication information
 * @author Lucas Holt
 */
@RestController
public class MeController {
    @RequestMapping("/me")
      public Principal user(Principal user) {
        return user;
      }
}
