package com.justjournal.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Represent individual journal.  This allows multiple journals to be associated with one login.
 * @author Lucas Holt
 */
@Entity
@Table(name = "journal")
public class Journal implements Serializable {
    private static final long serialVersionUID = 9106701690730308047L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "slug", length = 15, nullable = false)
    private String slug = "";

    @Column(name = "name", length = 150, nullable = true)
    private String name = "";

    @JsonBackReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "allow_spider")
    @Enumerated(EnumType.STRING)
    private boolean allowSpider = true;

    @ManyToOne
    @JoinColumn(name = "style")
    private Style style;

    @Column(name = "owner_view_only", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private boolean ownerViewOnly = false;

    @Column(name = "ping_services", nullable = false, length = 1)
    @Enumerated(EnumType.STRING)
    private boolean pingServices = true;

    @Column(name = "modified", nullable = false)
    private Timestamp modified;

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(final String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public boolean isAllowSpider() {
        return allowSpider;
    }

    public void setAllowSpider(final boolean allowSpider) {
        this.allowSpider = allowSpider;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(final Style style) {
        this.style = style;
    }

    public boolean isOwnerViewOnly() {
        return ownerViewOnly;
    }

    public void setOwnerViewOnly(final boolean ownerViewOnly) {
        this.ownerViewOnly = ownerViewOnly;
    }

    public boolean isPingServices() {
        return pingServices;
    }

    public void setPingServices(final boolean pingServices) {
        this.pingServices = pingServices;
    }

    public Timestamp getModified() {
        return modified;
    }

    public void setModified(final Timestamp modified) {
        this.modified = modified;
    }
}