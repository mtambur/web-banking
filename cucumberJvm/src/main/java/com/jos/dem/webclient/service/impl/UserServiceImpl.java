package com.jos.dem.webclient.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import reactor.core.publisher.Flux;

import com.jos.dem.webclient.model.SSHKey;
import com.jos.dem.webclient.model.PublicEmail;
import com.jos.dem.webclient.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@PropertySource("classpath:application.properties")
public class UserServiceImpl implements UserService {

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Autowired
  private WebClient webClient;

  @Value("${username}")
  private String username;

  public Flux<SSHKey> getKeys() {
    log.info("Username: [" + username + "]");
    return webClient.get().uri("users/" + username + "/keys").retrieve()
    .bodyToFlux(SSHKey.class);
  }

  public Flux<PublicEmail> getEmails() {
    return webClient.get().uri("user/public_emails").retrieve()
    .bodyToFlux(PublicEmail.class);
  }

}
