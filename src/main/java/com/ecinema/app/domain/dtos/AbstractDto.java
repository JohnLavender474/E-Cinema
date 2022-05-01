package com.ecinema.app.domain.dtos;

import java.io.Serializable;

/**
 * A dto class is a POJO that relays information from the persistence layer to the view layer
 * without having to relay the persistent entity objects themselves. This results in segregation
 * of the view layer and business layer, thereby not having one coupled to the changes of the other.
 */
public interface AbstractDto extends Serializable {}
