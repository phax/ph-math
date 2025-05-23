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

import com.helger.commons.collection.impl.CommonsConcurrentHashMap;
import com.helger.commons.collection.impl.ICommonsMap;
import com.helger.numbercruncher.mathutils.AbstractDifferentialEquation;
import com.helger.numbercruncher.mathutils.DataPoint;

/**
 * Load into a global table the differential equations we want to solve.
 */
public final class DiffEqsToSolve
{
  /** global function table */
  private static ICommonsMap <String, AbstractDifferentialEquation> TABLE = new CommonsConcurrentHashMap <> (32);

  // Enter the differential equations into the global table.
  static
  {
    enterDifferentialEquations ();
  }

  /**
   * Return the differential equation with the given hash key
   *
   * @param key
   *        the hash key
   * @return the differential equation
   */
  public static AbstractDifferentialEquation equation (final String key)
  {
    return TABLE.get (key);
  }

  /**
   * Enter the differential equations into the global table.
   */
  private static void enterDifferentialEquations ()
  {
    // Differential equation f(x, y) = 2x
    // Initial condition y(2) = 0
    // Solution y = x^2 - 4
    TABLE.put ("2x", new AbstractDifferentialEquation (new DataPoint (2, 0), "x^2 - 4")
    {
      @Override
      public float at (final float x)
      {
        return 2 * x;
      }

      @Override
      public float solutionAt (final float x)
      {
        return x * x - 4;
      }
    });

    // Differential equation f(x, y) = 3x^2 + 6x - 9
    // Initial condition y(-4.5050397) = 0
    // Solution y = x^3 + 3x^2 - 9x - 10
    TABLE.put ("3x^2 + 6x - 9",
               new AbstractDifferentialEquation (new DataPoint (-4.5050397f, 0), "x^3 + 3x^2 - 9x - 10")
               {
                 @Override
                 public float at (final float x)
                 {
                   return 3 * x * x + 6 * x - 9;
                 }

                 @Override
                 public float solutionAt (final float x)
                 {
                   return x * x * x + 3 * x * x - 9 * x - 10;
                 }
               });

    // Differential equation f(x, y) = 6x^2 - 20x + 11
    // Initial condition y(0) = -5
    // Solution y = 2x^3 - 10x^2 + 11x - 5
    TABLE.put ("6x^2 - 20x + 11", new AbstractDifferentialEquation (new DataPoint (0, -5), "2x^3 - 10x^2 + 11x - 5")
    {
      @Override
      public float at (final float x)
      {
        return 6 * x * x - 20 * x + 11;
      }

      @Override
      public float solutionAt (final float x)
      {
        return 2 * x * x * x - 10 * x * x + 11 * x - 5;
      }
    });

    // Differential equation f(x, y) = 2xe^2x + y
    // Initial condition y(0) = 1
    // Solution y = 3e^x - 2e^2x + 2xe^2x
    TABLE.put ("2xe^2x + y", new AbstractDifferentialEquation (new DataPoint (0, 1), "3e^x - 2e^2x + 2xe^2x")
    {
      @Override
      public float at (final float x)
      {
        return (float) (2 * x * Math.exp (2 * x) + solutionAt (x));
      }

      @Override
      public float at (final float x, final float y)
      {
        return (float) (2 * x * Math.exp (2 * x) + y);
      }

      @Override
      public float solutionAt (final float x)
      {
        return (float) (3 * Math.exp (x) + 2 * Math.exp (2 * x) * (x - 1));
      }
    });

    // Differential equation f(x, y) = 8x - 2y + 8
    // Initial condition y(0) = -1
    // Solution y = 4x - 3e^-2x + 2
    TABLE.put ("8x - 2y + 8", new AbstractDifferentialEquation (new DataPoint (0, -1), "4x - 3e^-2x + 2")
    {
      @Override
      public float at (final float x)
      {
        return 8 * x - 2 * solutionAt (x) + 8;
      }

      @Override
      public float at (final float x, final float y)
      {
        return 8 * x - 2 * y + 8;
      }

      @Override
      public float solutionAt (final float x)
      {
        return (float) (4 * x - 3 * Math.exp (-2 * x) + 2);
      }
    });

    // Differential equation f(x, y) = xe^-2x - 2y
    // Initial condition y(0) = -0.5
    // Solution y = (x^2e^-2x - e^-2x)/2
    TABLE.put ("xe^-2x - 2y", new AbstractDifferentialEquation (new DataPoint (0, -0.5f), "(x^2e^-2x - e^-2x)/2")
    {
      @Override
      public float at (final float x)
      {
        return (float) (x * Math.exp (-2 * x) - 2 * solutionAt (x));
      }

      @Override
      public float at (final float x, final float y)
      {
        return (float) (x * Math.exp (-2 * x) - 2 * y);
      }

      @Override
      public float solutionAt (final float x)
      {
        return (float) ((x * x - 1) * Math.exp (-2 * x)) / 2;
      }
    });
  }
}
