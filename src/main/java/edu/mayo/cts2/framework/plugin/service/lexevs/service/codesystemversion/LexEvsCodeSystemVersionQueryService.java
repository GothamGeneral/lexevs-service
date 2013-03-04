package edu.mayo.cts2.framework.plugin.service.lexevs.service.codesystemversion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.LexGrid.LexBIG.DataModel.Collections.CodingSchemeRenderingList;
import org.LexGrid.LexBIG.DataModel.InterfaceElements.CodingSchemeRendering;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntry;
import edu.mayo.cts2.framework.model.codesystemversion.CodeSystemVersionCatalogEntrySummary;
import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference;
import edu.mayo.cts2.framework.model.core.OpaqueData;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.core.SourceReference;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.DocumentedNamespaceReference;
import edu.mayo.cts2.framework.plugin.service.lexevs.service.AbstractLexEvsService;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQuery;
import edu.mayo.cts2.framework.service.profile.codesystemversion.CodeSystemVersionQueryService;
@Component
public class LexEvsCodeSystemVersionQueryService extends AbstractLexEvsService
		implements CodeSystemVersionQueryService {

	// ------ Local methods ----------------------
	private CodingSchemeToCodeSystemTransform codingSchemeTransformer = new CodingSchemeToCodeSystemTransform();

	public CodingSchemeToCodeSystemTransform getCodingSchemeTransformer() {
		return codingSchemeTransformer;
	}

	public void setCodingSchemeTransformer(
			CodingSchemeToCodeSystemTransform codingSchemeTransformer) {
		this.codingSchemeTransformer = codingSchemeTransformer;
	}

	// -------- Implemented methods ----------------
	@Override
	public int count(CodeSystemVersionQuery arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public DirectoryResult<CodeSystemVersionCatalogEntry> getResourceList(
			CodeSystemVersionQuery arg0, SortCriteria arg1, Page arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public DirectoryResult<CodeSystemVersionCatalogEntrySummary> getResourceSummaries(
			CodeSystemVersionQuery query, SortCriteria sortCriteria, Page page) {

		LexBIGService lexBigService = getLexBigService();
		ArrayList<CodeSystemVersionCatalogEntrySummary> list = new ArrayList<CodeSystemVersionCatalogEntrySummary>();
		DirectoryResult<CodeSystemVersionCatalogEntrySummary> directoryResult = null;
		boolean atEnd = false;
		try {
			CodingSchemeRenderingList csrList = lexBigService.getSupportedCodingSchemes();
			CodingSchemeRendering[] csRendering = csrList.getCodingSchemeRendering();
			for(CodingSchemeRendering render : csRendering){
				list.add(codingSchemeTransformer.transform(render));
			}
			
			ArrayList<CodeSystemVersionCatalogEntrySummary> sublist = new ArrayList<CodeSystemVersionCatalogEntrySummary>();
			int start = page.getStart();
			int end = page.getEnd();
			int i = 0;
			for(i = start; i < end && i < list.size(); i++){
				sublist.add(list.get(i));
			}

			if(i == list.size()){
				atEnd = true;
			}
			
			directoryResult = new DirectoryResult<CodeSystemVersionCatalogEntrySummary>(sublist, atEnd);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return directoryResult;
	}

	@Override
	public List<DocumentedNamespaceReference> getKnownNamespaceList() {
		throw new UnsupportedOperationException();
	}

	@Override
	public OpaqueData getServiceDescription() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getServiceName() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public SourceReference getServiceProvider() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getServiceVersion() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<PredicateReference> getKnownProperties() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<? extends MatchAlgorithmReference> getSupportedMatchAlgorithms() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<? extends PropertyReference> getSupportedSearchReferences() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<? extends PropertyReference> getSupportedSortReferences() {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}
