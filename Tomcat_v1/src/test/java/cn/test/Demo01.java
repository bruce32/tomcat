package cn.test;

import java.nio.ByteBuffer;

import org.junit.Test;

public class Demo01 {
	@Test
	public void t1() {
		ByteBuffer buffer = ByteBuffer.allocate(8*1024);
		System.out.println(buffer);
	}
}
