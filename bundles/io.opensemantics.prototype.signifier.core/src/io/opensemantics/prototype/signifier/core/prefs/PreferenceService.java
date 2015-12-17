package io.opensemantics.prototype.signifier.core.prefs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class PreferenceService {

  public PreferenceService() {
    // TODO Auto-generated constructor stub
  }

  public static IEclipsePreferences getNode() {
    return InstanceScope.INSTANCE.getNode(PreferenceConstants.P_NODE_PATH);
  }
  
  public static void addProjectToPreferences(IProject project) {
    if (isValidProject(project)) {    
      IEclipsePreferences prefs = getNode();
      final String projectStrings = prefs.get(PreferenceConstants.P_PROJECT_LIST, "");
      final String projectString = project.getLocation().append(".project").toPortableString();    
      final String updatedProjects = PreferenceValueConverter.addProjectList(projectStrings, projectString);
      prefs.put(PreferenceConstants.P_PROJECT_LIST, updatedProjects);
    }
  }

  public static IProject[] getProjectsFromPreference() {
    IEclipsePreferences prefs = getNode();
    String projectList = prefs.get(PreferenceConstants.P_PROJECT_LIST, "");
    final List<IProject> projects = new ArrayList<>();

    for (String project: PreferenceValueConverter.toProjectList(projectList)) {
      try {
        IProjectDescription desc = ResourcesPlugin.getWorkspace()
            .loadProjectDescription(new Path(project));
        projects.add(ResourcesPlugin.getWorkspace().getRoot().getProject(desc.getName()));
      } catch (CoreException e) {
        // TODO better logging
        StringBuilder error = new StringBuilder("Invalid path: ");
        error.append(e.getMessage());
      }
    }

    return projects.toArray(new IProject[projects.size()]);
  }
  
  public static IJavaProject[] getJavaProjectsFromPreference() {
    List<IJavaProject> projects = new ArrayList<>();
    for (IProject project: getProjectsFromPreference()) {
      if (isValidProject(project)) {
        projects.add(JavaCore.create(project));
      }
    }
    return projects.toArray(new IJavaProject[projects.size()]);
  }

  private static boolean isValidProject(IProject project) {
    try {
      if (project.isNatureEnabled(JavaCore.NATURE_ID)) {
        return true;
      }
    } catch (CoreException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return false;
  }
}
