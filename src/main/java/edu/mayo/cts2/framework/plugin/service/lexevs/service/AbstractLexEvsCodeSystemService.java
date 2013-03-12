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
package edu.mayo.cts2.framework.plugin.service.lexevs.service;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.codingSchemes.CodingScheme;

import edu.mayo.cts2.framework.model.service.core.NameOrURI;

/**
 * A base service for all services needing to deal with LexEVS CodingSchemes.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractLexEvsCodeSystemService<T> extends AbstractLexEvsService {
	
	protected abstract T transform(CodingScheme codingScheme);
	
	/**
	 * Gets the code system by version id or tag.
	 *
	 * @param codeSystem the code system
	 * @param versionIdOrTag the version id or tag
	 * @return the code system by version id or tag
	 */
	protected T getByVersionIdOrTag(
			NameOrURI codeSystem, CodingSchemeVersionOrTag versionIdOrTag){
		String nameOrUri;
		if(codeSystem.getName() != null){
			nameOrUri = codeSystem.getName();
		} else {
			nameOrUri = codeSystem.getUri();
		}
		
		CodingScheme codingScheme;
		try {
			codingScheme = this.getLexBigService().resolveCodingScheme(nameOrUri, versionIdOrTag);
		} catch (LBException e) {
			//this could be just that LexEVS didn't find it. If so, return null.
			log.warn(e);
			return null;
		}
		
		return this.transform(codingScheme);
	}
}