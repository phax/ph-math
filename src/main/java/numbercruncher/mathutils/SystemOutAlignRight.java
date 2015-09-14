/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package numbercruncher.mathutils;

import javax.annotation.Nonnull;

import com.helger.commons.string.StringHelper;

/**
 * Print text and numbers right-aligned in columns.
 */
public class SystemOutAlignRight
{
  /** line size */
  private int m_nLineSize;

  /**
   * Constructor.
   */
  public SystemOutAlignRight ()
  {}

  /**
   * Print text right-aligned in the column.
   *
   * @param text
   *        the text to print
   * @param width
   *        the column width
   */
  public void print (@Nonnull final String text, final int width)
  {
    final int padding = width - text.length ();
    System.out.print (StringHelper.getRepeated (' ', padding) + text);

    m_nLineSize += width;
  }

  /**
   * Print an integer value right-aligned in the column.
   *
   * @param value
   *        the value to print
   * @param width
   *        the column width
   */
  public void print (final int value, final int width)
  {
    print (Integer.toString (value), width);
  }

  /**
   * Print a float value right-aligned in the column.
   *
   * @param value
   *        the value to print
   * @param width
   *        the column width
   */
  public void print (final float value, final int width)
  {
    print (Float.toString (value), width);
  }

  /**
   * Print a double value right-aligned in the column.
   *
   * @param value
   *        the value to print
   * @param width
   *        the column width
   */
  public void print (final double value, final int width)
  {
    print (Double.toString (value), width);
  }

  /**
   * Print a line.
   */
  public void println ()
  {
    System.out.println ();
    m_nLineSize = 0;
  }

  /**
   * Print an underline.
   */
  public void underline ()
  {
    System.out.println ();
    for (int i = 0; i < m_nLineSize; ++i)
      System.out.print ("-");
    System.out.println ();
    m_nLineSize = 0;
  }
}