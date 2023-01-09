/*
 * Copyright (C) 2014-2023 Philip Helger (www.helger.com)
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
 * Differential equation solver that implements a fourth-order Runge-Kutta
 * algorithm.
 */
public class RungeKuttaDiffEqSolver extends AbstractDiffEqSolver
{
  /**
   * Constructor.
   *
   * @param equation
   *        the differential equation to solve
   */
  public RungeKuttaDiffEqSolver (final AbstractDifferentialEquation equation)
  {
    super (equation);
  }

  /**
   * Return the next data point in the approximation of the solution.
   *
   * @param h
   *        the width of the interval
   */
  @Override
  public DataPoint nextPoint (final float h)
  {
    final float k1 = m_aEquation.at (m_fX, m_fY);
    final float k2 = m_aEquation.at (m_fX + h / 2, m_fY + k1 * h / 2);
    final float k3 = m_aEquation.at (m_fX + h / 2, m_fY + k2 * h / 2);
    final float k4 = m_aEquation.at (m_fX + h, m_fY + k3 * h);

    m_fY += (k1 + 2 * (k2 + k3) + k4) * h / 6;
    m_fX += h;

    return new DataPoint (m_fX, m_fY);
  }
}
