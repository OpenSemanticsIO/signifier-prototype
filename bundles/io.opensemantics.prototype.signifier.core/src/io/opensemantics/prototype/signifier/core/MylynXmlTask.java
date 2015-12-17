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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;

import io.opensemantics.prototype.contrib.org.eclipse.mylyn.internal.tasks.core.externalization.TaskListExternalizer;
import io.opensemantics.signifier.api.Callsite;
import io.opensemantics.signifier.api.Method;

public class MylynXmlTask {
  private TaskListExternalizer externalizer;
  
  public MylynXmlTask(int offset) {
    this.externalizer = new TaskListExternalizer(offset);
  }

  private List<Callsite> callsites = new ArrayList<>();
  
  public void addCallSites(Method callee, List<Method> callers) {
    callsites.add(new Callsite(callee, callers));
  }

  public void writeTasks(File file) throws CoreException {
    externalizer.writeTaskList(callsites, file);
  }
}
