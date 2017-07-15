package com.justjournal.ctl;

import com.justjournal.model.User;

/**
 * Represent the blog user and authenticated user in one package along with the output buffer.
 * @author Lucas Holt
 */
class UserContext {
     private User blogUser;          // the blog owner
     private User authenticatedUser; // the logged in user

     /**
      * Default constructor for User Context.  Creates a usable instance.
      *
      * @param currentBlogUser blog owner
      * @param authUser        logged in user
      */
     UserContext(final User currentBlogUser, final User authUser) {
         this.blogUser = currentBlogUser;
         this.authenticatedUser = authUser;
     }

     /**
      * Retrieve the blog owner
      *
      * @return blog owner
      */
     User getBlogUser() {
         return blogUser;
     }

     /**
      * Retrieve the authenticated aka logged in user.
      *
      * @return logged in user.
      */
     User getAuthenticatedUser() {
         return authenticatedUser;
     }

     /**
      * Check to see if the authenticated user is the blog owner also. Used for private information.
      *
      * @return true if blog owner = auth owner
      */
     boolean isAuthBlog() {
         return authenticatedUser != null && blogUser != null
                 && authenticatedUser.getUsername().compareTo(blogUser.getUsername()) == 0;
     }
 }
