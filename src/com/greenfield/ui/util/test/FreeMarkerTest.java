package com.greenfield.ui.util.test;


import java.io.File;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.greenfield.common.tool.FreeMarkerTool;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerTest {

	public static void main(String[] args) {
		try {
			FreeMarkerTest.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public static void test() throws Exception {

	    //
	    // You will do this for several times in typical applications.
	    
	    // 2.1. Prepare the template input:
	    
	    Map<String, Object> input = new HashMap<String, Object>();
	    
	    input.put("query_string", "Vogella example");
	    input.put("query_field", "field example");
	    input.put("query_value", "value example");


	    //input.put("exampleObject", new ValueExampleObject("Java object", "me"));
	    
	    List<ValueExampleObject> systems = new ArrayList<ValueExampleObject>();
	    systems.add(new ValueExampleObject("Android", "Google"));
	    systems.add(new ValueExampleObject("iOS States", "Apple"));
	    systems.add(new ValueExampleObject("Ubuntu", "Canonical"));
	    systems.add(new ValueExampleObject("Windows7", "Microsoft"));
	    input.put("systems", systems);
	    
	    
	    
	    String output = FreeMarkerTool.makeOutput("config/freemarker/template/elastic_query.ftl", input);
	      

	    System.out.println("result: " +  output);


	    input = new HashMap<String, Object>();
	    
	    input.put("query_string", "Vogella example 2");
	    input.put("query_field", "field example 2 ");
	    input.put("query_value", "value example 2");


	    //input.put("exampleObject", new ValueExampleObject("Java object", "me"));
	    
	    List<ValueExampleObject> systems2 = new ArrayList<ValueExampleObject>();
	    systems2.add(new ValueExampleObject("Android 2", "Google"));
	    systems2.add(new ValueExampleObject("iOS States 2", "Apple"));
	    systems2.add(new ValueExampleObject("Ubuntu 2", "Canonical"));
	    systems2.add(new ValueExampleObject("Windows7 2", "Microsoft"));
	    input.put("systems", systems2);
	    
	    output = FreeMarkerTool.makeOutput("config/freemarker/template/elastic_query.ftl", input);
	      
	    System.out.println("result: " +  output);
	  }

}
