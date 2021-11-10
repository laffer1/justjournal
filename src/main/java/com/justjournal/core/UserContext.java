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
package com.justjournal.core;


import com.justjournal.model.User;

/**
 * Represent the blog user and authenticated user in one package along with the output buffer.
 *
 * @author Lucas Holt
 */
public class UserContext {
  private User blogUser; // the blog owner
  private User authenticatedUser; // the logged in user

  /**
   * Default constructor for User Context. Creates a usable instance.
   *
   * @param currentBlogUser blog owner
   * @param authUser logged in user
   */
  public UserContext(final User currentBlogUser, final User authUser) {
    this.blogUser = currentBlogUser;
    this.authenticatedUser = authUser;
  }

  /**
   * Retrieve the blog owner
   *
   * @return blog owner
   */
  public User getBlogUser() {
    return blogUser;
  }

  /**
   * Retrieve the authenticated aka logged in user.
   *
   * @return logged in user.
   */
  public User getAuthenticatedUser() {
    return authenticatedUser;
  }

  /**
   * Check to see if the authenticated user is the blog owner also. Used for private information.
   *
   * @return true if blog owner = auth owner
   */
  public boolean isAuthBlog() {
    return authenticatedUser != null
        && blogUser != null
        && authenticatedUser.getUsername().compareTo(blogUser.getUsername()) == 0;
  }
}
