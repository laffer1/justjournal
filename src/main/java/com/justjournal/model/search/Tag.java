package com.justjournal.model.search;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Tags for blog entries
 * @author Lucas Holt
 */
@ToString
@EqualsAndHashCode
public class Tag implements Serializable, Comparable<Tag> {
	private static final long serialVersionUID = 3452319081969591586L;

	@Getter
	@Setter
	private String name;

	@Override
	public int compareTo( final Tag o) {
		return this.name.compareTo(o.getName());
	}
}
