package com.jos.dem.webclient;

import static java.nio.charset.StandardCharsets.UTF_8;

import org.springframework.util.Base64Utils;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.io.IOException;

@SpringBootApplication
@PropertySource("classpath:application.properties")
public class WebClientApplication {

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Value("${github.api.url}")
  private String githubApiUrl;
  @Value("${username}")
  private String username;
  @Value("${token}")
  private String token;

    private byte[] base64Decode(String s) {
        try {
            BASE64Decoder d = new BASE64Decoder();
            return d.decodeBuffer(s);
        } catch (IOException e) {throw new RuntimeException(e);}
    }

  @Bean
  public WebClient webClient() {
    

    if ( githubApiUrl==null || githubApiUrl.isEmpty() )
    {
      String s = "Github Api Url is empty. Please specify 'github.api.url' system property.";
      log.info(s);
      githubApiUrl="https://api.github.com/";
    }

    log.info("Github Api Url: [" + githubApiUrl + "]");
    log.info("Token: [" + token + "]");

    byte[] bytes = base64Decode(token);

    token = new String(bytes, UTF_8);

    return WebClient
      .builder()
        .baseUrl(githubApiUrl)
        .defaultHeader("Authorization", "Basic " + Base64Utils
          .encodeToString((username + ":" + token).getBytes(UTF_8)))
      .build();
  }

	public static void main(String[] args) {
		SpringApplication.run(WebClientApplication.class, args);
	}

}
