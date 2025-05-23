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
package com.helger.numbercruncher.program5_1;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;

import com.helger.numbercruncher.graphutils.PlotProperties;
import com.helger.numbercruncher.mathutils.AbstractFunction;
import com.helger.numbercruncher.mathutils.AbstractRootFinder;
import com.helger.numbercruncher.mathutils.BisectionRootFinder;
import com.helger.numbercruncher.rootutils.AbstractRootFinderPanel;
import com.helger.numbercruncher.rootutils.PlotFunction;

/**
 * The demo panel for the Bisection Algorithm program and applet.
 */
public final class BisectionPanel extends AbstractRootFinderPanel
{
  private static final int MAX_ITERS = 50;

  private static final String FUNCTION_IMAGE_FILE_NAME = "root-finder.gif";
  private static final String FUNCTION_FRAME_TITLE = "Click to choose a function f(x)";

  /** x-negative label */
  private final Label xNegLabel = new Label ("x Neg:");
  /** x-negative text */
  private final Label xNegText = new Label (" ");
  /** x-middle label */
  private final Label xMidLabel = new Label ("x Mid:");
  /** x-middle text */
  private final Label xMidText = new Label (" ");
  /** x-positive label */
  private final Label xPosLabel = new Label ("x Pos:");
  /** x-positive text */
  private final Label xPosText = new Label (" ");

  /** Bisection root finder */
  private BisectionRootFinder finder;

  /** Functions whose roots to find */
  private static PlotFunction FUNCTIONS[] = { new PlotFunction ("x^2 - 4", -0.25f, 5.25f, -5.5f, 25.25f),
                                              new PlotFunction ("-x^2 + 4x + 5", -0.5f, 10.25f, -25.5f, 10.25f),
                                              new PlotFunction ("x^3 + 3x^2 - 9x - 10", -6.25f, 4.25f, -20.5f, 20.25f),
                                              new PlotFunction ("x^2 - 2x + 3", -7.25f, 9.25f, -1.5f, 25.25f),
                                              new PlotFunction ("2x^3 - 10x^2 + 11x - 5", -0.5f, 5.25f, -10.5f, 25.25f),
                                              new PlotFunction ("e^-x - x", -0.5f, 2.25f, -1.75f, 1.75f),
                                              new PlotFunction ("x - e^(1/x)", -3.25f, 4.25f, -10.25f, 3.25f), };

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

  /** panel height */
  private int h;
  /** x-axis row */
  private int xAxisRow;
  /** minimum x value */
  private float xMin;
  /** maximum x value */
  private float xMax;
  /** x delta per pixel */
  private float xDelta;
  /** y delta per pixel */
  @SuppressWarnings ("unused")
  private float yDelta;
  /** x-negative value */
  private float xNeg;
  /** x-middle value */
  private float xMid;
  /** x-positive value */
  private float xPos;

  /**
   * Constructor.
   */
  BisectionPanel ()
  {
    super (FUNCTIONS, FUNCTION_IMAGE_FILE_NAME, FUNCTION_FRAME_TITLE);

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Bisection controls.
    xNegLabel.setFont (labelFont);
    xNegLabel.setAlignment (Label.RIGHT);
    xNegText.setFont (textFont);
    xNegText.setAlignment (Label.LEFT);
    xMidLabel.setFont (labelFont);
    xMidLabel.setAlignment (Label.RIGHT);
    xMidLabel.setForeground (Color.blue);
    xMidText.setFont (textFont);
    xMidText.setAlignment (Label.LEFT);
    xPosLabel.setFont (labelFont);
    xPosLabel.setAlignment (Label.RIGHT);
    xPosText.setFont (textFont);
    xPosText.setAlignment (Label.LEFT);

    // Bisection control panel.
    controlPanel.setLayout (new GridLayout (0, 5, 5, 2));
    controlPanel.add (xNegLabel);
    controlPanel.add (xNegText);
    controlPanel.add (xMidLabel);
    controlPanel.add (xMidText);
    controlPanel.add (runButton);
    controlPanel.add (xPosLabel);
    controlPanel.add (xPosText);
    controlPanel.add (nLabel);
    controlPanel.add (nText);
    controlPanel.add (stepButton);
    addDemoControls (controlPanel);
  }

  /**
   * Draw the bisection lines.
   */
  private void drawBisectionLines ()
  {
    k = 0;

    // Convert xNeg, xPos, and xMid to graph columns.
    final int colNeg = Math.round ((xNeg - xMin) / xDelta);
    final int colPos = Math.round ((xPos - xMin) / xDelta);
    final int colMid = (colNeg + colPos) >>> 1;

    // Draw the boundary lines at xNeg and xPos.
    cs1[k] = cs2[k] = colPos;
    rs1[k] = 0;
    rs2[k] = h;
    ++k;

    if (colNeg != colPos)
    {
      cs1[k] = cs2[k] = colNeg;
      rs1[k] = 0;
      rs2[k] = h;
      ++k;
    }

    // Draw the midpoint line at xMid.
    if ((colNeg != colMid) && (colMid != colPos))
    {
      cs1[k] = cs2[k] = colMid;
      rs1[k] = xAxisRow - 20;
      rs2[k] = xAxisRow + 20;
      ++k;
    }

    plotLines (cs1, rs1, cs2, rs2, k, Color.red);
  }

  // ------------------//
  // Method overrides //
  // ------------------//

  /**
   * Draw the contents of the bisection demo panel.
   */
  @Override
  public void draw ()
  {
    super.draw ();

    nText.setText ("0");
    xNegText.setText (" ");
    xMidText.setText (" ");
    xPosText.setText (" ");

    // Plot properties
    final PlotProperties props = getPlotProperties ();
    xMin = props.getXMin ();
    xMax = props.getXMax ();
    xDelta = props.getXDelta ();
    yDelta = props.getYDelta ();
    h = props.getHeight ();
    xAxisRow = props.getXAxisRow ();

    // Initialize xNeg and xPos.
    xNeg = xMin + xDelta;
    xPos = xMax - xDelta;

    final PlotFunction plotFunction = getSelectedPlotFunction ();

    // Make sure f(xNeg) < 0 and f(xPos) > 0.
    if (plotFunction.at (xPos) < 0)
    {
      final float temp = xNeg;
      xNeg = xPos;
      xPos = temp;
    }

    // Create the bisection root finder.
    try
    {
      finder = new BisectionRootFinder ((AbstractFunction) plotFunction.getFunction (), xNeg, xPos);
    }
    catch (final AbstractRootFinder.InvalidIntervalException ex)
    {
      nText.setText ("***");
      xMidText.setText ("Bad interval");
      runButton.setEnabled (false);
      stepButton.setEnabled (false);
    }
  }

  /**
   * Do one iteration step by choosing the appropriate half of the interval.
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
      iterationLimitExceeded (MAX_ITERS, xMidText);
      return;
    }
    catch (final AbstractRootFinder.PositionUnchangedException ex)
    {
      // ignore
    }

    final int n = finder.getIterationCount ();
    if (n > 1)
      drawBisectionLines (); // erase previous lines

    xNeg = finder.getXNeg ();
    xMid = finder.getXMid ();
    xPos = finder.getXPos ();

    drawBisectionLines (); // draw new lines

    // Update text controls.
    nText.setText (Integer.toString (n));
    xNegText.setText (Float.toString (xNeg));
    xMidText.setText (Float.toString (xMid));
    xPosText.setText (Float.toString (xPos));
  }
}
