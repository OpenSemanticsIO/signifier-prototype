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

package io.opensemantics.prototype.signifier.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import io.opensemantics.prototype.signifier.core.MylynXmlTask;
import io.opensemantics.prototype.signifier.core.EclipseSearch;
import io.opensemantics.prototype.signifier.core.util.ModelHelper;
import io.opensemantics.prototype.signifier.ui.prefs.PreferenceConstants;
import io.opensemantics.prototype.signifier.ui.prefs.util.PreferenceValueConverter;
import io.opensemantics.signifier.api.Method;
import io.opensemantics.signifier.api.Search;

public class MainHandler {

  public MainHandler() {
  }
  
  // TODO : Make both below UI preferences
  private static final String SINKS = "/sinks.json";
  private static final String OUTPUT = "/Users/jonpasski/Desktop/mylyn-tasks-jon.xml.zip";
  
  @Inject ESelectionService selection;
  
  // Inject?
  final IPreferenceStore prefs = Activator.getDefault().getPreferenceStore();
  
  @Execute
  public void execute(Shell shell) {
    IProject project = getProject();

    if (project != null) {
      try {
        if (isJavaNature(project)) {
          addProjectToPreferences(project);
          searchForSinks();
        }
      } catch (CoreException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
  }

  private void addProjectToPreferences(IProject project) {
    final String projectStrings = prefs.getString(PreferenceConstants.P_PROJECT_LIST);
    final String projectString = project.getLocation().toPortableString();
    final String updatedProjects = PreferenceValueConverter.addProjectList(projectStrings, projectString);
    prefs.setValue(PreferenceConstants.P_PROJECT_LIST, updatedProjects);
  }
  
  private boolean isJavaNature(IProject project) throws CoreException {
    return project.isNatureEnabled("org.eclipse.jdt.core.javanature");
  }
  
  private void searchForSinks() {
    MylynXmlTask task = new MylynXmlTask(1000);

    Search search = new EclipseSearch(getJavaProjectsFromPrefs());
    try {
      @SuppressWarnings("unchecked")
      List<Method> callees = (List<Method>)ModelHelper.unmarshall(
          new StreamSource(getClass().getResourceAsStream(SINKS)),
          Method.class).getValue();

      if ((callees == null) || callees.isEmpty()) {
        System.out.println("The resource '" + SINKS + "' did not open correctly");
      }

      for (Method callee: callees) {
        List<Method> callers = search.callsites(callee);
        task.addCallSites(callee, callers);
      }
      task.writeTasks(new File(OUTPUT));
    } catch (JAXBException | CoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private IJavaProject[] getJavaProjectsFromPrefs() {
    List<IJavaProject> projects = new ArrayList<>();
    String pref = prefs.getString(PreferenceConstants.P_PROJECT_LIST);
    try {
      for (IProject project: PreferenceValueConverter.toProjects(pref)) {
        if (isJavaNature(project)) {
          projects.add(JavaCore.create(project));
        }
      }
    } catch (CoreException e) {
        e.printStackTrace();
    }
    return projects.toArray(new IJavaProject[projects.size()]);
  }

  // Inspired by
  // https://wiki.eclipse.org/FAQ_How_do_I_access_the_active_project%3F 
  private IProject getProject(){
    Object selected = selection.getSelection();
    if (!(selected instanceof IStructuredSelection)) return null;
    selected = ((IStructuredSelection)selected).getFirstElement();
    if (!(selected instanceof IProject)) return null;
    return (IProject)selected;
  }
}
