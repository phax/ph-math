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
package com.helger.numbercruncher.program5_5;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.MouseEvent;

import com.helger.numbercruncher.graphutils.PlotProperties;
import com.helger.numbercruncher.mathutils.AbstractFunction;
import com.helger.numbercruncher.mathutils.AbstractRootFinder;
import com.helger.numbercruncher.mathutils.NewtonsRootFinder;
import com.helger.numbercruncher.rootutils.AbstractRootFinderPanel;
import com.helger.numbercruncher.rootutils.PlotFunction;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The demo panel for the Newton's Algorithm program and applet.
 */
final class NewtonsPanel extends AbstractRootFinderPanel
{
  private static final int MAX_ITERS = 50;

  private static final String FUNCTION_IMAGE_FILE_NAME = "root-finder.gif";
  private static final String FUNCTION_FRAME_TITLE = "Click to choose a function f(x)";

  /** control panel */
  private final Panel m_aControlPanel = new Panel ();
  /** x[0] label */
  private final Label x0Label = new Label ("x[0]:");
  /** x[0] text */
  private final Label x0Text = new Label (" ");
  /** x[n+1] label */
  private final Label xnp1Label = new Label ("x[n+1]:");
  /** x[n+1] text */
  private final Label xnp1Text = new Label (" ");

  /** Newton's root finder */
  private NewtonsRootFinder finder;

  /** Functions to plot. */
  private static PlotFunction FUNCTIONS[] = { new PlotFunction ("x^2 - 4", -3.25f, 5.25f, -5.5f, 25.25f),
                                              new PlotFunction ("-x^2 + 4x + 5", -5.25f, 10.25f, -25.5f, 10.25f),
                                              new PlotFunction ("x^3 + 3x^2 - 9x - 10", -6.25f, 4.25f, -20.5f, 20.25f),
                                              new PlotFunction ("x^2 - 2x + 3", -7.25f, 9.25f, -1.5f, 25.25f),
                                              new PlotFunction ("2x^3 - 10x^2 + 11x - 5", -0.5f, 5.25f, -10.5f, 25.25f),
                                              new PlotFunction ("e^-x - x", -0.5f, 2.25f, -1.75f, 1.75f),
                                              new PlotFunction ("x - e^(1/x)", -4.25f, 4.25f, -10.25f, 3.25f), };

  /**
   * array of plot endpoint columns #1
   */
  private final int cs1[] = new int [2 * MAX_ITERS + 1];
  /**
   * array of plot endpoint rows #1
   */
  private final int rs1[] = new int [2 * MAX_ITERS + 1];
  /**
   * array of plot endpoint columns #2
   */
  private final int cs2[] = new int [2 * MAX_ITERS + 1];
  /**
   * array of plot endpoint rows #2
   */
  private final int rs2[] = new int [2 * MAX_ITERS + 1];

  /** trace point index */
  private int k;
  /** starting value of x */
  @SuppressWarnings ("unused")
  private float m_fX0;

  /** true if OK to trace */
  private boolean traceOK = true;

  /**
   * Constructor.
   */
  NewtonsPanel ()
  {
    super (FUNCTIONS, FUNCTION_IMAGE_FILE_NAME, FUNCTION_FRAME_TITLE);

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Newton controls.
    x0Label.setFont (labelFont);
    x0Label.setAlignment (Label.RIGHT);
    x0Text.setFont (textFont);
    x0Text.setAlignment (Label.LEFT);
    xnp1Label.setFont (labelFont);
    xnp1Label.setAlignment (Label.RIGHT);
    xnp1Label.setForeground (Color.blue);
    xnp1Text.setFont (textFont);
    xnp1Text.setAlignment (Label.LEFT);

    // Newton control panel.
    m_aControlPanel.setLayout (new GridLayout (0, 4, 5, 2));
    m_aControlPanel.add (x0Label);
    m_aControlPanel.add (x0Text);
    m_aControlPanel.add (xnp1Label);
    m_aControlPanel.add (xnp1Text);
    m_aControlPanel.add (nLabel);
    m_aControlPanel.add (nText);
    addDemoControls (m_aControlPanel);
  }

  // ------------------//
  // Method overrides //
  // ------------------//

  /**
   * Draw the contents of the Newton demo panel.
   */
  @Override
  public void draw ()
  {
    super.draw ();

    k = 0;
    traceOK = true;

    nText.setText (" ");
    x0Text.setText (" ");
    xnp1Text.setText (" ");

    final PlotFunction plotFunction = getSelectedPlotFunction ();

    // Create the Newton's root finder.
    finder = new NewtonsRootFinder ((AbstractFunction) plotFunction.getFunction ());
  }

  /**
   * Mouse pressed event handler: Animate the Newton trace. (Callback from the
   * plot panel)
   *
   * @param ev
   *        the mouse event
   */
  @Override
  public void mousePressedOnPlot (final MouseEvent ev)
  {
    if (traceOK)
      animate (ev.getX ());
  }

  /**
   * Mouse dragged event handler: Animate the Newton trace. (Callback from the
   * plot panel)
   *
   * @param ev
   *        the mouse event
   */
  @Override
  public void mouseDraggedOnPlot (final MouseEvent ev)
  {
    if (traceOK)
      animate (ev.getX ());
  }

  /**
   * Notification that a user input error occurred. Disable the Newton trace.
   */
  @Override
  protected void userErrorOccurred ()
  {
    traceOK = false;
  }

  // -----------------------//
  // Interactive animation //
  // -----------------------//

  /**
   * Animate a Newton trace starting at column c.
   *
   * @param c
   *        the column
   */
  private void animate (final int c)
  {
    // Erase the previous Newton trace, if any.
    if (k > 0)
      plotLines (cs1, rs1, cs2, rs2, k, Color.red);

    // Draw a new Newton trace.
    _iterate (x0 (c));
    plotLines (cs1, rs1, cs2, rs2, k, Color.red);
  }

  /**
   * Return the starting value x0 at column c.
   *
   * @param c
   *        the column
   * @return the starting value
   */
  private float x0 (final int c)
  {
    final PlotProperties props = getPlotProperties ();

    final float x0 = props.getXMin () + c * props.getXDelta ();
    x0Text.setText (Float.toString (x0));

    return x0;
  }

  /**
   * Iterate to create the graphic trace of Newton's algorithm starting at x0.
   *
   * @param x0
   *        the starting value
   */
  @SuppressFBWarnings ("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
  private void _iterate (final float x0)
  {
    // Plot properties.
    final PlotProperties props = getPlotProperties ();
    final int w = props.getWidth ();
    final int h = props.getHeight ();
    final int xAxisRow = props.getXAxisRow ();
    @SuppressWarnings ("unused")
    final int yAxisCol = props.getYAxisColumn ();
    final float xMin = props.getXMin ();
    final float yMax = props.getYMax ();
    final float deltaX = props.getXDelta ();
    final float deltaY = props.getYDelta ();

    float xn;
    float fn;
    float xnp1;
    boolean converged = false;

    k = 0;
    finder.reset (x0);

    // Apply Newton's algorithm. For each iteration, plot the
    // vertical at xn and the tangent at f(xn), i = 0..k.
    do
    {
      try
      {
        converged = finder.step ();
      }
      catch (final AbstractRootFinder.IterationCountExceededException ex)
      {
        iterationLimitExceeded (MAX_ITERS, xnp1Text);
        return;
      }
      catch (final AbstractRootFinder.PositionUnchangedException ex)
      {
        // ignore
      }

      boolean inRange = true;

      xn = finder.getXn ();
      fn = finder.getFn ();
      xnp1 = finder.getXnp1 ();

      // Plot the vertical from (xn, 0) to (xn, f(xn)).
      // Truncate lines that go beyond the graph bounds.
      cs1[k] = Math.round ((xn - xMin) / deltaX);
      rs1[k] = xAxisRow;
      cs2[k] = cs1[k];
      final float r = (yMax - fn) / deltaY;
      if (r < Short.MIN_VALUE)
      {
        rs2[k] = 0;
        inRange = false;
      }
      else
        if (r > Short.MAX_VALUE)
        {
          rs2[k] = h;
          inRange = false;
        }
        else
        {
          rs2[k] = Math.round (r);
        }

      if (inRange)
      {

        // Plot the tangent at (xn, f(xn)).
        // Check for nonconvergence (c == infinity).
        ++k;
        cs1[k] = cs2[k - 1];
        rs1[k] = rs2[k - 1];
        final float c = (xnp1 - xMin) / deltaX;
        if (c < Short.MIN_VALUE)
        {
          cs2[k] = 0;
          rs2[k] = rs1[k];
          ++k;
          xn = Float.NEGATIVE_INFINITY;
          break;
        }
        else
          if (c > Short.MAX_VALUE)
          {
            cs2[k] = w;
            rs2[k] = rs1[k];
            ++k;
            xn = Float.POSITIVE_INFINITY;
            break;
          }
          else
          {
            cs2[k] = Math.round (c);
            rs2[k] = xAxisRow;
            ++k;
          }
      }
    } while (!converged);

    // Update the n and xn text controls.
    nText.setText (Integer.toString (finder.getIterationCount ()));
    xnp1Text.setText (Float.toString (xnp1));
  }

  // -----------//
  // Debugging //
  // -----------//

  /**
   * Main for debugging.
   *
   * @param args
   *        the array of arguments
   */
  // *
  public static void main (final String args[])
  {
    final Frame test = new Frame ();
    final NewtonsPanel demo = new NewtonsPanel ();

    final AbstractFunction function = new AbstractFunction ()
    {
      @Override
      public float at (final float x)
      {
        return x * x * x - 1;
      }

      @Override
      public float derivativeAt (final float x)
      {
        return 3 * x * x;
      }
    };

    final PlotFunction plotFunction = new PlotFunction (function, -2.25f, 2.25f, -2.25f, 2.25f);
    /*
     * Function function = new Function() { public float at(float x) { return
     * x*x - 4*x + 4; } public float derivativeAt(float x) { return 2*x - 4; }
     * }; PlotFunction plotFunction = new PlotFunction(function, -1.25f, 5.255f,
     * -0.5f, 5.25f); Function function = new Function() { public float at(float
     * x) { return x*x*x - 6*x*x + 12*x - 8; } public float derivativeAt(float
     * x) { return 3*x*x - 12*x + 12; } }; PlotFunction plotFunction = new
     * PlotFunction(function, -0.25f, 5.255f, -5.25f, 5.25f);
     */

    FUNCTIONS[0] = plotFunction;

    test.setTitle ("Newton's Test");
    test.setSize (600, 500);

    test.setLayout (new BorderLayout ());
    test.add (demo, BorderLayout.CENTER);

    test.setVisible (true);

    demo.chooseFunction (0);
    demo.draw ();
  }
  // */
}
