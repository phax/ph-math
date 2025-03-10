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
package com.helger.numbercruncher.program16_4;

import java.awt.Frame;

import com.helger.numbercruncher.graphutils.AbstractDemoFrame;

/**
 * PROGRAM 16-4: Mandelbrot Set Fractal (Standalone Demo) Graph the Mandelbrot
 * set fractal. You can zoom into any rectangular region of the graph by using
 * the mouse.
 */
public final class MandelbrotSetDemo extends AbstractDemoFrame
{
  private static final String TITLE = "Mandelbrot Set Demo";

  /**
   * Constructor.
   */
  private MandelbrotSetDemo ()
  {
    super (TITLE, new MandelbrotSetPanel (), 450, 525);
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new MandelbrotSetDemo ();
    frame.setVisible (true);
  }
}
