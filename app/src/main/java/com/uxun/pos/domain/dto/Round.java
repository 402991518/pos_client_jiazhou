package com.uxun.pos.domain.dto;

import java.io.Serializable;

public class Round implements Serializable {

	private static final long serialVersionUID = -3663927525571713237L;
	public Integer scale;// 精度
	public Integer mode;// 舍入模式

	public Round(Integer scale, Integer mode) {
		this.scale = scale;
		this.mode = mode;
	}
}
