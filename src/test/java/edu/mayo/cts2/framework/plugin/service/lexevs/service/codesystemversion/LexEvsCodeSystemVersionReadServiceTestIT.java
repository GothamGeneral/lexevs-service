/*
* Copyright: (c) 2004-2013 Mayo Foundation for Medical Education and
* Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
* triple-shield Mayo logo are trademarks and service marks of MFMER.
*
* Except as contained in the copyright notice above, or as used to identify
* MFMER as the author of this software, the trade names, trademarks, service
* marks, or product names of the copyright holder shall not be used in
* advertising, promotion or otherwise in connection with this software without
* prior written authorization of the copyright holder.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package edu.mayo.cts2.framework.plugin.service.lexevs.service.codesystemversion;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.junit.Test;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.VersionTagReference;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.lexevs.test.AbstractTestITBase;
import edu.mayo.cts2.framework.plugin.service.lexevs.utility.Constants;


public class LexEvsCodeSystemVersionReadServiceTestIT extends AbstractTestITBase {
	
	@Resource
	private LexEvsCodeSystemVersionReadService service;
	
	@Resource
	private Cts2Marshaller marshaller;

	@Test
	public void testSetUp() {
		assertNotNull(this.service);
	}
	
	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testReadByOfficialVersionId() throws Exception {
		NameOrURI name = ModelUtils.nameOrUriFromName("Automobiles");
		
		assertNotNull(this.service.getCodeSystemByVersionId(name, "1.0", null));
	}
	
	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testReadByTag() throws Exception {
		String nameOrUri = "Automobiles";
		NameOrURI codeSystem = ModelUtils.nameOrUriFromName(nameOrUri);
		VersionTagReference tag = Constants.CURRENT_TAG; 
		ResolvedReadContext readContext = null;
		CodeSystemVersionCatalogEntry csvCatalogEntry = this.service.readByTag(codeSystem, tag, readContext);
		assertNotNull(csvCatalogEntry);
		
		// Verify LexEVS to CTS2 transform worked 
		assertNotNull(csvCatalogEntry.getFormalName());
		assertEquals("Formal name not transformed - ", "autos", csvCatalogEntry.getFormalName());
		assertNotNull(csvCatalogEntry.getCodeSystemVersionName());
		assertEquals("CodeSystemVersionName not transformed - ","Automobiles-1.0",csvCatalogEntry.getCodeSystemVersionName());
		assertNotNull(csvCatalogEntry.getDocumentURI());
		assertEquals("DocumentURI not transformed - ","urn:oid:11.11.0.1",csvCatalogEntry.getDocumentURI());		
		assertNotNull(csvCatalogEntry.getAbout());
		assertEquals("About not transformed - ","urn:oid:11.11.0.1",csvCatalogEntry.getAbout());		
		assertNotNull(csvCatalogEntry.getResourceSynopsis());
		assertNotNull(csvCatalogEntry.getResourceSynopsis().getValue());
		assertNotNull(csvCatalogEntry.getResourceSynopsis().getValue().getContent());
		assertEquals("Resource Synopsis not transformed - ","Automobiles",csvCatalogEntry.getResourceSynopsis().getValue().getContent());
		assertNotNull(csvCatalogEntry.getKeyword());
		assertEquals("Number of KeyWords not transformed correctly - ",3,csvCatalogEntry.getKeywordCount());
		String[] keyWordsArray = csvCatalogEntry.getKeyword();
		assertEquals("KeyWord value not transformed correctly - ","11.11.0.1",keyWordsArray[0]);
		assertEquals("KeyWord value not transformed correctly - ","Automobiles",keyWordsArray[1]);
		assertEquals("KeyWord value not transformed correctly - ","SomeOtherValue",keyWordsArray[2]);		
	}

	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testReadByTagNotFound() throws Exception {
		String nameOrUri = "Automooobiles";
		NameOrURI codeSystem = ModelUtils.nameOrUriFromName(nameOrUri);
		VersionTagReference tag = Constants.CURRENT_TAG; 
		ResolvedReadContext readContext = null;
		CodeSystemVersionCatalogEntry csvCatalogEntry = this.service.readByTag(codeSystem, tag, readContext);
		assertNull(csvCatalogEntry);
	}

	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testRead() throws Exception {
		String nameOrUri = "Automobiles-1.0";
		NameOrURI identifier = ModelUtils.nameOrUriFromName(nameOrUri);
		ResolvedReadContext readContext = null;
		CodeSystemVersionCatalogEntry csvCatalogEntry = this.service.read(identifier, readContext);
		assertNotNull(csvCatalogEntry);		
	}

	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testReadValidXML() throws Exception {
		String nameOrUri = "Automobiles-1.0";
		NameOrURI identifier = ModelUtils.nameOrUriFromName(nameOrUri);
		ResolvedReadContext readContext = null;
		CodeSystemVersionCatalogEntry csvCatalogEntry = this.service.read(identifier, readContext);
		
		marshaller.marshal(csvCatalogEntry, new StreamResult(new StringWriter()));		
	}
	
	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testRead_ErrorWithoutDash() throws Exception {
		String nameOrUri = "Automobiles1.0";
		NameOrURI identifier = ModelUtils.nameOrUriFromName(nameOrUri);
		ResolvedReadContext readContext = null;
		CodeSystemVersionCatalogEntry csvCatalogEntry = this.service.read(identifier, readContext);
		assertNull(csvCatalogEntry);		
	}

	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testRead_WithSpace() throws Exception {
		String nameOrUri = "Automobiles - 1.0";
		NameOrURI identifier = ModelUtils.nameOrUriFromName(nameOrUri);
		ResolvedReadContext readContext = null;
		CodeSystemVersionCatalogEntry csvCatalogEntry = this.service.read(identifier, readContext);
		assertNull(csvCatalogEntry);		
	}

	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testReadNotFound() throws Exception {
		String nameOrUri = "Automooobiles-1.0";
		NameOrURI identifier = ModelUtils.nameOrUriFromName(nameOrUri);
		ResolvedReadContext readContext = null;
		CodeSystemVersionCatalogEntry csvCatalogEntry = this.service.read(identifier, readContext);
		assertNull(csvCatalogEntry);		
	}

	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testExistsTrue() throws Exception {
		String nameOrUri = "Automobiles-1.0";
		NameOrURI identifier = ModelUtils.nameOrUriFromName(nameOrUri);
		ResolvedReadContext readContext = null;
		assertTrue(this.service.exists(identifier, readContext));
	}
	
	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testExistsFalse() throws Exception {
		String nameOrUri = "Automooobiles-1.0";
		NameOrURI identifier = ModelUtils.nameOrUriFromName(nameOrUri);
		ResolvedReadContext readContext = null;
		assertFalse(this.service.exists(identifier, readContext));
	}
	
	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testExistsByTagTrue() throws Exception {
		String nameOrUri = "Automobiles";
		NameOrURI identifier = ModelUtils.nameOrUriFromName(nameOrUri);
		VersionTagReference tag = Constants.CURRENT_TAG; 
		ResolvedReadContext readContext = null;
		assertTrue(this.service.existsByTag(identifier, tag, readContext));
	}
	
	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testExistsByTagFalse() throws Exception {
		String nameOrUri = "Automooobiles";
		NameOrURI identifier = ModelUtils.nameOrUriFromName(nameOrUri);
		VersionTagReference tag = Constants.CURRENT_TAG; 
		ResolvedReadContext readContext = null;
		assertFalse(this.service.existsByTag(identifier, tag, readContext));
	}
	
	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testExistsByVersionIdTrue() throws Exception {
		NameOrURI codeSystem = ModelUtils.nameOrUriFromName("Automobiles");
		String officialResourceVersionId = "1.0";
		assertTrue(this.service.existsVersionId(codeSystem, officialResourceVersionId));
	}
	
	@Test
	@LoadContent(contentPath="lexevs/test-content/Automobiles.xml")
	public void testExistsByVersionIdFalse() throws Exception {
		NameOrURI codeSystem = ModelUtils.nameOrUriFromName("Automooobiles");
		String officialResourceVersionId = "1.0";
		assertFalse(this.service.existsVersionId(codeSystem, officialResourceVersionId));
	}	

}
