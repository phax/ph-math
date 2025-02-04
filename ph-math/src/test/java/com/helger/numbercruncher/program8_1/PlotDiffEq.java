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
package com.helger.numbercruncher.program8_1;

import java.awt.Rectangle;

import com.helger.commons.collection.impl.CommonsConcurrentHashMap;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.numbercruncher.graphutils.IPlottable;
import com.helger.numbercruncher.graphutils.PlotProperties;
import com.helger.numbercruncher.mathutils.AbstractDifferentialEquation;
import com.helger.numbercruncher.mathutils.DataPoint;
import com.helger.numbercruncher.mathutils.IEvaluatable;

/**
 * Wrapper class that makes differential equations plottable.
 */
public final class PlotDiffEq implements IPlottable
{
  private static final int X1 = 1;
  private static final int X2 = 145;

  private static final int Y11 = 25;
  private static final int Y12 = 52;
  private static final int Y21 = Y12 + 2;
  private static final int Y22 = 82;
  private static final int Y31 = Y22 + 2;
  private static final int Y32 = 111;
  private static final int Y41 = Y32 + 2;
  private static final int Y42 = 139;
  private static final int Y51 = Y42 + 2;
  private static final int Y52 = 169;
  private static final int Y61 = Y52 + 2;
  private static final int Y62 = 219;

  /** differential equation */
  private AbstractDifferentialEquation m_aEquation;
  /** image region */
  private Rectangle rectangle;
  /** plot properties */
  private PlotProperties properties;

  /** wrapped differential equations table */
  public static final ICommonsMap <String, PlotDiffEq> TABLE = new CommonsConcurrentHashMap <> (32);

  // Enter the wrapped differential equations into the table.
  static
  {
    enter ("2x", X1, Y11, X2, Y12);
    enter ("3x^2 + 6x - 9", X1, Y21, X2, Y22);
    enter ("6x^2 - 20x + 11", X1, Y31, X2, Y32);
    enter ("2xe^2x + y", X1, Y41, X2, Y42);
    enter ("8x - 2y + 8", X1, Y51, X2, Y52);
    enter ("xe^-2x - 2y", X1, Y61, X2, Y62);
  }

  /**
   * Create a wrapped diffential equation and enter it into the table.
   *
   * @param key
   *        the hash key
   * @param x1
   *        the x-coordinate of uppper left corner of image region
   * @param y1
   *        the y-coordinate of uppper left corner of image region
   * @param x2
   *        the x-coordinate of lower left corner of image region
   * @param y2
   *        the y-coordinate of lower left corner of image region
   */
  private static void enter (final String key, final int x1, final int y1, final int x2, final int y2)
  {
    final PlotDiffEq plotDiffEq = new PlotDiffEq (x1, y1, x2, y2);
    plotDiffEq.m_aEquation = DiffEqsToSolve.equation (key);

    TABLE.put (key, plotDiffEq);
  }

  /**
   * Constructor.
   *
   * @param key
   *        the function key
   * @param xMin
   *        the minimum x value of the plot bounds
   * @param xMax
   *        the maximum x value of the plot bounds
   * @param yMin
   *        the minimum y value of the plot bounds
   * @param yMax
   *        the maximum y value of the plot bounds
   */
  public PlotDiffEq (final String key, final float xMin, final float xMax, final float yMin, final float yMax)
  {
    final PlotDiffEq solvedEquation = TABLE.get (key);

    if (solvedEquation != null)
    {
      this.m_aEquation = solvedEquation.m_aEquation;
      this.rectangle = solvedEquation.rectangle;
      this.properties = new PlotProperties (xMin, xMax, yMin, yMax);
    }
  }

  /**
   * Constructor.
   *
   * @param equation
   *        the differential equation
   * @param xMin
   *        the minimum x value of the plot bounds
   * @param xMax
   *        the maximum x value of the plot bounds
   * @param yMin
   *        the minimum y value of the plot bounds
   * @param yMax
   *        the maximum y value of the plot bounds
   */
  public PlotDiffEq (final AbstractDifferentialEquation equation,
                     final float xMin,
                     final float xMax,
                     final float yMin,
                     final float yMax)
  {
    this.m_aEquation = equation;
    this.rectangle = null;
    this.properties = new PlotProperties (xMin, xMax, yMin, yMax);
  }

  /**
   * Constructor.
   *
   * @param x1
   *        the x-coordinate of uppper left corner of image region
   * @param y1
   *        the y-coordinate of uppper left corner of image region
   * @param x2
   *        the x-coordinate of lower left corner of image region
   * @param y2
   *        the y-coordinate of lower left corner of image region
   */
  private PlotDiffEq (final int x1, final int y1, final int x2, final int y2)
  {
    this.rectangle = new Rectangle (x1, y1, x2 - x1 + 1, y2 - y1 + 1);
  }

  /**
   * Return the unwrapped differential equation.
   *
   * @return the differential equation
   */
  public IEvaluatable getFunction ()
  {
    return m_aEquation;
  }

  /**
   * Return the value of the differential equation at x.
   *
   * @return the differential equation value
   */
  public float at (final float x)
  {
    return m_aEquation.at (x);
  }

  /**
   * Return the value of the solution at x.
   *
   * @return the solution value
   */
  public float solutionAt (final float x)
  {
    return m_aEquation.solutionAt (x);
  }

  /**
   * Return the initial condition data point.
   *
   * @return the initial condition
   */
  public DataPoint getInitialCondition ()
  {
    return m_aEquation.getInitialCondition ();
  }

  // --------------------------//
  // Plottable implementation //
  // --------------------------//

  /**
   * Get the root function's image rectangle
   *
   * @return the rectangle
   */
  public Rectangle getRectangle ()
  {
    return rectangle;
  }

  /**
   * Get the root function's properties
   *
   * @return the properties
   */
  public PlotProperties getPlotProperties ()
  {
    return properties;
  }
}
