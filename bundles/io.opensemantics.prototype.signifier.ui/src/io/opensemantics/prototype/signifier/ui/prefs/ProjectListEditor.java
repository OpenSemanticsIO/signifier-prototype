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
import org.eclipse.swt.widgets.Composite;

import io.opensemantics.prototype.signifier.ui.prefs.util.PreferenceValueConverter;

public class ProjectListEditor extends ListEditor {

  public ProjectListEditor() {
    super();
  }

  public ProjectListEditor(String name, String labelText, Composite parent) {
    super(name, labelText, parent);
  }

  @Override
  protected String createList(String[] list) {
    return PreferenceValueConverter.toProjectString(list);
  }

  @Override
  protected String getNewInputObject() {
    return "";
  }

  @Override
  protected String[] parseString(String string) {
    return PreferenceValueConverter.toProjectList(string); 
  }
}
