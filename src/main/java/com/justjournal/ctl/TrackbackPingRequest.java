package com.justjournal.ctl;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Lucas Holt
 */
@Getter
@Setter
public class TrackbackPingRequest {
    private int entryID;
    private String url;
    private String title;
    private String name;
    private String blog_name; // spec requirement
    private String excerpt;
    private String comment;
    private String email;
}
