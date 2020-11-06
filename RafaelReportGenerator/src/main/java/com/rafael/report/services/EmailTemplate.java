package com.rafael.report.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import com.rafael.report.utils.StaticData;

public class EmailTemplate {
	private String templateId;
	private String template;
	private Map<String, String> replacementParams;

	public EmailTemplate(String templateId) {
		this.templateId = templateId;
		try {
			this.template = loadTemplate(templateId);
		} catch (Exception ex) {
			this.template = Constants.BLANK;
		}
	}

	private String loadTemplate(String templateId) throws Exception {
		File file = new File(
				StaticData.DIR_PATH_NAME + File.separator + "email-templates" + File.separator + templateId);
		// ResourceUtils.getFile("classpath:email-templates/" + templateId);
		String content = Constants.BLANK;
		try {
			content = new String(Files.readAllBytes(file.toPath()));
		} catch (IOException e) {
			throw new Exception("Could not read template with ID = " + templateId);
		}
		return content;
	}

	public String getTemplate(Map<String, String> replacements) {
		String cTemplate = this.getTemplate();

		if (!AppUtil.isObjectEmpty(cTemplate)) {
			for (Map.Entry<String, String> entry : replacements.entrySet()) {
				cTemplate = cTemplate.replace("{{" + entry.getKey() + "}}", entry.getValue());
			}
		}
		return cTemplate;

	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	public Map<String, String> getReplacementParams() {
		return replacementParams;
	}

	public void setReplacementParams(Map<String, String> replacementParams) {
		this.replacementParams = replacementParams;
	}

//	Resource loading in java is called location independent because it is not relevant where your code is running, 
//	it just needs correct environment to find the resources.
//	This means that no matter where the application classes reside—a web server, the local filesystem, or even inside a JAR file or other archive

//	@Autowired
//	ResourceLoader resourceLoader;//ResourceLoader is used to loads resources from class-path as well as file system.
//	Resource resource=resourceLoader.getResource("classpath:preferences.json");
//	File file = resource.getFile()//resource.getInputStream() or resource.getFile() 

//	or

	// Getting ClassLoader obj
	// ClassLoader classLoader = this.getClass().getClassLoader();
	// Getting resource(File) from class loader
	// File configFile=new File(classLoader.getResource(fileName).getFile());

//	or

//	ResourceUtils.getFile("classpath:test.json");//It doesn’t check if the file exists or not.
}
