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

import java.beans.XMLEncoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
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

import kr.ac.hanyang.tosca2camp.Tosca2CampLauncher;
import kr.ac.hanyang.tosca2camp.Tosca2CampPlatform;
//import kr.ac.hanyang.tosca2camp.rest.model.ModelFactory;
import kr.ac.hanyang.tosca2camp.rest.model.ServiceTemplateModel;
import kr.ac.hanyang.tosca2camp.rest.model.impl.ModelFactoryImpl;
import kr.ac.hanyang.tosca2camp.rest.resources.ServiceTemplateResource;

/**
 * Entry point of the 'TOSCA2CAMP' transformation module.
 */
public class TOSCA2CAMP {

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
		
	/**
	 * The main method.
	 * 
	 * @param args
	 *            are the arguments
	 * @generated
	 */
	public static void main(String[] args) {
		try {
		//	if (args.length < 2) {
		//		System.out.println("Arguments not valid : {IN_model_path, OUT_model_path}.");
		//	} else {
			
			
			
			Tosca2CampLauncher launcher = Tosca2CampLauncher.of()
															.platform(Tosca2CampPlatform.newPlatform())
															.serviceTemplate("WebappExample.yml")
															.createTemplates(true)
															.launchPlatform();
															
			
			//Tosca2CampPlatform toscaPlatform = launcher.getPlatform();
			
			//toscaPlatform.createServiceTemplate("WebappExample.yml");
			
			//ByteArrayOutputStream baos = new ByteArrayOutputStream();
			//XMLEncoder e = new XMLEncoder(baos);
			//e.writeObject(ServiceTemplateResource.getServiceTemplate(launcher.getPlatform().getServiceTemplate("ServiceTemplate")));
			//e.close();
			//ObjectOutputStream oos = new ObjectOutputStream(baos);
			//oos.writeObject(ServiceTemplateResource.getServiceTemplate(launcher.getPlatform().getServiceTemplate("ServiceTemplate")));
			//oos.close();
			//System.out.println(baos.toString( "UTF-8"));
			//InputStream is = new ByteArrayInputStream(baos.toByteArray());
			
				TOSCA2CAMP runner = new TOSCA2CAMP();
				runner.loadModel(ServiceTemplateResource.getServiceTemplate(launcher.getPlatform().getServiceTemplate("ServiceTemplate")));
				//runner.doTOSCA2CAMP(new NullProgressMonitor());
				runner.saveModels("C:\\Users\\kenax\\test.ecore");
			//}
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
	 * @generated
	 */
	public TOSCA2CAMP() throws IOException {
		properties = new Properties();
		properties.load(getFileURL("TOSCA2CAMP.properties").openStream());
		//Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
		//Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
	}
	
	/**
	 * Load the input and input/output models, initialize output models.
	 * 
	 * @param inModelPath
	 *            the IN model path
	 * @throws ATLCoreException
	 *             if a problem occurs while loading models
	 *
	 * @generated
	 */
//	public void loadModels(String inModelPath) throws ATLCoreException {
//		ModelFactory factory = new EMFModelFactory();
//		IInjector injector = new EMFInjector();
//	 	IReferenceModel mmcampMetamodel = factory.newReferenceModel();
//		injector.inject(mmcampMetamodel, getMetamodelUri("MMCAMP"));
//	 	IReferenceModel mmtoscaMetamodel = factory.newReferenceModel();
//		injector.inject(mmtoscaMetamodel, getMetamodelUri("MMTOSCA"));
//		this.inModel = factory.newModel(mmtoscaMetamodel);
//		injector.inject(inModel, inModelPath);
//		this.outModel = factory.newModel(mmcampMetamodel);
//	}
	
	public void loadModel(Object obj) throws ATLCoreException {
		ResourceSet resourceSet= new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());
		URI fileURI = URI.createFileURI("C:\\Users\\kenax\\test.xml");
		Resource resource = resourceSet.createResource(fileURI);
		//resource.getContents().add((ServiceTemplateModel)obj);
		resource.getContents().add(kr.ac.hanyang.tosca2camp.rest.model.ModelFactory.eINSTANCE.createServiceTemplateModel());
	
		ModelFactory factory = new EMFModelFactory();
		IInjector injector = new EMFInjector();		
	 	IReferenceModel mmcampMetamodel = factory.newReferenceModel();
	 	IModel model = factory.newModel(mmcampMetamodel);
	 	
	 	//ResourceSet resourceSet = ((EMFModelFactory)mmcampMetamodel.getModelFactory()).getResourceSet();
	 	
	 	((EMFInjector)injector).inject(model, resource);
		
		this.outModel= model;
	}
	
	/**
	 * Save the output and input/output models.
	 * 
	 * @param outModelPath
	 *            the OUT model path
	 * @throws ATLCoreException
	 *             if a problem occurs while saving models
	 *
	 * @generated
	 */
	public void saveModels(String outModelPath) throws ATLCoreException {
		IExtractor extractor = new EMFExtractor();
		extractor.extract(outModel, outModelPath);
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
	public Object doTOSCA2CAMP(IProgressMonitor monitor) throws ATLCoreException, IOException, ATLExecutionException {
		ILauncher launcher = new EMFVMLauncher();
		Map<String, Object> launcherOptions = getOptions();
		launcher.initialize(launcherOptions);
		launcher.addInModel(inModel, "IN", "MMTOSCA");
		launcher.addOutModel(outModel, "OUT", "MMCAMP");
		return launcher.launch("run", monitor, launcherOptions, (Object[]) getModulesList());
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
		String modulesList = properties.getProperty("TOSCA2CAMP.modules");
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
	 * Returns the URI of the given metamodel, parameterized from the property file.
	 * 
	 * @param metamodelName
	 *            the metamodel name
	 * @return the metamodel URI
	 *
	 * @generated
	 */
	protected String getMetamodelUri(String metamodelName) {
		return properties.getProperty("TOSCA2CAMP.metamodels." + metamodelName);
	}
	
	/**
	 * Returns the file name of the given library, parameterized from the property file.
	 * 
	 * @param libraryName
	 *            the library name
	 * @return the library file name
	 *
	 * @generated
	 */
	protected InputStream getLibraryAsStream(String libraryName) throws IOException {
		return getFileURL(properties.getProperty("TOSCA2CAMP.libraries." + libraryName)).openStream();
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
			if (entry.getKey().toString().startsWith("TOSCA2CAMP.options.")) {
				options.put(entry.getKey().toString().replaceFirst("TOSCA2CAMP.options.", ""), 
				entry.getValue().toString());
			}
		}
		return options;
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
			URL resourceURL = TOSCA2CAMP.class.getResource(fileName);
			if (resourceURL != null) {
				fileURL = FileLocator.toFileURL(resourceURL);
			} else {
				fileURL = null;
			}
		} else {
			fileURL = TOSCA2CAMP.class.getResource(fileName);
		}
		if (fileURL == null) {
			throw new IOException("'" + fileName + "' not found");
		} else {
			return fileURL;
		}
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
