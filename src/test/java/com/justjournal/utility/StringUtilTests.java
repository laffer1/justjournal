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
package com.justjournal.utility;

import static com.justjournal.utility.StringUtil.lengthCheck;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** @author Lucas Holt */
public class StringUtilTests {

  @Test
  public void testDeleteChar() {
    String result = StringUtil.deleteChar("I am a cool dude.", 'a');
    assertEquals("I m  cool dude.", result);
  }

  @Test
  public void testReplaceChar() {
    String result = StringUtil.replace("We believe OS/2 is the platform for the 90s.", ' ', "_");
    assertEquals("We_believe_OS/2_is_the_platform_for_the_90s.", result);
  }

  @Test
  public void testReplaceArray() {
    String[] txt = {"a"};
    String result = StringUtil.replace("We believe OS/2 is the platform for the 90s.", " ", txt);
    assertEquals("WeabelieveaOS/2aisatheaplatformaforathea90s.", result);
  }

  @Test
  public void testEmail() {
    boolean result = StringUtil.isEmailValid("foo@bar.baz");
    assertTrue(result);
  }

  @Test
  public void testEmailNull() {
    boolean result = StringUtil.isEmailValid(null);
    assertFalse(result);
  }

  // we don't support TLD as email right now
  @Test
  public void testEmailTLD() {
    boolean result = StringUtil.isEmailValid("foo@coke");
    assertFalse(result);
  }

  @Test
  public void testEmailNoDomain() {
    boolean result = StringUtil.isEmailValid("foo");
    assertFalse(result);
  }

  @Test
  public void testEmailNoUser() {
    boolean result = StringUtil.isEmailValid("@foo");
    assertFalse(result);
  }

  @Test
  public void testIsAlphaNumericWithLetters() {
    boolean result = StringUtil.isAlphaNumeric("foo");
    assertTrue(result);
  }

  @Test
  public void testIsAlphaNumericWithMix() {
    boolean result = StringUtil.isAlphaNumeric("foo123");
    assertTrue(result);
  }

  @Test
  public void testIsAlphaNumericWithNumbers() {
    boolean result = StringUtil.isAlphaNumeric("123");
    assertTrue(result);
  }

  @Test
  public void testIsAlphaNumericSpace() {
    boolean result = StringUtil.isAlphaNumeric(" ");
    assertFalse(result);
  }

  @Test
  public void testIsAlphaNumericInvalid() {
    boolean result = StringUtil.isAlphaNumeric("!");
    assertFalse(result);
  }

  @Test
  public void testIsAlphaNumericNull() {
    boolean result = StringUtil.isAlphaNumeric(null);
    assertFalse(result);
  }

  @Test
  public void testIsAlpha() {
    boolean result = StringUtil.isAlpha("foo");
    assertTrue(result);
  }

  @Test
  public void testIsAlphaNumberString() {
    boolean result = StringUtil.isAlpha("123");
    assertFalse(result);
  }

  @Test
  public void testIsAlphaNull() {
    boolean result = StringUtil.isAlpha(null);
    assertFalse(result);
  }

  @Test
  public void testLengthCheckHappy() {
    boolean result = lengthCheck("foo", 1, 3);
    assertTrue(result);
  }

  @Test
  public void testLengthTooShort() {
    boolean result = lengthCheck("foo", 4, 12);
    assertFalse(result);
  }

  @Test
  public void testLengthTooLong() {
    boolean result = lengthCheck("foo", 1, 2);
    assertFalse(result);
  }

  @Test
  public void testLengthNull() {
    boolean result = lengthCheck(null, 1, 2);
    assertFalse(result);
  }
}
