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

package io.opensemantics.prototype.signifier.core.util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;

public class ModelHelper {

  public static <T> JAXBElement<T> unmarshall(Source source, Class<T> cls) throws JAXBException {
    JAXBContext context = JAXBContext.newInstance(cls);
    Unmarshaller unmarshaller = context.createUnmarshaller();
    unmarshaller.setProperty("eclipselink.media-type", "application/json");
    unmarshaller.setProperty("eclipselink.json.include-root", false);
    return (JAXBElement<T>) unmarshaller.unmarshal(source, cls);
  }
}
