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

package io.opensemantics.prototype.signifier.ui.prefs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

import io.opensemantics.prototype.signifier.ui.prefs.PreferenceConstants;

public class PreferenceValueConverter {

  public static final String PROJECT_DELIMINATOR = "||";

  public static String toProjectString(String[] list) {
    return Arrays.stream(list).collect(Collectors.joining(PROJECT_DELIMINATOR));
  }

  public static String addProjectList(String list, String add) {
    return toProjectString(Stream.concat(Arrays.stream(toProjectList(list)),
                                         Arrays.stream(new String[]{add}))
                                 .toArray(String[]::new));
  }
  
  public static String[] toProjectList(String list) {
    if ((list == null) || (list.equals(""))) return new String[]{};
    return list.split(Pattern.quote(PROJECT_DELIMINATOR));
  }
  
  /**
   * 
   * @param   Un-parsed preference string list of projects
   * @return  An array of IProjects from the parsed preferences string
   */
  public static List<IProject> toProjects(String string) {
    final List<IProject> projects = new ArrayList<>();
    for (String project: toProjectList(string)) {
      try {
        IProjectDescription desc = ResourcesPlugin.getWorkspace()
            .loadProjectDescription(new Path(project).append(".project"));
        projects.add(ResourcesPlugin.getWorkspace().getRoot().getProject(desc.getName()));
      } catch (CoreException e) {
        // TODO better handling (maybe popup?)
        // TODO better logging
        StringBuilder error = new StringBuilder("Invalid path: ");
        error.append(e.getMessage());
      }
    }
    return projects;
  }
}
