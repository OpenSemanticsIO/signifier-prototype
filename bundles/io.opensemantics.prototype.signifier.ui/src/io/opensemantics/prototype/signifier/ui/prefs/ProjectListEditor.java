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

package io.opensemantics.prototype.signifier.ui.prefs;

import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;

import io.opensemantics.prototype.signifier.core.prefs.PreferenceConstants;
import io.opensemantics.prototype.signifier.core.prefs.PreferenceValueConverter;

public class ProjectListEditor extends ListEditor {
  
  public ProjectListEditor(Composite parent) {
    super(PreferenceConstants.P_PROJECT_LIST, "Projects", parent);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.preference.PathEditor#getNewInputObject()
   */
  @Override
  protected String getNewInputObject() {
    FileDialog dialog = new FileDialog(getShell(), SWT.SHEET);
    dialog.setText("Select a .project");
    dialog.setFilterExtensions(new String[]{".project"});
    dialog.setFilterNames(new String[]{"*.project"});
    return dialog.open();
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.preference.ListEditor#createList(java.lang.String[])
   */
  @Override
  protected String createList(String[] items) {
    return PreferenceValueConverter.toProjectString(items);
  }

  /*
   * (non-Javadoc)
   * @see org.eclipse.jface.preference.ListEditor#parseString(java.lang.String)
   */
  @Override
  protected String[] parseString(String stringList) {
    return PreferenceValueConverter.toProjectList(stringList);
  }
}
