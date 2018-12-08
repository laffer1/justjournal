package com.justjournal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Lucas Holt
 */
@Entity
@Table(name = "favorites")
public class Favorite implements Serializable {

    private static final long serialVersionUID = 4304617243861178238L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonProperty("user")
    @ManyToOne
    @JoinColumn(name = "owner")
    private User user;

    @JsonBackReference
    @JsonProperty("entry")
    @ManyToOne
    @JoinColumn(name = "entryid")
    private Entry entry;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(final Entry entry) {
        this.entry = entry;
    }
}
