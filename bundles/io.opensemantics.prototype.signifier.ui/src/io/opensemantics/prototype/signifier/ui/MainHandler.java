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
import java.util.List;

import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;

import io.opensemantics.prototype.signifier.core.EclipseSearch;
import io.opensemantics.prototype.signifier.core.MylynXmlTask;
import io.opensemantics.prototype.signifier.core.prefs.PreferenceService;
import io.opensemantics.prototype.signifier.core.util.ModelHelper;
import io.opensemantics.signifier.api.Method;
import io.opensemantics.signifier.api.Search;

public class MainHandler {

  public MainHandler() {
  }
  
  // TODO : Make both below UI preferences
  private static final String SINKS = "/sinks.json";
  private static final String OUTPUT = "/Users/jonpasski/Desktop/mylyn-tasks-jon.xml.zip";
  
  @Inject ESelectionService selection;
  
  @Execute
  public void execute(Shell shell) {
    IProject project = getProject();
    if (project != null) {
      PreferenceService.addProjectToPreferences(project);
      searchForSinks();
    }
  }

  private void searchForSinks() {
    MylynXmlTask task = new MylynXmlTask(1000);

    Search search = new EclipseSearch();
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
