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
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

import org.apache.sling.commons.testing.integration.HttpTestBase;



public class Sling475Test extends HttpTestBase {
 

    private String contentUrlX;
    private String contentUrlY;
    private String contentUrlZ;
    private String contentUrlP;

    private String resourceTypeX;
    private String resourceTypeXY;
    private String resourceTypeXZ;
    private String resourceTypeXP;


    @Override
    protected void setUp() throws Exception {
      
	super.setUp();

		
	Map<String,String> props = new HashMap<String,String>();

	resourceTypeX =  "/x";
      props.put("sling:resourceType", resourceTypeX);
	contentUrlX = testClient.createNode(HTTP_BASE_URL +resourceTypeX , props);


	resourceTypeXY = resourceTypeX + "/y";
        props.clear();
        props.put("sling:resourceType", resourceTypeXY);
      contentUrlY = testClient.createNode(HTTP_BASE_URL +resourceTypeXY, props);

	resourceTypeXZ = resourceTypeX + "/z";
        props.clear();
        props.put("sling:resourceType", resourceTypeXZ);
      contentUrlZ = testClient.createNode(HTTP_BASE_URL +resourceTypeXZ, props);	

	resourceTypeXP = resourceTypeX + "/p";
        props.clear();
        props.put("sling:resourceType", resourceTypeXP);
      contentUrlP = testClient.createNode(HTTP_BASE_URL +resourceTypeXP, props);	

      uploadTestScript(resourceTypeX,"path-resource-test.jsp","html.jsp");	
      uploadTestScript(resourceTypeXY,"selector-test.jsp","html.jsp");
      uploadTestScript(resourceTypeXY,"add-selector.jsp","select.jsp");
      uploadTestScript(resourceTypeXY,"replace-selector.jsp","replace.jsp");
      uploadTestScript(resourceTypeXZ,"resource-test.jsp","html.jsp");
      uploadTestScript(resourceTypeXP,"flush-test.jsp","html.jsp");
      uploadTestScript(resourceTypeXP,"add-selector.jsp","select.jsp");
      uploadTestScript(resourceTypeXP,"replace-selector.jsp","replace.jsp");

    }

    @Override
    protected void tearDown() throws Exception {

        testClient.delete(contentUrlX);
        super.tearDown();
    }
    
    public void testSelectors() throws Exception {

	  // This test tests addSelectors and replaceSelector tag options

        final String content = getContent(contentUrlY + ".html", CONTENT_TYPE_HTML);
        assertTrue("text content from resource y with addSelector only",content.contains("Embedding content from node y selector script"));
        assertTrue("text content from resource y with replaceSelector",content.contains("Embedding content from node y replace selector script"));
        assertTrue("node path for resource y",content.contains("/x/y"));
    }

    public void testIncludeUsingResourceAndPath() throws Exception {

	  //This test tests path and resource options along with resourceType


        final String content = getContent(contentUrlX + ".html", CONTENT_TYPE_HTML);

	  // Content from path

        assertTrue("text content from resource y with addSelector only",content.contains("Embedding content from node y selector script"));
        assertTrue("text content from resource y with replaceSelector",content.contains("Embedding content from node y replace selector script"));
        assertTrue("node path for resource y",content.contains("/x/y"));

	  // Content from resource

        assertTrue("text content from resource z",content.contains("Embedding the formatting of resource z"));
        assertTrue("node path for resource z",content.contains("z"));
    }


    public void testWithoutInclude() throws Exception {

	  //This test is for currentNode without the include tag.

        final String content = getContent(contentUrlZ + ".html", CONTENT_TYPE_HTML);
        assertTrue("text content from resource z",content.contains("Embedding the formatting of resource z"));
        assertTrue("node path for resource z",content.contains("/x/z"));
    }

    public void testFlush() throws Exception {

	  //This test is intended to test flush option in <sling:include \>. The logic here is if response.isCommitted()
	  // returns true that means the output buffer is flushed and hence response is committed. 

        final String content = getContent(contentUrlP + ".html", CONTENT_TYPE_HTML);
        assertTrue("result of flush",content.contains("Flush Successful becasue commit is true"));
    }

   
}

