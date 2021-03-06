/*******************************************************************************
 * Copyright (c) 2010, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2m.atl.sample.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.m2m.atl.common.ATLExecutionException;
import org.eclipse.m2m.atl.core.ATLCoreException;
import org.eclipse.m2m.atl.core.IExtractor;
import org.eclipse.m2m.atl.core.IInjector;
import org.eclipse.m2m.atl.core.IModel;
import org.eclipse.m2m.atl.core.IReferenceModel;
import org.eclipse.m2m.atl.core.ModelFactory;
import org.eclipse.m2m.atl.core.emf.EMFExtractor;
import org.eclipse.m2m.atl.core.emf.EMFInjector;
import org.eclipse.m2m.atl.core.emf.EMFModelFactory;
import org.eclipse.m2m.atl.core.launch.ILauncher;
import org.eclipse.m2m.atl.engine.emfvm.launch.EMFVMLauncher;

import kr.ac.hanyang.oCamp.camp.pdp.DeploymentPlan;
import kr.ac.hanyang.oCamp.camp.pdp.PdpPackage;
import kr.ac.hanyang.oCamp.camp.pdp.impl.PdpFactoryImpl;
import kr.ac.hanyang.tosca2camp.Tosca2CampLauncher;
import kr.ac.hanyang.tosca2camp.Tosca2CampPlatform;
import kr.ac.hanyang.tosca2camp.rest.model.ModelPackage;
//import kr.ac.hanyang.tosca2camp.rest.model.ModelFactory;
import kr.ac.hanyang.tosca2camp.rest.model.ServiceTemplateModel;
import kr.ac.hanyang.tosca2camp.rest.resources.ServiceTemplateTransformer;
import kr.ac.hanyang.tosca2camp.templates.ServiceTemplate;


/**
 * Entry point of the 'TOSCA2CAMP' transformation module.
 */
public class T2CATLConverter {

	/**
	 * The property file. Stores module list, the metamodel and library locations.
	 * @generated
	 */
	private Properties properties;
	
	/**
	 * The IN model.
	 * @generated
	 */
	protected IModel inModel;	
	
	/**
	 * The OUT model.
	 * @generated
	 */
	protected IModel outModel;	
	
	protected ResourceSet resourceSet;
	protected EPackage inPackage;
	protected EPackage outPackage;
	
	private static final String MODELPATH = "./src/org/eclipse/m2m/atl/sample/files/";
	protected Resource inResource;
	protected Resource outResource;
	
	private ServiceTemplate serviceTemplate;
	
	/**
	 * The main method.
	 * 
	 * @param args
	 *            are the arguments
	 * @throws IOException 
	 * 
	 */
	public static T2CATLConverter newT2CATLConverter() throws IOException {
		return new T2CATLConverter();
	}
	
	public T2CATLConverter serviceTemplate(ServiceTemplate serviceTemplate){
		this.serviceTemplate = serviceTemplate;
		return this;
	}
	
	public void runConversion(){
		try {
			loadModels(serviceTemplate);
			doT2C(new NullProgressMonitor());
			saveModels();
		} catch (ATLCoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ATLExecutionException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Constructor.
	 *
	 * 
	 */
	protected T2CATLConverter() throws IOException {
		properties = new Properties();
		properties.load(getFileURL("TOSCA2CAMP.properties").openStream());
		resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xmi", new XMIResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		//resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new PdpFactoryImpl());
		
		URI inFileURI = URI.createFileURI(getMetamodelUri("MMTOSCA"));
		inResource = resourceSet.getResource(inFileURI,true);
		URI outFileURI = URI.createFileURI(getMetamodelUri("MMCAMP"));
		outResource = resourceSet.getResource(outFileURI,true);

	}
	
	


	/**
	 * Load the input and input/output models, initialize output models.
	 * 
	 * @param inModelPath
	 *            the IN model path
	 * @throws ATLCoreException
	 *             if a problem occurs while loading models
	 * @throws IOException 
	 *
	 * 
	 */	

	public void loadModels(ServiceTemplate st) throws ATLCoreException, IOException {
		EMFModelFactory factory = new EMFModelFactory();
		EMFInjector injector = new EMFInjector();
	 	IReferenceModel mmtoscaMetamodel = factory.newReferenceModel();
	 	injector.inject(mmtoscaMetamodel, inResource);
	 	
		IReferenceModel mmcampMetamodel = factory.newReferenceModel();
		injector.inject(mmcampMetamodel, outResource);

		//persist the input model
		ServiceTemplateModel  stm = ServiceTemplateTransformer.getServiceTemplate(st);
		persistModel(stm, MODELPATH+"TOSCA.xml");
		
	 	inModel = factory.newModel(mmtoscaMetamodel);

	    InputStream is[] = new InputStream[1];
	    is[0] = new FileInputStream(MODELPATH+"TOSCA.xml");
	    Map<String, Object> options = new HashMap<String, Object>();
        //Inject loaded stuff
	 	injector.inject(inModel, is[0], options);

	 	
 	 	outModel = factory.newModel(mmcampMetamodel);

	}
	
	/**
	 * Transform the models.
	 * 
	 * @param monitor
	 *            the progress monitor
	 * @throws ATLCoreException
	 *             if an error occurs during models handling
	 * @throws IOException
	 *             if a module cannot be read
	 * @throws ATLExecutionException
	 *             if an error occurs during the execution
	 *
	 * @generated
	 */
	public Object doT2C(IProgressMonitor monitor) throws ATLCoreException, IOException, ATLExecutionException {
		ILauncher launcher = new EMFVMLauncher();
		Map<String, Object> launcherOptions = getOptions();
		launcher.initialize(launcherOptions);
		launcher.addInModel(inModel, "IN", "MMTOSCA");
		launcher.addOutModel(outModel, "OUT", "MMCAMP");
		return launcher.launch("run", monitor, launcherOptions, (Object[]) getModulesList());
	}
	
	
	/**
	 * Save the output and input/output models.
	 * 
	 * @param outModelPath
	 *            the OUT model path
	 * @throws ATLCoreException
	 *             if a problem occurs while saving models
	 * @throws IOException 
	 *
	 * 
	 */
	public void saveModels() throws ATLCoreException, IOException {
		IExtractor extractor = new EMFExtractor();
		extractor.extract(outModel, MODELPATH+"CAMP.xml");
	}

	public void persistModel(EObject model, String pathName) throws ATLCoreException, IOException {
		XMIResourceFactoryImpl resFactory = new XMIResourceFactoryImpl();
		URI xmiuri = URI.createFileURI(pathName);
		XMIResource xmiresource = (XMIResource) resFactory.createResource(xmiuri);
		xmiresource.getContents().add(model);
		xmiresource.save(new HashMap());
	}

	public DeploymentPlan getDeploymentPlan() throws IOException{
		PdpPackage.eINSTANCE.eClass();
		
		URI xmiuri = URI.createFileURI(MODELPATH+"CAMP.xml");
		Resource xmiresource = resourceSet.getResource(xmiuri,true);
		
		DeploymentPlan pdp = (DeploymentPlan) xmiresource.getContents().get(0);
		return pdp;
	}
	
	/**
	 * Returns the URI of the given metamodel, parameterized from the property file.
	 * 
	 * @param metamodelName
	 *            the metamodel name
	 * @return the metamodel URI
	 *
	 * @generated
	 */
	protected String getMetamodelUri(String metamodelName) {
		return properties.getProperty("T2C.metamodels." + metamodelName);
	}
	
	
	protected String getModelUri(String modelName) {
		return properties.getProperty("TOSCA2CAMP.models." + modelName);
	}
	
	
	/**
	 * Finds the file in the plug-in. Returns the file URL.
	 * 
	 * @param fileName
	 *            the file name
	 * @return the file URL
	 * @throws IOException
	 *             if the file doesn't exist
	 * 
	 * @generated
	 */
	protected static URL getFileURL(String fileName) throws IOException {
		final URL fileURL;
		if (isEclipseRunning()) {
			URL resourceURL = T2CATLConverter.class.getResource(fileName);
			if (resourceURL != null) {
				fileURL = FileLocator.toFileURL(resourceURL);
			} else {
				fileURL = null;
			}
		} else {
			fileURL = T2CATLConverter.class.getResource(fileName);
		}
		if (fileURL == null) {
			throw new IOException("'" + fileName + "' not found");
		} else {
			return fileURL;
		}
	}
	
	/**
	 * Returns the options map, parameterized from the property file.
	 * 
	 * @return the options map
	 *
	 * @generated
	 */
	protected Map<String, Object> getOptions() {
		Map<String, Object> options = new HashMap<String, Object>();
		for (Entry<Object, Object> entry : properties.entrySet()) {
			if (entry.getKey().toString().startsWith("T2C.options.")) {
				options.put(entry.getKey().toString().replaceFirst("T2C.options.", ""), 
				entry.getValue().toString());
			}
		}
		return options;
	}
	
	/**
	 * Returns an Array of the module input streams, parameterized by the
	 * property file.
	 * 
	 * @return an Array of the module input streams
	 * @throws IOException
	 *             if a module cannot be read
	 *
	 * @generated
	 */
	protected InputStream[] getModulesList() throws IOException {
		InputStream[] modules = null;
		String modulesList = properties.getProperty("T2C.modules");
		if (modulesList != null) {
			String[] moduleNames = modulesList.split(",");
			modules = new InputStream[moduleNames.length];
			for (int i = 0; i < moduleNames.length; i++) {
				String asmModulePath = new Path(moduleNames[i].trim()).removeFileExtension().addFileExtension("asm").toString();
				modules[i] = getFileURL(asmModulePath).openStream();
			}
		}
		return modules;
	}
	
	/**
	 * Tests if eclipse is running.
	 * 
	 * @return <code>true</code> if eclipse is running
	 *
	 * @generated
	 */
	public static boolean isEclipseRunning() {
		try {
			return Platform.isRunning();
		} catch (Throwable exception) {
			// Assume that we aren't running.
		}
		return false;
	}
	
}
