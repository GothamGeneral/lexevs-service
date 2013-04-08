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
package edu.mayo.cts2.framework.plugin.service.lexevs.service.mapentry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import javax.annotation.Resource;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContents;
import org.junit.Test;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.command.ResolvedReadContext;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.mapversion.MapEntry;
import edu.mayo.cts2.framework.model.service.core.NameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.lexevs.test.AbstractTestITBase;
import edu.mayo.cts2.framework.service.profile.mapentry.name.MapEntryReadId;

/**
 *  @author <a href="mailto:frutiger.kim@mayo.edu">Kim Frutiger</a>
 *  @author <a href="mailto:hardie.linda@mayo.edu">Linda Hardie</a>
 *
 */
public class LexEvsMapEntryReadServiceTestIT extends AbstractTestITBase {

	@Resource
	private LexEvsMapEntryReadService service;
	
	@Resource
	private Cts2Marshaller marshaller;

	@Test
	public void testSetUp() {
		assertNotNull(this.service);
	}

	@Test
	@LoadContents(
		{
			@LoadContent(contentPath="lexevs/test-content/German_Made_Parts.xml"),
			@LoadContent(contentPath="lexevs/test-content/Automobiles.xml"),
			@LoadContent(contentPath="lexevs/test-content/testMapping.xml")
		}
	)
	public void testRead() {
		
		String sourceEntityCode = "A0001";
		ScopedEntityName mapFromName = new ScopedEntityName();
		mapFromName.setName(sourceEntityCode);

		String mapCodingSchemeURI = "urn:oid:mapping:sample";
		NameOrURI mapVersion = ModelUtils.nameOrUriFromUri(mapCodingSchemeURI);
		MapEntryReadId identifier = new MapEntryReadId(mapFromName, mapVersion);
		
		ResolvedReadContext readContext = null;
		
		MapEntry mapEntry = this.service.read(identifier, readContext);
		assertNotNull(mapEntry);
				
	}

	@Test
	@LoadContents(
		{
			@LoadContent(contentPath="lexevs/test-content/German_Made_Parts.xml"),
			@LoadContent(contentPath="lexevs/test-content/Automobiles.xml"),
			@LoadContent(contentPath="lexevs/test-content/testMapping.xml")
		}
	)
	public void testReadEntityCodeNotFound() {
		
		String sourceEntityCode = "A0001zzz";
		ScopedEntityName mapFromName = new ScopedEntityName();
		mapFromName.setName(sourceEntityCode);

		String mapCodingSchemeURI = "urn:oid:mapping:sample";
		NameOrURI mapVersion = ModelUtils.nameOrUriFromUri(mapCodingSchemeURI);
		MapEntryReadId identifier = new MapEntryReadId(mapFromName, mapVersion);
		
		ResolvedReadContext readContext = null;
		
		MapEntry mapEntry = this.service.read(identifier, readContext);
		assertNull(mapEntry);
				
	}

	@Test(expected=RuntimeException.class)
	@LoadContents(
		{
			@LoadContent(contentPath="lexevs/test-content/German_Made_Parts.xml"),
			@LoadContent(contentPath="lexevs/test-content/Automobiles.xml"),
			@LoadContent(contentPath="lexevs/test-content/testMapping.xml")
		}
	)
	public void testReadMapVersionNotFound() {
		
		String sourceEntityCode = "A0001";
		ScopedEntityName mapFromName = new ScopedEntityName();
		mapFromName.setName(sourceEntityCode);

		String mapCodingSchemeURI = "urn:oid:mapping:samplezzz";
		NameOrURI mapVersion = ModelUtils.nameOrUriFromUri(mapCodingSchemeURI);
		MapEntryReadId identifier = new MapEntryReadId(mapFromName, mapVersion);
		
		ResolvedReadContext readContext = null;
		
		@SuppressWarnings("unused")
		MapEntry mapEntry = this.service.read(identifier, readContext);
	}

	@Test
	@LoadContents(
			{
				@LoadContent(contentPath="lexevs/test-content/German_Made_Parts.xml"),
				@LoadContent(contentPath="lexevs/test-content/Automobiles.xml"),
				@LoadContent(contentPath="lexevs/test-content/testMapping.xml")
			}
	)
	public void testExistsFound() {
		String sourceEntityCode = "Ford";
		ScopedEntityName mapFromName = new ScopedEntityName();
		mapFromName.setName(sourceEntityCode);

		String mapCodingSchemeURI = "urn:oid:mapping:sample";
		NameOrURI mapVersion = ModelUtils.nameOrUriFromUri(mapCodingSchemeURI);
		MapEntryReadId identifier = new MapEntryReadId(mapFromName, mapVersion);
		
		ResolvedReadContext readContext = null;
		
		assertTrue(this.service.exists(identifier, readContext));		
	}

	@Test
	@LoadContents(
			{
				@LoadContent(contentPath="lexevs/test-content/German_Made_Parts.xml"),
				@LoadContent(contentPath="lexevs/test-content/Automobiles.xml"),
				@LoadContent(contentPath="lexevs/test-content/testMapping.xml")
			}
	)
	public void testExistsNotFound() {
		String sourceEntityCode = "Fjord";
		ScopedEntityName mapFromName = new ScopedEntityName();
		mapFromName.setName(sourceEntityCode);

		String mapCodingSchemeURI = "urn:oid:mapping:sample";
		NameOrURI mapVersion = ModelUtils.nameOrUriFromUri(mapCodingSchemeURI);
		MapEntryReadId identifier = new MapEntryReadId(mapFromName, mapVersion);
		
		ResolvedReadContext readContext = null;
		
		assertFalse(this.service.exists(identifier, readContext));				
	}

}
