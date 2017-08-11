package com.wy.dubbo_jmeter.test;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.sun.mail.iap.Argument;
import com.wy.sequence.service.SequenceService;

public class TestConsumer extends AbstractJavaSamplerClient{

	Logger log = LoggerFactory.getLogger(TestConsumer.class);
	
	SequenceService sequenceService ;
	
	ReferenceConfig<SequenceService> ref;
	
	public void init(){
		ApplicationConfig config = new ApplicationConfig();
		config.setName("dubbo_jmeter");
		
		RegistryConfig reg = new RegistryConfig();
		reg.setAddress("127.0.0.1:2181");
		reg.setProtocol("zookeeper");
		
		
		ref = new ReferenceConfig<SequenceService>();
		ref.setApplication(config);
		ref.setRegistry(reg);
		ref.setVersion("myversion");
		ref.setInterface(SequenceService.class);
		ref.setTimeout(5000);
		ref.setRetries(3);
		ref.setCheck(false);
		sequenceService = ref.get();
	}
	@Override
	public void setupTest(JavaSamplerContext arg0) {
		init();
    }
	@Override
	public SampleResult runTest(JavaSamplerContext arg0) {
		SampleResult sr =new SampleResult();
		try{
		sr.setSampleLabel("haha");
		sr.sampleStart();
		if(check(sequenceService.getSequence())){
			sr.setSuccessful(true);
		}else{
			sr.setSuccessful(false);
		}
		sr.setResponseCode("000");
		sr.setResponseMessage("123");
		sr.setResponseMessageOK();
		sr.sampleEnd();
		}catch (Exception e) {
			log.info("successful");
			sr.setSuccessful(false);
		}
		return sr;
	}
	@Override
	public void teardownTest(JavaSamplerContext arg0){
		ref.destroy();
	}
	@Override
	public Arguments getDefaultParameters(){
		Arguments params = new Arguments();
		
//		params.addArgument("hah","haha");
		
		return params;
	}
	static Long now =new Long(0);
	private boolean check(Long x){
		synchronized (now) {
			if(now!=0){
				if((now+1) !=x){
					now = x;
					return false;
				}else{
					now++;
					return true;
				}
			}else{
				now = x;
			}
			return true;
		}
	}
	
}
