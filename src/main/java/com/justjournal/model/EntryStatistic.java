package com.justjournal.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Lucas Holt
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class , property = "id")
@Entity
@Table(name = "entry_statistics")
public class EntryStatistic implements Serializable {

    private static final long serialVersionUID = 7280767109218766181L;
    
    @Id
    @GeneratedValue
    private int id;

    @JsonProperty("user")
    @ManyToOne
    @JoinColumn(name = "uid")
    private User user;

    @Column(name = "year")
    private int year;

    @Column(name = "entry_count")
    private long count;

    @Column(name = "modified")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date modified;

    @JsonCreator
    public EntryStatistic() {
        super();
    }
}
