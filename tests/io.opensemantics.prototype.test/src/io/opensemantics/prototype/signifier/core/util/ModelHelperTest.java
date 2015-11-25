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

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.transform.stream.StreamSource;

import org.junit.Test;

import io.opensemantics.prototype.signifier.core.util.ModelHelper;
import io.opensemantics.signifier.api.Method;


public class ModelHelperTest {

  @Test
  public void shouldUnmarshall() throws JAXBException {
    InputStream sinkStream = getClass().getResourceAsStream("/sinks-test.json");
    assertNotNull(sinkStream);
    @SuppressWarnings("unchecked")
    List<Method> callers = (List<Method>) ModelHelper.unmarshall(new StreamSource(sinkStream), Method.class).getValue();
    assertEquals(3, callers.size());
  }

}
