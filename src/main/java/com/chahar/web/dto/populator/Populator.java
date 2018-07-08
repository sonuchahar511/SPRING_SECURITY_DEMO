package com.chahar.web.dto.populator;

import java.io.Serializable;

import com.chahar.web.dto.Dto;

public interface Populator<SOURCE extends Serializable,TARGET extends Dto> {
	TARGET populate(SOURCE source);
}
