/**
 * Copyright (C) 2014-2020 Philip Helger (www.helger.com)
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
 * The base class for functions that can have derivatives.
 */
public abstract class AbstractFunction implements IEvaluatable
{
  /**
   * Return the value of the function at x.
   *
   * @param x
   *        the value of x
   * @return the function value
   */
  public abstract float at (float x);

  /**
   * Return the value of the function's derivative at x.
   *
   * @param x
   *        the value of x
   * @return the derivative value
   */
  public float derivativeAt (final float x)
  {
    return 0;
  }
}
