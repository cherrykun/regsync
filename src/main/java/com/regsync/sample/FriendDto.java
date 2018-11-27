package com.regsync.sample;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class FriendDto {
	// フィールド変数
	private int from;
	private int to;
}
