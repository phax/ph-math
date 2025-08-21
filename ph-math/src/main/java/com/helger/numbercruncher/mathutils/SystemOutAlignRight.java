/*
 * Copyright (C) 2014-2025 Philip Helger (www.helger.com)
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
package com.helger.numbercruncher.mathutils;

import java.io.PrintStream;

import com.helger.annotation.WillNotClose;
import com.helger.base.string.StringHelper;

import jakarta.annotation.Nonnull;

/**
 * Print text and numbers right-aligned in columns.
 */
public class SystemOutAlignRight
{
  private final PrintStream m_aPS;
  /** line size */
  private int m_nLineSize;

  /**
   * Constructor.
   *
   * @param aPS
   *        The print stream to operate on. May not be <code>null</code>.
   */
  public SystemOutAlignRight (@Nonnull @WillNotClose final PrintStream aPS)
  {
    m_aPS = aPS;
  }

  /**
   * Print text right-aligned in the column.
   *
   * @param sText
   *        the text to print
   * @param nWidth
   *        the column width
   */
  public void print (@Nonnull final String sText, final int nWidth)
  {
    final int padding = nWidth - sText.length ();
    m_aPS.print (StringHelper.getRepeated (' ', padding) + sText);

    m_nLineSize += nWidth;
  }

  /**
   * Print an integer value right-aligned in the column.
   *
   * @param value
   *        the value to print
   * @param nWidth
   *        the column width
   */
  public void print (final int value, final int nWidth)
  {
    print (Integer.toString (value), nWidth);
  }

  /**
   * Print a float value right-aligned in the column.
   *
   * @param value
   *        the value to print
   * @param nWidth
   *        the column width
   */
  public void print (final float value, final int nWidth)
  {
    print (Float.toString (value), nWidth);
  }

  /**
   * Print a double value right-aligned in the column.
   *
   * @param value
   *        the value to print
   * @param nWidth
   *        the column width
   */
  public void print (final double value, final int nWidth)
  {
    print (Double.toString (value), nWidth);
  }

  /**
   * Print a line.
   */
  public void println ()
  {
    m_aPS.println ();
    m_nLineSize = 0;
  }

  /**
   * Print an underline.
   */
  public void underline ()
  {
    m_aPS.println ();
    m_aPS.print (StringHelper.getRepeated ('-', m_nLineSize));
    m_aPS.println ();
    m_nLineSize = 0;
  }
}
