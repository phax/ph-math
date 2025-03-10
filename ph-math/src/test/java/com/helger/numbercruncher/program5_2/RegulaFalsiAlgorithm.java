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
package com.helger.numbercruncher.program5_2;

import com.helger.numbercruncher.mathutils.RegulaFalsiRootFinder;
import com.helger.numbercruncher.mathutils.SystemOutAlignRight;
import com.helger.numbercruncher.rootutils.RootFunctions;

/**
 * PROGRAM 5-2: Regula Falsi Algorithm Demonstrate the Regula Falsi Algorithm on
 * a function.
 */
public final class RegulaFalsiAlgorithm
{
  /**
   * Main program.
   *
   * @param args
   *        the array of runtime arguments
   */
  public static void main (final String args[])
  {
    try
    {
      final RegulaFalsiRootFinder finder = new RegulaFalsiRootFinder (RootFunctions.function ("x^2 - 4"),
                                                                      -0.25f,
                                                                      3.25f);

      final SystemOutAlignRight ar = new SystemOutAlignRight (System.out);

      ar.print ("n", 2);
      ar.print ("xNeg", 11);
      ar.print ("f(xNeg)", 15);
      ar.print ("xFalse", 11);
      ar.print ("f(xFalse)", 15);
      ar.print ("xPos", 6);
      ar.print ("f(xPos)", 9);
      ar.underline ();

      // Loop until convergence or failure.
      boolean converged;
      do
      {
        converged = finder.step ();

        ar.print (finder.getIterationCount (), 2);
        ar.print (finder.getXNeg (), 11);
        ar.print (finder.getFNeg (), 15);
        ar.print (finder.getXFalse (), 11);
        ar.print (finder.getFFalse (), 15);
        ar.print (finder.getXPos (), 6);
        ar.print (finder.getFPos (), 9);
        ar.println ();
      } while (!converged);

      System.out.println ("\nSuccess! Root = " + finder.getXFalse ());
    }
    catch (final Exception ex)
    {
      System.out.println ("***** Error: " + ex);
    }
  }
}
/*
 * Output: n xNeg f(xNeg) xFalse f(xFalse) xPos f(xPos)
 * --------------------------------------------------------------------- 1 -0.25
 * -3.9375 1.0625 -2.8710938 3.25 6.5625 2 1.0625 -2.8710938 1.7282609
 * -1.0131145 3.25 6.5625 3 1.7282609 -1.0131145 1.9317685 -0.26827025 3.25
 * 6.5625 4 1.9317685 -0.26827025 1.9835405 -0.06556702 3.25 6.5625 5 1.9835405
 * -0.06556702 1.9960688 -0.015709162 3.25 6.5625 6 1.9960688 -0.015709162
 * 1.9990634 -0.0037455559 3.25 6.5625 7 1.9990634 -0.0037455559 1.999777
 * -8.921623E-4 3.25 6.5625 8 1.999777 -8.921623E-4 1.9999468 -2.1266937E-4 3.25
 * 6.5625 9 1.9999468 -2.1266937E-4 1.9999874 -5.054474E-5 3.25 6.5625 10
 * 1.9999874 -5.054474E-5 1.999997 -1.1920929E-5 3.25 6.5625 11 1.999997
 * -1.1920929E-5 1.9999992 -3.33786E-6 3.25 6.5625 Success! Root = 1.9999992
 */
