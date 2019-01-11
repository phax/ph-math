/**
 * Copyright (C) 2014-2019 Philip Helger (www.helger.com)
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
 * Differential equation solver that implements a predictor-corrector algorithm.
 */
public class PredictorCorrectorDiffEqSolver extends AbstractDiffEqSolver
{
  /**
   * Constructor.
   *
   * @param equation
   *        the differential equation to solve
   */
  public PredictorCorrectorDiffEqSolver (final AbstractDifferentialEquation equation)
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
    final float predictor = m_fY + Math.abs (h) * m_aEquation.at (m_fX);
    final float avgSlope = (m_aEquation.at (m_fX, m_fY) + m_aEquation.at (m_fX + h, predictor)) / 2;

    m_fY += h * avgSlope; // corrector
    m_fX += h;

    return new DataPoint (m_fX, m_fY);
  }
}
