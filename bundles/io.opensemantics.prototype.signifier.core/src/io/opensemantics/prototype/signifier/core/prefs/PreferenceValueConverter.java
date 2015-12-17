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

package io.opensemantics.prototype.signifier.core.prefs;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
}
