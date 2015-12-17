/**
 * Copyright 2015 OpenSemantics.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opensemantics.prototype.signifier.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;

import io.opensemantics.prototype.signifier.core.prefs.PreferenceService;
import io.opensemantics.signifier.api.Method;
import io.opensemantics.signifier.api.Search;

public class EclipseSearch implements Search {

  private IJavaProject[] projects;
  
  public EclipseSearch() {
    this.projects = PreferenceService.getJavaProjectsFromPreference();
  }

  public List<Method> callsites(Method callee) {
    SearchEngine engine = new SearchEngine();
    final int mask = IJavaSearchScope.SOURCES |
                     IJavaSearchScope.SYSTEM_LIBRARIES |
                     IJavaSearchScope.APPLICATION_LIBRARIES |
                     IJavaSearchScope.REFERENCED_PROJECTS;
    IJavaSearchScope scope = SearchEngine.createJavaSearchScope(projects, mask);
    IProgressMonitor monitor = null;  // TODO use a monitor
    int searchFor = IJavaSearchConstants.METHOD | IJavaSearchConstants.CONSTRUCTOR;
    int limitTo = IJavaSearchConstants.REFERENCES;
    int matchRule = SearchPattern.R_EXACT_MATCH; // SearchPattern.R_PATTERN_MATCH;
    final List<Method> methods = new ArrayList<>();

    SearchPattern pattern = SearchPattern.createPattern(toSearchString(callee), searchFor, limitTo, matchRule);
    try {
      engine.search(pattern,
                    new SearchParticipant[]{SearchEngine.getDefaultSearchParticipant()},
                    scope,
                    new SearchRequestor() {
                      @Override
                      public void acceptSearchMatch(SearchMatch match) throws CoreException {
                        final Object el = match.getElement();
                        if (el instanceof IMethod) {
                          final IMethod imethod = (IMethod)el;
                          methods.add(toMethod(imethod));
                        } else {
                          // TODO log? This seems like an odd error to 
                          // have, so logging isn't a bad idea
                        }
                      }
                    },
                    monitor);
    } catch (CoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return methods;
  }
  
  public static String toSearchString(Method method) {
    final StringBuilder builder = new StringBuilder(method.getTypeName());
    if (!method.isConstructor()) {
      builder.append(".");
      builder.append(method.getName());
    }
    builder.append("(").append(String.join(",", method.getParameters())).append(")");
    return builder.toString();
  }
  
  public static Method toMethod(IMethod imethod) throws JavaModelException {
    final Method method = new Method();
    method.setConstructor(imethod.isConstructor());
    method.setTypeName(imethod.getDeclaringType().getFullyQualifiedName());
    method.setName(imethod.getElementName());
    method.setParameters(Arrays.asList(imethod.getParameterTypes()));
    method.setBody(imethod.getSource());
    return method;
  }
}
