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

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;

import com.helger.numbercruncher.graphutils.PlotProperties;
import com.helger.numbercruncher.mathutils.AbstractFunction;
import com.helger.numbercruncher.mathutils.AbstractRootFinder;
import com.helger.numbercruncher.mathutils.ImprovedRegulaFalsiRootFinder;
import com.helger.numbercruncher.rootutils.AbstractRootFinderPanel;
import com.helger.numbercruncher.rootutils.PlotFunction;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * The demo panel for the Improved Regula Falsi Algorithm program and applet.
 */
public final class ImprovedRegulaFalsiPanel extends AbstractRootFinderPanel
{
  private static final int MAX_ITERS = 50;

  private static final String FUNCTION_IMAGE_FILE_NAME = "root-finder.gif";
  private static final String FUNCTION_FRAME_TITLE = "Click to choose a function f(x)";

  /** x-negative label */
  private final Label xNegLabel = new Label ("x Neg:");
  /** x-negative text */
  private final Label xNegText = new Label (" ");
  /** x-false label */
  private final Label xFalseLabel = new Label ("x False:");
  /** x-false text */
  private final Label xFalseText = new Label (" ");
  /** x-positive label */
  private final Label xPosLabel = new Label ("x Pos:");
  /** x-positive text */
  private final Label xPosText = new Label (" ");

  /** Improved regula falsi root finder */
  protected ImprovedRegulaFalsiRootFinder finder;

  /** Functions whose roots to find */
  private static PlotFunction FUNCTIONS[] = { new PlotFunction ("x^2 - 4", -0.25f, 5.25f, -5.5f, 25.25f),
                                              new PlotFunction ("-x^2 + 4x + 5", -0.5f, 10.25f, -25.5f, 10.25f),
                                              new PlotFunction ("x^3 + 3x^2 - 9x - 10", -6.25f, 4.25f, -20.5f, 20.25f),
                                              new PlotFunction ("x^2 - 2x + 3", -7.25f, 9.25f, -1.5f, 25.25f),
                                              new PlotFunction ("2x^3 - 10x^2 + 11x - 5", -0.5f, 5.25f, -10.5f, 25.25f),
                                              new PlotFunction ("e^-x - x", -0.5f, 2.25f, -1.75f, 1.75f),
                                              new PlotFunction ("x - e^(1/x)", -4.25f, 4.25f, -10.25f, 3.25f), };

  /** minimum x value */
  private float xMin;
  /** maximum x value */
  private float xMax;
  /** maximum y value */
  private float yMax;
  /** x delta per pixel */
  private float xDelta;
  /** y delta per pixel */
  private float yDelta;
  /** x negative value */
  protected float xNeg;
  /** x positive value */
  protected float xPos;
  /** x false value */
  private float xFalse;
  /** x-axis row */
  @SuppressWarnings ("unused")
  private int xAxisRow;

  /**
   * Constructor.
   */
  public ImprovedRegulaFalsiPanel ()
  {
    super (FUNCTIONS, false, FUNCTION_IMAGE_FILE_NAME, FUNCTION_FRAME_TITLE);

    final Font labelFont = getLabelFont ();
    final Font textFont = getTextFont ();

    // Regula falsi controls.
    xNegLabel.setFont (labelFont);
    xNegLabel.setAlignment (Label.RIGHT);
    xNegText.setFont (textFont);
    xNegText.setAlignment (Label.LEFT);
    xFalseLabel.setFont (labelFont);
    xFalseLabel.setAlignment (Label.RIGHT);
    xFalseLabel.setForeground (Color.blue);
    xFalseText.setFont (textFont);
    xFalseText.setAlignment (Label.LEFT);
    xPosLabel.setFont (labelFont);
    xPosLabel.setAlignment (Label.RIGHT);
    xPosText.setFont (textFont);
    xPosText.setAlignment (Label.LEFT);

    // Regula falsi control panel.
    controlPanel.setLayout (new GridLayout (0, 5, 5, 2));
    controlPanel.add (xNegLabel);
    controlPanel.add (xNegText);
    controlPanel.add (xFalseLabel);
    controlPanel.add (xFalseText);
    controlPanel.add (runButton);
    controlPanel.add (xPosLabel);
    controlPanel.add (xPosText);
    controlPanel.add (nLabel);
    controlPanel.add (nText);
    controlPanel.add (stepButton);
    addDemoControls (controlPanel);
  }

  /**
   * Draw the secant.
   */
  @SuppressFBWarnings ("RV_RETURN_VALUE_IGNORED_NO_SIDE_EFFECT")
  private void _drawSecant ()
  {
    // Convert xNeg, xPos, and xFalse to graph columns.
    final int cNeg = Math.round ((xNeg - xMin) / xDelta);
    final int cPos = Math.round ((xPos - xMin) / xDelta);
    @SuppressWarnings ("unused")
    final int cFalse = Math.round ((xFalse - xMin) / xDelta);

    final float fNeg = finder.getFNeg ();
    final float fFalse = finder.getFFalse ();
    final float fPos = finder.getFPos ();

    // Convert f(xNeg), f(xPos), and f(xFalse) to graph rows.
    final int rNeg = Math.round ((yMax - fNeg) / yDelta);
    final int rPos = Math.round ((yMax - fPos) / yDelta);
    @SuppressWarnings ("unused")
    final int rFalse = Math.round ((yMax - fFalse) / yDelta);

    // Draw the secant.
    plotLine (cNeg, rNeg, cPos, rPos, Color.red);
  }

  // ------------------//
  // Method overrides //
  // ------------------//

  /**
   * Draw the contents of the regula falsi demo panel.
   */
  @Override
  public void draw ()
  {
    super.draw ();

    nText.setText ("0");
    xNegText.setText (" ");
    xFalseText.setText (" ");
    xPosText.setText (" ");

    // Plot properties
    final PlotProperties props = getPlotProperties ();
    xMin = props.getXMin ();
    xMax = props.getXMax ();
    yMax = props.getYMax ();
    xDelta = props.getXDelta ();
    yDelta = props.getYDelta ();
    xAxisRow = props.getXAxisRow ();

    // Initialize xNeg and xPos.
    xNeg = xMin;
    xPos = xMax;

    final PlotFunction plotFunction = getSelectedPlotFunction ();

    // Make sure f(xNeg) < 0 and f(xPos) > 0.
    if (plotFunction.at (xPos) < 0)
    {
      final float temp = xNeg;
      xNeg = xPos;
      xPos = temp;
    }

    // Create the regula falsi root finder.
    try
    {
      finder = new ImprovedRegulaFalsiRootFinder ((AbstractFunction) plotFunction.getFunction (), xNeg, xPos);
    }
    catch (final AbstractRootFinder.InvalidIntervalException ex)
    {
      nText.setText ("***");
      xFalseText.setText ("Bad interval");
      runButton.setEnabled (false);
      stepButton.setEnabled (false);
    }
  }

  /**
   * Do one iteration step by constructing the secant and choosing the left or
   * right part of the interval.
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
      iterationLimitExceeded (MAX_ITERS, xFalseText);
      return;
    }
    catch (final AbstractRootFinder.PositionUnchangedException ex)
    {
      // ignore
    }

    xNeg = finder.getXNeg ();
    xFalse = finder.getXFalse ();
    xPos = finder.getXPos ();

    _drawSecant (); // draw a new secant

    // Update text controls.
    nText.setText (Integer.toString (finder.getIterationCount ()));
    xNegText.setText (Float.toString (xNeg));
    xFalseText.setText (Float.toString (xFalse));
    xPosText.setText (Float.toString (xPos));
  }
}
