package edu.mayo.cts2.framework.plugin.service.lexevs.service.resolvedvalueset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.transform.stream.StreamResult;

import org.LexGrid.LexBIG.test.LexEvsTestRunner.LoadContent;
import org.junit.Test;
import org.springframework.oxm.XmlMappingException;

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.EntitySynopsis;
import edu.mayo.cts2.framework.model.core.ScopedEntityName;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.entity.EntityDirectoryEntry;
import edu.mayo.cts2.framework.model.service.core.EntityNameOrURI;
import edu.mayo.cts2.framework.model.util.ModelUtils;
import edu.mayo.cts2.framework.plugin.service.lexevs.test.AbstractTestITBase;
import edu.mayo.cts2.framework.plugin.service.lexevs.utility.CommonTestUtils;
import edu.mayo.cts2.framework.service.command.restriction.ResolvedValueSetResolutionEntityRestrictions;
import edu.mayo.cts2.framework.service.meta.StandardMatchAlgorithmReference;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.ResolvedValueSetResolutionService;
import edu.mayo.cts2.framework.service.profile.resolvedvalueset.name.ResolvedValueSetReadId;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ResolvedValueSetResult;

@LoadContent(contentPath = "lexevs/test-content/valueset/ResolvedAllDomesticAutosAndGM.xml")
public class LexEVSResolvedValuesetResolutionServiceTestIT extends
		AbstractTestITBase {

	@Resource
	private ResolvedValueSetResolutionService service;
	@Resource
	Cts2Marshaller marshaller;

	// ---- Test methods ----
	@Test
	public void testSetUp() {
		assertNotNull(this.service);
	}

	@Test
	public void testGetRolution() throws Exception {

		ResolvedValueSetReadId identifier = new ResolvedValueSetReadId("All Domestic Autos AND GM-06736a30878a0f8bd0ea83196732380a",
				ModelUtils.nameOrUriFromName("All Domestic Autos AND GM"),
				ModelUtils.nameOrUriFromName("All Domestic Autos AND GM-06736a30878a0f8bd0ea83196732380a"));
		DirectoryResult<EntitySynopsis> dirResult = service.getResolution(
				identifier, null, new Page());

		assertNotNull(dirResult);
		int expecting = 1;
		int actual = dirResult.getEntries().size();
		assertEquals("Expecting " + expecting + " but got " + actual,
				expecting, actual);
	}

	@Test
	public void testGetResolutionEntitiesNoFilter() {
		ResolvedValueSetReadId identifier = new ResolvedValueSetReadId("SRITEST:AUTO:AllDomesticANDGM-06736a30878a0f8bd0ea83196732380a",
				ModelUtils.nameOrUriFromName("SRITEST:AUTO:AllDomesticANDGM"),
				ModelUtils.nameOrUriFromName("All Domestic Autos AND GM-06736a30878a0f8bd0ea83196732380a"));
		DirectoryResult<EntityDirectoryEntry> dirResult = service.getEntities(
				identifier, null, null, new Page());
		assertTrue(dirResult.getEntries().size() > 0);

	}

	@Test
	public void testGetResolutionEntitiesNoFilterValidXML() throws Exception {
		ResolvedValueSetReadId identifier = new ResolvedValueSetReadId("SRITEST:AUTO:AllDomesticANDGM-06736a30878a0f8bd0ea83196732380a",
				ModelUtils.nameOrUriFromName("SRITEST:AUTO:AllDomesticANDGM"),
				ModelUtils.nameOrUriFromName("All Domestic Autos AND GM-06736a30878a0f8bd0ea83196732380a"));
		DirectoryResult<EntityDirectoryEntry> dirResult = service.getEntities(
				identifier, null, null, new Page());
		
		for(EntityDirectoryEntry entry : dirResult.getEntries()){
			StreamResult result = new StreamResult(new StringWriter());
			marshaller.marshal(entry, result);	
		}
	}
	
	@Test
	public void testGetResolutionEntitiesWithFilter() {
		ResolvedValueSetReadId identifier = new ResolvedValueSetReadId("SRITEST:AUTO:AllDomesticANDGM-06736a30878a0f8bd0ea83196732380a",
				ModelUtils.nameOrUriFromName("SRITEST:AUTO:AllDomesticANDGM"),
				ModelUtils.nameOrUriFromName("All Domestic Autos AND GM-06736a30878a0f8bd0ea83196732380a"));
		Set<ResolvedFilter> filter = CommonTestUtils.createFilterSet(StandardModelAttributeReference.RESOURCE_NAME.getPropertyReference(), 
		  		  StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(), 
		  		"GM");
		
		ResolvedValueSetResolutionQueryImpl query= new ResolvedValueSetResolutionQueryImpl();
		query.setFilterComponent(filter);
		DirectoryResult<EntityDirectoryEntry> dirResult = service.getEntities(
				identifier, query, null, new Page());
		assertTrue(dirResult.getEntries().size() > 0);

	}	
	
	
	
	@Test
	public void testGetResolutionEntitiesWithEntityRestriction() {
		ResolvedValueSetReadId identifier = new ResolvedValueSetReadId("SRITEST:AUTO:AllDomesticANDGM-06736a30878a0f8bd0ea83196732380a",
				ModelUtils.nameOrUriFromName("SRITEST:AUTO:AllDomesticANDGM"),
				ModelUtils.nameOrUriFromName("All Domestic Autos AND GM-06736a30878a0f8bd0ea83196732380a"));
		
		ResolvedValueSetResolutionQueryImpl query= new ResolvedValueSetResolutionQueryImpl();
		ResolvedValueSetResolutionEntityRestrictions entityRestriction= new ResolvedValueSetResolutionEntityRestrictions();
		EntityNameOrURI entity = new EntityNameOrURI();
		ScopedEntityName scopedEntityName = new ScopedEntityName();
		//scopedEntityName.setNamespace("Automobiles");
		scopedEntityName.setName("GM");
		entity.setEntityName(scopedEntityName);
		Set<EntityNameOrURI> entities= new HashSet<EntityNameOrURI>();
		entities.add(entity);
		entityRestriction.setEntities(entities);
		query.setResolvedValueSetResolutionEntityRestrictions(entityRestriction);
		query.setResolvedValueSetResolutionEntityRestrictions(entityRestriction);
		DirectoryResult<EntityDirectoryEntry> dirResult = service.getEntities(
				identifier, query, null, new Page());
		assertTrue(dirResult.getEntries().size() > 0);

	}	
	
	@Test
	public void testGetResolutionValidXML() throws Exception {
		ResolvedValueSetReadId identifier = new ResolvedValueSetReadId("SRITEST:AUTO:AllDomesticANDGM-06736a30878a0f8bd0ea83196732380a",
				ModelUtils.nameOrUriFromName("SRITEST:AUTO:AllDomesticANDGM"),
				ModelUtils.nameOrUriFromName("All Domestic Autos AND GM-06736a30878a0f8bd0ea83196732380a"));

		ResolvedValueSetResult<EntitySynopsis> dirResult = service.getResolution(
				identifier, null, new Page());

		for (EntitySynopsis synopsis: dirResult.getEntries()) {
			
			StringWriter sw = new StringWriter();
			StreamResult result= new StreamResult(sw);
			marshaller.marshal(synopsis, result);
			System.out.println(sw.toString());
		}		
	}
	
	@Test
	public void testGetResolutionValidHasValidHeader() throws Exception {
		ResolvedValueSetReadId identifier = new ResolvedValueSetReadId("SRITEST:AUTO:AllDomesticANDGM-06736a30878a0f8bd0ea83196732380a",
				ModelUtils.nameOrUriFromName("SRITEST:AUTO:AllDomesticANDGM"),
				ModelUtils.nameOrUriFromName("All Domestic Autos AND GM-06736a30878a0f8bd0ea83196732380a"));
		
		Set<ResolvedFilter> filter = CommonTestUtils.createFilterSet(StandardModelAttributeReference.RESOURCE_NAME.getPropertyReference(), 
		  		  StandardMatchAlgorithmReference.CONTAINS.getMatchAlgorithmReference(), 
		  		"GM");
		
		ResolvedValueSetResult<EntitySynopsis> dirResult = service.getResolution(
				identifier, filter, new Page());

		assertNotNull(dirResult.getResolvedValueSetHeader());
		
		StreamResult result = new StreamResult(new StringWriter());
		marshaller.marshal(dirResult.getResolvedValueSetHeader(), result);		
	}

}
