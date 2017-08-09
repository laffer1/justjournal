package com.justjournal.model.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

/**
 * @author Lucas Holt
 */
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PublicMember implements Serializable {

    @JsonIgnore
    private static final long serialVersionUID = -877596710940098083L;

    private String username;
    private String name;
    private int since;
}