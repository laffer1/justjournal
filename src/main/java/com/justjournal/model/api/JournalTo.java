package com.justjournal.model.api;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Lucas Holt
 */
@AllArgsConstructor
@Builder
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalTo implements Serializable {
    @JsonIgnore
    private static final long serialVersionUID = 9106701690730308099L;

    private int id;

    private String slug = "";

    private String name = "";

    private boolean allowSpider = true;

    private int styleId;

    private boolean ownerViewOnly = false;

    private boolean pingServices = true;

    private Date since;

    private Date modified;

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

    public boolean isAllowSpider() {
        return allowSpider;
    }

    public void setAllowSpider(final boolean allowSpider) {
        this.allowSpider = allowSpider;
    }

    public int getStyleId() {
        return this.styleId;
    }

    public void setStyleId(int styleId) {
        this.styleId = styleId;
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

    public Date getModified() {
        return modified;
    }

    public void setModified(final Date modified) {
        this.modified = modified;
    }

    public Date getSince() {
        return since;
    }

    public void setSince(final Date since) {
        this.since = since;
    }

    @JsonCreator
    public JournalTo() {
        super();
    }
}