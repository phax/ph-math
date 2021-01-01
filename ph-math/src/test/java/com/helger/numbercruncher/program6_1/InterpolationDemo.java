/**
 * Copyright (C) 2014-2021 Philip Helger (www.helger.com)
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
package com.helger.numbercruncher.program6_1;

import java.awt.Frame;

import com.helger.numbercruncher.graphutils.AbstractDemoFrame;

/**
 * PROGRAM 6-1d: Polynomial Interpolation (Interactive Standalone Demo)
 * Interactively demonstrate polynomial interpolation.
 */
public final class InterpolationDemo extends AbstractDemoFrame
{
  private static final String TITLE = "Polynomial Interpolation Demo";

  /**
   * Constructor.
   */
  private InterpolationDemo ()
  {
    super (TITLE, new InterpolationPanel ());
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new InterpolationDemo ();
    frame.setVisible (true);
  }
}
