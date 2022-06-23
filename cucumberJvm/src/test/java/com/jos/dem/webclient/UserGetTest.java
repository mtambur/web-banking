package com.jos.dem.webclient;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jos.dem.webclient.model.SSHKey;
import com.jos.dem.webclient.model.PublicEmail;

import java.util.List;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import reactor.core.publisher.Flux;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserGetTest extends UserIntegrationTest {

  private List<SSHKey> keys;

  private Logger log = LoggerFactory.getLogger(this.getClass());

  @Before
  public void setup() {
    log.info("Before any test execution");
  }

  @Then("^User gets his SSH keys$")
  public void shouldGetKeys() throws Exception {
    log.info("Running: User gets his SSH keys");
    List<SSHKey> keys = getKeys()
      .collectList()
      .block();
    assertEquals(0, keys.size(),  () -> "Should be 0 keys");
  }

  @Then("^User gets his public emails$")
  public void shouldGetEmails() throws Exception {
    log.info("Running: User gets his public emails");
    List<PublicEmail> emails = getEmails()
      .collectList()
      .block();
    log.info("Emails Size: [" + emails.size() + "]");
    log.info("Emails : [" + emails + "]");
    
    PublicEmail email = null;
    String emailAddress = "howard.hamlin.howard@gmail.com";
    for(int i=0; i<emails.size(); i++) {
        email = emails.get(i);
        if ( email.getEmail() == emailAddress )
          break;
    }

    log.info("Public Email: [" + email.getEmail() + "]");
    log.info("Email Verified: [" + email.getVerified() + "]");
    log.info("Email Primary: [" + email.getPrimary() + "]");
    log.info("Email Visibility: [" + email.getVisibility() + "]");

    assertTrue(emails.size() == 4, "Should be 4 emails");
    assertEquals("howard.hamlin.howard@gmail.com", email.getEmail(), "Should contains howard's email");
    assertTrue(email.getVerified(), "Should be verified");
    assertTrue(email.getPrimary(), "Should be primary");
    assertEquals("public", email.getVisibility(), "Should be public");
  }

  @After
  public void tearDown() {
    log.info("After all test execution");
  }

}
