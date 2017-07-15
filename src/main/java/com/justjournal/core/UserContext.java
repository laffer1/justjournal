package com.justjournal.core;

import com.justjournal.model.User;

/**
 * Represent the blog user and authenticated user in one package along with the output buffer.
 * @author Lucas Holt
 */
public class UserContext {
     private User blogUser;          // the blog owner
     private User authenticatedUser; // the logged in user

     /**
      * Default constructor for User Context.  Creates a usable instance.
      *
      * @param currentBlogUser blog owner
      * @param authUser        logged in user
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
         return authenticatedUser != null && blogUser != null
                 && authenticatedUser.getUsername().compareTo(blogUser.getUsername()) == 0;
     }
 }
