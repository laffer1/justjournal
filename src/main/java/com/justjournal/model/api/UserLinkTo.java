package com.justjournal.model.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author Lucas Holt
 */
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserLinkTo implements Serializable, Comparable<UserLinkTo> {

    @JsonIgnore
    private static final long serialVersionUID = -132658312201527378L;

    private int id;

    private String title;

    private String uri;

    @JsonIgnore
    private int userId;

    @JsonCreator
    public UserLinkTo() {
        super();
    }

    /**
     * Retrieve unique identifier for link
     *
     * @return int > 0
     */
    public final int getId() {
        return id;
    }

    /**
     * Set unique identifier for link
     *
     * @param id > 0
     */
    public void setId(int id) {
        if (id > 0)
            this.id = id;
        else
            throw new IllegalArgumentException("id must be greater than zero");
    }

    /**
     * Retrieve the link title
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the link title so that it can be displayed instead of just the hyperlink itself.
     *
     * @param title ascii text
     */
    public void setTitle(String title) {
        if (title != null)
            this.title = title;
        else
            throw new IllegalArgumentException("title cannot be null");
    }

    /**
     * Retrieve the address of the link which should be a complete URI
     *
     * @return uri string
     */
    public String getUri() {
        return uri;
    }

    /**
     * Set the Uniform resource identifier as a string
     *
     * @param uri a valid uri
     */
    public void setUri(String uri) {
        if (uri != null)
            this.uri = uri;
        else
            throw new IllegalArgumentException("uri cannot be null");
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(final int userId) {
        this.userId = userId;
    }

    public int compareTo(UserLinkTo ul) {
        return this.getTitle().compareTo(ul.getTitle());
    }

}
