package com.regsync.sample;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserDto {
	// フィールド変数
	private int id;
	private String name;
}
