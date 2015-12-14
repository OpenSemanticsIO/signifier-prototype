package io.opensemantics.prototype.signifier.ui.prefs.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class PreferenceValueConverterTest {

  @Test
  public void testProjectStringAndList() {
    // roundtrip testing
    String[] expectedList = new String[]{"a", "b", "c"};
    String actualString = PreferenceValueConverter.toProjectString(expectedList);
    String[] actualList = PreferenceValueConverter.toProjectList(actualString);
    assertArrayEquals(expectedList, actualList);
    assertEquals(actualString, PreferenceValueConverter.toProjectString(actualList));
    
    // null testing
    expectedList = new String[]{};
    actualString = PreferenceValueConverter.toProjectString(expectedList);
    actualList = PreferenceValueConverter.toProjectList(actualString);
    assertArrayEquals(expectedList, actualList);
  }
  
  @Test
  public void shouldAddNewProjectToList() {
    // roundtrip testing
    final String[] tmpArr = new String[]{"a", "b"};
    final String[] expectedArr = new String[]{"a", "b", "c"};
    String actualString = PreferenceValueConverter.toProjectString(tmpArr);
    actualString = PreferenceValueConverter.addProjectList(actualString, "c");
    assertArrayEquals(expectedArr, PreferenceValueConverter.toProjectList(actualString));
  }
}
