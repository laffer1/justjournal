package com.justjournal.model;

import com.justjournal.utility.DateConvert;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(value = XmlAccessType.NONE)
@XmlRootElement(name = "url")
public class Url {
    public enum Priority {
        HIGH("1.0"), MEDIUMHIGH("0.7"), MEDIUM("0.5"), MEDIUMLOW("0.2"), LOW("0.0");

        private String value;

        Priority(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ChangeFreqency {
        ALWAYS("always"), HOURLY("hourly"), DAILY("daily"), WEEKLY("weekly"), MONTHLY("monthly"), YEARLY("yearly"), NEVER("never");

        private String value;

        ChangeFreqency(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @XmlElement
    private String loc;

    @XmlElement
    private String lastmod = DateConvert.encode8601();

    @XmlElement
    private String changefreq = ChangeFreqency.DAILY.getValue();

    @XmlElement
    private String priority = Priority.MEDIUM.getValue();

    public Url() {
        this("", Priority.MEDIUM, ChangeFreqency.DAILY);
    }

    public Url(final String loc, final Priority priority) {
        this(loc, priority, ChangeFreqency.DAILY);
    }

    public Url(final String loc, final Priority priority, final ChangeFreqency freqency) {
        this.loc = loc;
        this.priority = priority.getValue();
        this.changefreq = freqency.getValue();
    }

    public String getLoc() {
        return loc;
    }

    public String getPriority() {
        return priority;
    }

    public String getChangefreq() {
        return changefreq;
    }

    public String getLastmod() {
        return lastmod;
    }
}
