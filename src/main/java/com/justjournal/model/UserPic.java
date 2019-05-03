package com.justjournal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Lucas Holt
 */
@Getter
@Setter
@Entity
@Table(name = "user_pic")
public class UserPic implements Serializable {

    private static final long serialVersionUID = -180763510438968573L;

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /*
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "owner")
    private User user;
    */

    @Column(name = "mimetype", length = 75, nullable = false)
    private String mimeType;

    @Column(name = "filename", length = 100)
    private String filename;

    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "date_modified")
    private Date modified;

    @Column(name = "image", columnDefinition = "MEDIUMBLOB")
    @Lob
    private byte[] image;

    @JsonProperty("source")
    @Column(name = "source", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private AvatarSource source = AvatarSource.UPLOAD;
}
