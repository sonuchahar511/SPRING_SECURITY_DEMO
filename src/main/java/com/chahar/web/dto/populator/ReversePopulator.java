package com.chahar.web.dto.populator;

import java.io.Serializable;

import com.chahar.web.dto.Dto;

public interface ReversePopulator <SOURCE extends Dto,TARGET extends Serializable>{
	TARGET populate(SOURCE source);
}
