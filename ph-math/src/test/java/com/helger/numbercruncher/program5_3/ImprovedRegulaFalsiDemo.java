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
package com.helger.numbercruncher.program5_3;

import java.awt.Frame;

import com.helger.numbercruncher.graphutils.AbstractDemoFrame;

/**
 * PROGRAM 5-3d: Improved Regula Falsi Algorithm (Interactive Standalone Demo)
 * Interactively demonstrate the improved Regula Falsi Algorithm on various
 * functions. Either single-step or let the program automatically step once each
 * half second.
 */
public final class ImprovedRegulaFalsiDemo extends AbstractDemoFrame
{
  private static final String TITLE = "Improved Regula Falsi Demo";

  /**
   * Constructor.
   */
  private ImprovedRegulaFalsiDemo ()
  {
    super (TITLE, new ImprovedRegulaFalsiPanel ());
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new ImprovedRegulaFalsiDemo ();
    frame.setVisible (true);
  }
}
