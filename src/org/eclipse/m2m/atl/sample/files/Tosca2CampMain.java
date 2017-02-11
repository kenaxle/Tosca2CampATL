package org.eclipse.m2m.atl.sample.files;

import java.io.IOException;

import org.eclipse.m2m.atl.sample.files.T2CATLConverter;

import kr.ac.hanyang.oCamp.camp.platform.oCampPlatform;
import kr.ac.hanyang.oCamp.camp.platform.oCampPlatformLauncher;
import kr.ac.hanyang.oCamp.core.mgmt.BaseEntityManager;
import kr.ac.hanyang.oCamp.launcher.OCampLauncher;
import kr.ac.hanyang.tosca2camp.Tosca2CampPlatform;

public class Tosca2CampMain {
    public static void main(String [] args){
	OCampLauncher launcher = OCampLauncher.newInstance()
			 .managementContext(new BaseEntityManager())
			 .start();
	
	Tosca2CampPlatform toscaPlatform = Tosca2CampPlatform.newPlatform();
	toscaPlatform.createServiceTemplate("WebAppExample.yml");
	oCampPlatform campPlatform = new oCampPlatformLauncher()
            .useManagementContext(new BaseEntityManager())
            .launch()
            .getCampPlatform();
	
	try {
		T2CATLConverter converter = T2CATLConverter.newT2CATLConverter()
												   .serviceTemplate(toscaPlatform.getServiceTemplate("ServiceTemplate"));
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}
