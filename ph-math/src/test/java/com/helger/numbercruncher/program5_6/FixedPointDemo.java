/**
 * Copyright (C) 2014-2017 Philip Helger (www.helger.com)
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
package com.helger.numbercruncher.program5_6;

import java.awt.Frame;

import com.helger.numbercruncher.graphutils.AbstractDemoFrame;

/**
 * PROGRAM 5-6d: Fixed-Point Iteration (Interactive Standalone Demo)
 * Interactively demonstrate Fixed-Point Iteration Algorithm on various
 * functions.
 */
public final class FixedPointDemo extends AbstractDemoFrame
{
  private static final String TITLE = "Fixed-Point Iteration Demo";

  // Constructor
  private FixedPointDemo ()
  {
    super (TITLE, new FixedPointPanel ());
  }

  // Main
  public static void main (final String args[])
  {
    final Frame frame = new FixedPointDemo ();
    frame.setVisible (true);
  }
}
