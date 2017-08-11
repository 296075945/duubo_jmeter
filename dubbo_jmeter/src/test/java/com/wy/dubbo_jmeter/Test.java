package com.wy.dubbo_jmeter;

import org.apache.jmeter.samplers.SampleResult;

import com.wy.dubbo_jmeter.test.TestConsumer;

public class Test {
	public static void main(String[] args) {
		TestConsumer tc = new TestConsumer();
		tc.setupTest(null);
		for(int i =0;i<100;i++){
			
			SampleResult sr = tc.runTest(null);
			System.out.println(sr.isSuccessful());
		}
	}
}
