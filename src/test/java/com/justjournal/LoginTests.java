/*
 * Copyright (c) 2003-2021 Lucas Holt
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.justjournal;

import static com.justjournal.core.Constants.BAD_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.justjournal.model.User;
import com.justjournal.repository.UserRepository;
import java.security.NoSuchAlgorithmException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/** @author Lucas Holt */
@RunWith(MockitoJUnitRunner.class)
public class LoginTests {

  @Mock UserRepository userRepository;

  @InjectMocks private Login login;

  @Test
  public void testIsPasswordLowercaseOnly() {
    boolean result = Login.isPassword("abcdefg");
    assertTrue(result);
  }

  @Test
  public void testIsPasswordUpperCaseeOnly() {
    boolean result = Login.isPassword("ABCDEFG");
    assertTrue(result);
  }

  @Test
  public void testIsPasswordAlphaNum() {
    boolean result = Login.isPassword("ABCDEFGkadkdk39393");
    assertTrue(result);
  }

  @Test
  public void testIsPasswordSpecial() {
    boolean result = Login.isPassword("_@.!&*#$?^ ");
    assertTrue(result);
  }

  /** We don't allow % */
  @Test
  public void testIsPasswordInvalid() {
    boolean result = Login.isPassword("%_@.!&*#$?^ ");
    assertFalse(result);
  }

  @Test
  public void testIsUserNameSpecial() {
    boolean result = Login.isUserName("%_@.!&*#$?^ ");
    assertFalse(result);
  }

  @Test
  public void testIsUserNameSpaceInvalid() {
    boolean result = Login.isUserName("foo bar");
    assertFalse(result);
  }

  @Test
  public void testIsUserName() {
    boolean result = Login.isUserName("testing");
    assertTrue(result);
  }

  @Test
  public void testIsUserNameWeirdButValid() {
    boolean result = Login.isUserName("testing123_");
    assertTrue(result);
  }

  @Test
  public void testSha1() throws NoSuchAlgorithmException {
    String result = Login.sha1("foo");
    assertEquals("0beec7b5ea3f0fdbc95d0dd47f3c5bc275da8a33", result);
  }

  @Test
  public void testSha256() throws NoSuchAlgorithmException {
    String result = Login.sha256("foo");
    assertEquals("2c26b46b68ffc68ff99b453c1d30413413422d706483bfa0f98a5e886266e7ae", result);
  }

  @Test
  public void testValidateShortUser() {
    int result = login.validate("a", "basic");
    assertEquals(BAD_USER_ID, result);
  }

  @Test
  public void testValidateShortPass() {
    int result = login.validate("abcdef", "a");
    assertEquals(BAD_USER_ID, result);
  }

  @Test
  public void testValidateBadUser() {
    int result = login.validate("a@b", "basic");
    assertEquals(BAD_USER_ID, result);
  }

  @Test
  public void testValidateBadPass() {
    int result = login.validate("abb", "basic%");
    assertEquals(BAD_USER_ID, result);
  }

  @Test
  public void testValidate() {
    User user = new User();
    user.setId(1);
    user.setUsername("abc");
    when(userRepository.findByUsernameAndPassword(anyString(), anyString())).thenReturn(user);
    int result = login.validate("abc", "basic");
    assertEquals(1, result);

    verify(userRepository).findByUsernameAndPassword(anyString(), anyString());
  }
}
