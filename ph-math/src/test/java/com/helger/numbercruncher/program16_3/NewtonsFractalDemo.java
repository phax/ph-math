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
package com.helger.numbercruncher.program16_3;

import java.awt.Frame;

import com.helger.numbercruncher.graphutils.AbstractDemoFrame;

/**
 * PROGRAM 16-3: Newton's Fractal Graph the application of Newton's Method on
 * the function x^3 - 1, which has three roots in the complex plane: 1,
 * -0.5+0.87i, and -0.5-0.87i. Plot each point in the plane as follows: (1)
 * Apply Newton's Method using the point as the starting point. (2) Set the
 * point's color (red, green, or blue) according to which root the method
 * converges to from the point. (3) Set the color intensity according to the
 * number of iterations. The resulting graph is a Julia set fractal. You can
 * zoom into any rectangular region of the graph by using the mouse.
 */
public final class NewtonsFractalDemo extends AbstractDemoFrame
{
  private static final String TITLE = "Newton's Fractal Demo";

  /**
   * Constructor.
   */
  private NewtonsFractalDemo ()
  {
    super (TITLE, new NewtonsFractalPanel ());
  }

  /**
   * Main.
   */
  public static void main (final String args[])
  {
    final Frame frame = new NewtonsFractalDemo ();
    frame.setVisible (true);
  }
}
