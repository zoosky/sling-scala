/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package org.apache.sling.launchpad.webapp.integrationtest;


import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;

import org.apache.sling.commons.testing.integration.HttpTestBase;


public class JSPSlingIncludeTest extends HttpTestBase {

	// Holds the URL's for top level child content mock nodes after created by the test frame work ex: contentUrlX means path is HTTP_BASE_URL + /root after created by testClient

	private String contentUrlX;
	private String contentUrlA;
	private String contentUrlB;
	private String contentUrlC;
	private String contentUrlD;
	private String contentUrlE;
	private String contentUrlF;
	private String contentUrlG;
	private String contentUrlDocument;

	// Text values of content types ex: /root means resource type /root if attached to resource type property of a content node.

	private String resourceTypeX;
	private String resourceTypeXA;
	private String resourceTypeXB;
	private String resourceTypeXC;
	private String resourceTypeXCA;
	private String resourceTypeXD;
	private String resourceTypeXDA;
	private String resourceTypeXE;
	private String resourceTypeXEA;
	private String resourceTypeXF;
	private String resourceTypeXG;
	private String resourceTypeDocument;

	// Preparing the environment for tests. i.e setting up nodes and assignig scripts.
	
	protected void setUp() throws Exception {
      
		super.setUp();

		Map<String,String> nodeProperties = new HashMap<String,String>();

		//first level resource /root
		

		resourceTypeX =  "/root";
      		nodeProperties.put("sling:resourceType", resourceTypeX);
      		nodeProperties.put("message", "This is Resource /root");
		contentUrlX = testClient.createNode(HTTP_BASE_URL +resourceTypeX , nodeProperties);

		//second level resource /root/a for testAddSelector

		resourceTypeXA = resourceTypeX + "/a";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeXA);
      		nodeProperties.put("message", "This is Resource /root/a from add selector");
      		contentUrlA = testClient.createNode(HTTP_BASE_URL +resourceTypeXA, nodeProperties);

		//second level resource /root/b for testReplaceSelector

		resourceTypeXB = resourceTypeX + "/b";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeXB);
      		nodeProperties.put("message", "This is Resource /root/b from replace selector");
      		contentUrlB = testClient.createNode(HTTP_BASE_URL +resourceTypeXB, nodeProperties);

		//second level resource /root/c for testIncludeWithResource

		resourceTypeXC = resourceTypeX + "/c";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeXC);
      		nodeProperties.put("message", "This is Resource root/c");
      		contentUrlC = testClient.createNode(HTTP_BASE_URL +resourceTypeXC, nodeProperties);

		//third level resource /root/c/a for testIncludeWithResource

		resourceTypeXCA = resourceTypeXC + "/a";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeXCA);
      		nodeProperties.put("message", "This is Resource root/c/a");
      		testClient.createNode(HTTP_BASE_URL +resourceTypeXCA, nodeProperties);


		//second level resource /root/d for testIncludeWithPath

		resourceTypeXD = resourceTypeX + "/d";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeXD);
      		nodeProperties.put("message", "This is Resource root/d");
      		contentUrlD = testClient.createNode(HTTP_BASE_URL +resourceTypeXD, nodeProperties);

		//third level resource /root/d/a for testIncludeWithPath

		resourceTypeXDA = resourceTypeXD + "/a";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeXDA);
      		nodeProperties.put("message", "This is Resource root/d/a");
      		testClient.createNode(HTTP_BASE_URL +resourceTypeXDA, nodeProperties);

		//second level resource /root/e for testIncludeWithSelectorInPath

		resourceTypeXE = resourceTypeX + "/e";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeXE);
      		nodeProperties.put("message", "This is Resource root/e");
      		contentUrlE = testClient.createNode(HTTP_BASE_URL +resourceTypeXE, nodeProperties);

		//third level resource /root/e/a for testIncludeWithSelectorInPath

		resourceTypeXEA = resourceTypeXE + "/a";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeXEA);
      		nodeProperties.put("message", "This is Resource root/e/a");
      		testClient.createNode(HTTP_BASE_URL +resourceTypeXEA, nodeProperties);

		//second level resource /root/e for testFlush

		resourceTypeXF = resourceTypeX + "/f";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeXF);
      		nodeProperties.put("message", "This is Resource root/f");
      		contentUrlF = testClient.createNode(HTTP_BASE_URL +resourceTypeXF, nodeProperties);

		//second level resource /root/e for testReplaceSuffix

		resourceTypeXG = resourceTypeX + "/g";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeXG);
      		nodeProperties.put("message", "This is Resource root/g");
      		contentUrlG = testClient.createNode(HTTP_BASE_URL +resourceTypeXG, nodeProperties);

		//Code for creating the doc node for custom script test
		
		resourceTypeDocument = "/doc";
        	nodeProperties.clear();
        	nodeProperties.put("sling:resourceType", resourceTypeDocument);
      		nodeProperties.put("message", "This is Resource /doc");
      		contentUrlDocument = testClient.createNode(HTTP_BASE_URL + resourceTypeDocument, nodeProperties);

      		uploadTestScript(resourceTypeX,"include/html.jsp","html.jsp");

      		uploadTestScript(resourceTypeXA,"include/addSelectorTest.jsp","html.jsp");	
      		uploadTestScript(resourceTypeXA,"include/selectorTest.jsp","selector.jsp");

      		uploadTestScript(resourceTypeXB,"include/replaceSelectorTest.jsp","html.jsp");
      		uploadTestScript(resourceTypeXB,"include/selectorTest.jsp","replace.jsp");

      		uploadTestScript(resourceTypeXC,"include/resourceTest.jsp","html.jsp");
      		uploadTestScript(resourceTypeXCA,"include/selectorTest.jsp", "html.jsp");

      		uploadTestScript(resourceTypeXD,"include/pathTest.jsp","html.jsp");
      		uploadTestScript(resourceTypeXDA,"include/selectorTest.jsp", "html.jsp");

      		uploadTestScript(resourceTypeXE,"include/pathSelectorTest.jsp","html.jsp");
      		uploadTestScript(resourceTypeXEA,"include/selectorTest.jsp", "selector.jsp");

      		uploadTestScript(resourceTypeXF,"include/flushTest.jsp","html.jsp");
      		uploadTestScript(resourceTypeXF,"include/selectorTest.jsp", "selector.jsp");
      		uploadTestScript(resourceTypeXF,"include/replaceTest.jsp", "replace.jsp");

      		uploadTestScript(resourceTypeXG,"include/suffix.jsp","html.jsp");
      		uploadTestScript(resourceTypeXG,"include/suffixTest.jsp", "suffixtest.jsp");

	}

	// Cleaning up initialized content after completing tests

	protected void tearDown() throws Exception {

		testClient.delete(contentUrlX);
		super.tearDown();

	}
    
	//Thjs test simply tests the message property of the current node without <sling:include />.

	public void testWithoutInclude() throws Exception{

		final String content = getContent(contentUrlX + ".html", CONTENT_TYPE_HTML);
		assertTrue("Message from included node",content.contains("This is Resource /root"));
		
	}

	// This test tests the addSelector parameter in <sling:include/> tag

	public void testAddSelector() throws Exception{

		final String content = getContent(contentUrlA + ".html", CONTENT_TYPE_HTML);
		assertTrue("Message from included node",content.contains("This is Resource /root/a from add selector"));
		
	}

	// This test tests the replaceSelector parameter in <sling:include/> tag.
	
	public void testReplaceSelector() throws Exception{

		final String content = getContent(contentUrlB + ".html", CONTENT_TYPE_HTML);
		assertTrue("Message from included node",content.contains("This is Resource /root/b from replace selector"));

	}

	//This test tests path parameter in <sling:include /> tag

	public void testIncludeWithPath() throws Exception{

		final String content = getContent(contentUrlD + ".html", CONTENT_TYPE_HTML);
		assertTrue("Message from included node",content.contains("This is Resource root/d/a"));

	}
	
	//This test tests resource parameter in <sling:include /> tag

	public void testIncludeWithResource() throws Exception{

		final String content = getContent(contentUrlC + ".html", CONTENT_TYPE_HTML);
		assertTrue("Message from included node",content.contains("This is Resource root/c/a"));

	}

	//This test tests path parameter in <sling:include /> tag providing a selector in the path.

	public void testIncludeWithSelectorInPath() throws Exception{

		final String content = getContent(contentUrlE + ".html", CONTENT_TYPE_HTML);
		assertTrue("Message from included node",content.contains("This is Resource root/e/a"));

	}	

	//This test is intended to test flush parameter in <sling:include \>. The logic here is if response.isCommitted()
	// returns true that means the output buffer is flushed and hence response is committed. 

	public void testFlush() throws Exception{

	        final String content = getContent(contentUrlF + ".html", CONTENT_TYPE_HTML);
		assertTrue("result of flush",content.contains("flush successful becasue commit is true"));		

	}

	//This test tests the replaceSuffix parameter in <sling:include /> tag

	public void testReplaceSuffix() throws Exception{
		
		final String content = getContent(contentUrlG + ".html/a.html" , CONTENT_TYPE_HTML);
		assertTrue("Test text from suffixTest",content.contains("The suffix in suffixTest.jsp is"));
		assertTrue("Replaced suffix from suffixTest",content.contains("a.b"));

	}

	//This is a custom test for documentation purposes

//	public void testCustomScriptInput() throws Exception{
		
//		uploadSlingIncludeScript("<sling:include replaceSelectors='suffixtest' replaceSuffix='/a.b' />" , "src/test/resources/integration-test/include/include.jsp");
//		String content = getContent(contentUrlDocument);

//		assertTrue("Replaced suffix from suffixTest",content.contains("a.b"));
//		System.out.println(content);

//	}


	public String getContent(String nodeURL) throws Exception{

	
	
		return getContent(nodeURL + ".html" , CONTENT_TYPE_HTML);


	
	}



	public void uploadSlingIncludeScript(String script , String mainScript){


		String initialScript = "";

 		try {

        		BufferedReader input = new BufferedReader(new FileReader(mainScript));
        		String str;

        		while ((str = input.readLine()) != null) {

            			initialScript = initialScript + str;

        		}

        		input.close();

   		} catch (IOException e) {}

		initialScript = initialScript + script;


		try {

        		BufferedWriter out = new BufferedWriter(new FileWriter("src/test/resources/integration-test/include/document.jsp"));

        		out.write(initialScript);
        		out.close();
			uploadTestScript(resourceTypeXG,"include/document.jsp" , "html.jsp");
      			//uploadTestScript(resourceTypeDocument,"include/suffixTest.jsp", "suffixtest.jsp");


		} catch (IOException e) {}

	}

}

