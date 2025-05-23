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
package com.helger.numbercruncher.program5_4;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.MouseEvent;

import com.helger.numbercruncher.graphutils.PlotProperties;
import com.helger.numbercruncher.mathutils.AbstractFunction;
import com.helger.numbercruncher.mathutils.AbstractRootFinder;
import com.helger.numbercruncher.mathutils.SecantRootFinder;
import com.helger.numbercruncher.rootutils.AbstractRootFinderPanel;
import com.helger.numbercruncher.rootutils.PlotFunction;

/**
 * The demo panel for the Secant Algorithm program and applet.
 */
public final class SecantPanel extends AbstractRootFinderPanel
{
  private static final int MAX_ITERS = 50;

  private static final String FUNCTION_IMAGE_FILE_NAME = "root-finder.gif";
  private static final String FUNCTION_FRAME_TITLE = "Click to choose a function f(x)";

  /** control panel */
  private final Panel m_aControlPanel = new Panel ();
  /** xnm1 label */
  private final Label xnm1Label = new Label ("x[n-1]:");
  /** xnm1 test */
  private final Label xnm1Text = new Label (" ");
  /** xn label */
  private final Label xnLabel = new Label ("x[n]:");
  /** xn text */
  private final Label xnText = new Label (" ");
  /** xnp1 label */
  private final Label xnp1Label = new Label ("x[n+1]:");
  /** xnp1 text */
  private final Label xnp1Text = new Label (" ");

  /** Secant root finder */
  private SecantRootFinder finder;

  /** Functions whose roots to find */
  private static PlotFunction FUNCTIONS[] = { new PlotFunction ("x^2 - 4", -0.25f, 5.25f, -5.5f, 25.25f),
                                              new PlotFunction ("-x^2 + 4x + 5", -0.5f, 10.25f, -25.5f, 10.25f),
                                              new PlotFunction ("x^3 + 3x^2 - 9x - 10", -6.25f, 4.25f, -20.5f, 20.25f),
                                              new PlotFunction ("x^2 - 2x + 3", -7.25f, 9.25f, -1.5f, 25.25f),
                                              new PlotFunction ("2x^3 - 10x^2 + 11x - 5", -0.5f, 5.25f, -10.5f, 25.25f),
                                              new PlotFunction ("e^-x - x", -0.5f, 2.25f, -1.75f, 1.75f),
                                              new PlotFunction ("x - e^(1/x)", -4.25f, 4.25f, -10.25f, 3.25f), };

  /** array of plot endpoint columns #1 */
  private final int cs1[] = new int [3];
  /** array of plot endpoint rows #1 */
  private final int rs1[] = new int [3];
  /** array of plot endpoint columns #2 */
  private final int cs2[] = new int [3];
  /** array of plot endpoint rows #2 */
  private final int rs2[] = new int [3];
  /** line count */
  private int k;

  /** minimum x value */
  private float xMin;
  /** maximum x value */
  @SuppressWarnings ("unused")
  private float xMax;
  /** maximum y value */
  private float yMax;
  /** x delta per pixel */
  private float xDelta;
  /** y delta per pixel */
  private float yDelta;
  /** x[n-1] value */
  private float xnm1;
  /** x[n] value */
  private float xn;
  /** x[n+1] value */
  private float xnp1;
  /** f(x[n-1]) */
  private float fnm1;
  /** f([n]) */
  private float fn;
  /** f(x[n+1]) */
  @SuppressWarnings ("unused")
  private float fnp1;
  /** count of starting values */
  private int xStartCount;
  /** x-axis row */
  private int xAxisRow;

  /** true if OK to set values */
  private boolean setOK = true;

  // Constructor
  SecantPanel ()
  {
    super (FUNCTIONS, FUNCTION_IMAGE_FILE_NAME, FUNCTION_FRAME_TITLE);

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Secant controls
    xnm1Label.setFont (labelFont);
    xnm1Label.setAlignment (Label.RIGHT);
    xnm1Text.setFont (textFont);
    xnm1Text.setAlignment (Label.LEFT);
    xnLabel.setFont (labelFont);
    xnLabel.setAlignment (Label.RIGHT);
    xnText.setFont (textFont);
    xnText.setAlignment (Label.LEFT);
    xnp1Label.setFont (labelFont);
    xnp1Label.setAlignment (Label.RIGHT);
    xnp1Label.setForeground (Color.blue);
    xnp1Text.setFont (textFont);
    xnp1Text.setAlignment (Label.LEFT);

    // Secant panel
    m_aControlPanel.setLayout (new GridLayout (0, 5, 5, 2));
    m_aControlPanel.add (xnm1Label);
    m_aControlPanel.add (xnm1Text);
    m_aControlPanel.add (xnp1Label);
    m_aControlPanel.add (xnp1Text);
    m_aControlPanel.add (runButton);
    m_aControlPanel.add (xnLabel);
    m_aControlPanel.add (xnText);
    m_aControlPanel.add (nLabel);
    m_aControlPanel.add (nText);
    m_aControlPanel.add (stepButton);
    addDemoControls (m_aControlPanel);
  }

  /**
   * Draw the vertical lines and the secant.
   *
   * @param verticalsFlag
   *        true to draw the vertical lines
   */
  private void drawLines (final boolean verticalsFlag)
  {
    k = 0;

    if (verticalsFlag)
      drawVerticalLines ();
    if (finder != null)
      drawSecant ();

    plotLines (cs1, rs1, cs2, rs2, k, Color.red);
  }

  /**
   * Draw the vertical lines.
   */
  private void drawVerticalLines ()
  {
    // The vertical line from (x[n-1], 0) to (x[n-1], f(x[n-1]))
    if ((xStartCount == 1) || (finder != null))
    {
      final int cnm1 = Math.round ((xnm1 - xMin) / xDelta);
      final int rnm1 = Math.round ((yMax - fnm1) / yDelta);

      cs1[k] = cs2[k] = cnm1;
      rs1[k] = xAxisRow;
      rs2[k] = rnm1;
      ++k;
    }

    // The vertical line from (x[n], 0) to (x[n], f(x[n]))
    if ((xStartCount == 2) || (finder != null))
    {
      final int cn = Math.round ((xn - xMin) / xDelta);
      final int rn = Math.round ((yMax - fn) / yDelta);

      cs1[k] = cs2[k] = cn;
      rs1[k] = xAxisRow;
      rs2[k] = rn;
      ++k;
    }
  }

  /**
   * Draw the secant so that it crosses the x axis.
   */
  private void drawSecant ()
  {
    int c1, c2, r1, r2;

    // Opposite signs?
    if (fnm1 * fn < 0)
    {
      c1 = Math.round ((xnm1 - xMin) / xDelta);
      c2 = Math.round ((xn - xMin) / xDelta);
      r1 = Math.round ((yMax - fnm1) / yDelta);
      r2 = Math.round ((yMax - fn) / yDelta);
    }

    // |f(x[n+1])| > |f(x[n])| ?
    else
      if (Math.abs (fn) > Math.abs (fnm1))
      {
        c1 = Math.round ((xn - xMin) / xDelta);
        c2 = Math.round ((xnp1 - xMin) / xDelta);
        r1 = Math.round ((yMax - fn) / yDelta);
        r2 = xAxisRow;
      }

      // |f(x[n+1])| <= |f(x[n])| ?
      else
      {
        c1 = Math.round ((xnm1 - xMin) / xDelta);
        c2 = Math.round ((xnp1 - xMin) / xDelta);
        r1 = Math.round ((yMax - fnm1) / yDelta);
        r2 = xAxisRow;
      }

    // Check the endpoints.
    c1 = Math.min (c1, Short.MAX_VALUE);
    c1 = Math.max (c1, Short.MIN_VALUE);
    r1 = Math.min (r1, Short.MAX_VALUE);
    r1 = Math.max (r1, Short.MIN_VALUE);
    c2 = Math.min (c2, Short.MAX_VALUE);
    c2 = Math.max (c2, Short.MIN_VALUE);
    r2 = Math.min (r2, Short.MAX_VALUE);
    r2 = Math.max (r2, Short.MIN_VALUE);

    // Draw the secant.
    cs1[k] = c1;
    rs1[k] = r1;
    cs2[k] = c2;
    rs2[k] = r2;
    ++k;
  }

  // ------------------//
  // Method overrides //
  // ------------------//

  /**
   * Draw the contents of the secant demo panel.
   */
  @Override
  public void draw ()
  {
    super.draw ();

    setOK = true;
    xStartCount = 0;
    finder = null;

    nText.setText ("0");
    xnm1Text.setText (" ");
    xnText.setText (" ");
    xnp1Text.setText (" ");

    // Plot properties
    final PlotProperties props = getPlotProperties ();
    xMin = props.getXMin ();
    xMax = props.getXMax ();
    yMax = props.getYMax ();
    xDelta = props.getXDelta ();
    yDelta = props.getYDelta ();
    xAxisRow = props.getXAxisRow ();

    // Enable buttons after two starting values are set.
    runButton.setEnabled (false);
    stepButton.setEnabled (false);
  }

  /**
   * Mouse click on the plot: Set a starting x value.
   */
  @Override
  public void mouseClickedOnPlot (final MouseEvent ev)
  {
    if (!setOK || (++xStartCount > 2))
      return;

    final PlotProperties props = getPlotProperties ();
    final PlotFunction plotFunction = getSelectedPlotFunction ();

    final int c = ev.getX ();
    final float x = props.getXMin () + c * props.getXDelta ();

    // First starting value.
    if (xStartCount == 1)
    {
      xnm1 = x;
      fnm1 = plotFunction.at (xnm1);
      xnm1Text.setText (Float.toString (xnm1));
    }

    // Second starting value.
    else
    {
      xn = x;
      fn = plotFunction.at (xn);
      xnText.setText (Float.toString (xn));

      runButton.setEnabled (true);
      stepButton.setEnabled (true);
    }

    drawLines (true);

    // Create the secant root finder.
    if (xStartCount == 2)
    {
      finder = new SecantRootFinder ((AbstractFunction) plotFunction.getFunction (), xnm1, xn);
    }
  }

  /**
   * Notification that a user input error occurred. Disable user starting
   * points.
   */
  @Override
  protected void userErrorOccurred ()
  {
    setOK = false;
  }

  /**
   * Iterate to compute x[n+1] from the secant through (x[n-1], f(x[n-1])) and
   * (x[n], f(x[n])).
   */
  @Override
  protected void step ()
  {
    try
    {
      if (finder.step ())
      {
        successfullyConverged ();
      }
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

    final int n = finder.getIterationCount ();

    if (n > 1)
      drawLines (true); // erase the previous lines

    xnm1 = finder.getXnm1 ();
    fnm1 = finder.getFnm1 ();
    xn = finder.getXn ();
    fn = finder.getFn ();
    xnp1 = finder.getXnp1 ();
    fnp1 = finder.getFnp1 ();

    drawLines (n > 1); // draw the new lines

    // Update text controls.
    nText.setText (Integer.toString (n));
    xnm1Text.setText (Float.toString (xnm1));
    xnp1Text.setText (Float.toString (xnp1));
    xnText.setText (Float.toString (xn));
  }
}
