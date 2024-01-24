/*
 * Copyright (C) 2014-2024 Philip Helger (www.helger.com)
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
package com.helger.numbercruncher.mathutils;

/**
 * The base class for functions that can have derivatives. Initialize the static
 * function table with some sample functions.
 */
public abstract class AbstractDifferentialEquation implements IEvaluatable
{
  /** initial condition */
  private final DataPoint m_aInitialCondition;
  /** solution function label */
  private final String m_sSolutionLabel;

  /**
   * Constructor.
   *
   * @param initialCondition
   *        the initial condition data point
   * @param solutionLabel
   *        the solution function label
   */
  public AbstractDifferentialEquation (final DataPoint initialCondition, final String solutionLabel)
  {
    m_aInitialCondition = initialCondition;
    m_sSolutionLabel = solutionLabel;
  }

  /**
   * Return the initial condition data point.
   *
   * @return the initial condition
   */
  public DataPoint getInitialCondition ()
  {
    return m_aInitialCondition;
  }

  /**
   * Return the solution label.
   *
   * @return the label
   */
  public String getSolutionLabel ()
  {
    return m_sSolutionLabel;
  }

  /**
   * Return the value of the differential equation at x. (Implementation of
   * {@link IEvaluatable}.)
   *
   * @param x
   *        the value of x
   * @return the solution value
   */
  public abstract float at (float x);

  /**
   * Return the value of the differential equation at (x, y).
   *
   * @param x
   *        x position
   * @param y
   *        y position
   * @return the solution value
   */
  public float at (final float x, final float y)
  {
    return at (x);
  }

  /**
   * Return the value of the solution at x.
   *
   * @param x
   *        the value of x
   * @return the solution value
   */
  public abstract float solutionAt (float x);
}
