/**
 * Copyright (C) 2014-2018 Philip Helger (www.helger.com)
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
 * The base class for differential equation solvers.
 */
public abstract class AbstractDiffEqSolver
{
  /** the differential equation to solve */
  protected AbstractDifferentialEquation m_aEquation;

  /** the initial condition data point */
  protected DataPoint m_aInitialCondition;

  /** current x value */
  protected float m_fX;
  /** current y value */
  protected float m_fY;

  /**
   * Constructor.
   *
   * @param equation
   *        the differential equation to solve
   */
  public AbstractDiffEqSolver (final AbstractDifferentialEquation equation)
  {
    m_aEquation = equation;
    m_aInitialCondition = equation.getInitialCondition ();

    reset ();
  }

  /**
   * Reset x and y to the initial condition data point.
   */
  public void reset ()
  {
    m_fX = m_aInitialCondition.getX ();
    m_fY = m_aInitialCondition.getY ();
  }

  /**
   * Return the next data point in the approximation of the solution.
   *
   * @param h
   *        the width of the interval
   */
  public abstract DataPoint nextPoint (float h);
}
