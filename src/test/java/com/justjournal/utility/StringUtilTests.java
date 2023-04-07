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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** @author Lucas Holt */
class StringUtilTests {

  @Test
  void testDeleteChar() {
    String result = StringUtil.deleteChar("I am a cool dude.", 'a');
    Assertions.assertEquals("I m  cool dude.", result);
  }

  @Test
  void testReplaceChar() {
    String result = StringUtil.replace("We believe OS/2 is the platform for the 90s.", ' ', "_");
    Assertions.assertEquals("We_believe_OS/2_is_the_platform_for_the_90s.", result);
  }

  @Test
  void testReplaceArray() {
    String[] txt = {"a"};
    String result = StringUtil.replace("We believe OS/2 is the platform for the 90s.", " ", txt);
    Assertions.assertEquals("WeabelieveaOS/2aisatheaplatformaforathea90s.", result);
  }

  @Test
  void testEmail() {
    boolean result = StringUtil.isEmailValid("foo@bar.baz");
    Assertions.assertTrue(result);
  }

  @Test
  void testEmailNull() {
    boolean result = StringUtil.isEmailValid(null);
    Assertions.assertFalse(result);
  }

  // we don't support TLD as email right now
  @Test
  void testEmailTLD() {
    boolean result = StringUtil.isEmailValid("foo@coke");
    Assertions.assertFalse(result);
  }

  @Test
  void testEmailNoDomain() {
    boolean result = StringUtil.isEmailValid("foo");
    Assertions.assertFalse(result);
  }

  @Test
  void testEmailNoUser() {
    boolean result = StringUtil.isEmailValid("@foo");
    Assertions.assertFalse(result);
  }

  @Test
  void testIsAlphaNumericWithLetters() {
    boolean result = StringUtil.isAlphaNumeric("foo");
    Assertions.assertTrue(result);
  }

  @Test
  void testIsAlphaNumericWithMix() {
    boolean result = StringUtil.isAlphaNumeric("foo123");
    Assertions.assertTrue(result);
  }

  @Test
  void testIsAlphaNumericWithNumbers() {
    boolean result = StringUtil.isAlphaNumeric("123");
    Assertions.assertTrue(result);
  }

  @Test
  void testIsAlphaNumericSpace() {
    boolean result = StringUtil.isAlphaNumeric(" ");
    Assertions.assertFalse(result);
  }

  @Test
  void testIsAlphaNumericInvalid() {
    boolean result = StringUtil.isAlphaNumeric("!");
    Assertions.assertFalse(result);
  }

  @Test
  void testIsAlphaNumericNull() {
    boolean result = StringUtil.isAlphaNumeric(null);
    Assertions.assertFalse(result);
  }

  @Test
  void testIsAlpha() {
    boolean result = StringUtil.isAlpha("foo");
    Assertions.assertTrue(result);
  }

  @Test
  void testIsAlphaNumberString() {
    boolean result = StringUtil.isAlpha("123");
    Assertions.assertFalse(result);
  }

  @Test
  void testIsAlphaNull() {
    boolean result = StringUtil.isAlpha(null);
    Assertions.assertFalse(result);
  }

  @Test
  void testLengthCheckHappy() {
    boolean result = lengthCheck("foo", 1, 3);
    Assertions.assertTrue(result);
  }

  @Test
  void testLengthTooShort() {
    boolean result = lengthCheck("foo", 4, 12);
    Assertions.assertFalse(result);
  }

  @Test
  void testLengthTooLong() {
    boolean result = lengthCheck("foo", 1, 2);
    Assertions.assertFalse(result);
  }

  @Test
  void testLengthNull() {
    boolean result = lengthCheck(null, 1, 2);
    Assertions.assertFalse(result);
  }
}
