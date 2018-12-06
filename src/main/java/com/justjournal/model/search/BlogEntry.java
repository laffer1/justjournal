package com.justjournal.model.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An individual blog entry for search
 * @author Lucas Holt
 */
@ToString
@EqualsAndHashCode
@Document(indexName = "#{@blogEntryIndex}", type = "entry")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlogEntry implements Serializable, Comparable<BlogEntry> {
    private static final long serialVersionUID = 3452319081969591585L;

    @Id
    @Getter
    @Setter
    private Integer id;

    @Getter
    @Setter
    private String author;

    @Getter
    @Setter
    private Date date;


    @Field(type = FieldType.Nested)
    @Getter
    @Setter
    private List<Tag> tags = new ArrayList<>();

    @Getter
    @Setter
    private Boolean privateEntry = true;

    @Getter
    @Setter
    @Version
    private Long version;

    @Getter
    @Setter
    private String subject;

    @Getter
    @Setter
    private String body;

    @Override
    public int compareTo(final BlogEntry o) {
        return this.id.compareTo(o.getId());
    }
}