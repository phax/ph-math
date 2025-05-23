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
package com.helger.numbercruncher.program11_1;

import com.helger.numbercruncher.matrix.InvertibleMatrix;
import com.helger.numbercruncher.matrix.MatrixException;
import com.helger.numbercruncher.matrix.SquareMatrix;

/**
 * PROGRAM 11-1: Hilbert Matrices Test the matrix inverter with a Hilbert
 * matrix. Hilbert matrices are ill-conditioned and difficult to invert
 * accurately.
 */
public final class MainHilbertMatrix
{
  private static final int RANK = 4;

  private void run (final int rank) throws MatrixException
  {
    System.out.println ("Hilbert matrix of rank " + rank);
    final InvertibleMatrix H = new InvertibleMatrix (rank);

    // Compute the Hilbert matrix.
    for (int r = 0; r < rank; ++r)
    {
      for (int c = 0; c < rank; ++c)
      {
        H.set (r, c, 1.0f / (r + c + 1));
      }
    }
    H.print (15, System.out);

    // Invert the Hilbert matrix.
    final InvertibleMatrix Hinv = H.inverse ();
    System.out.println ("\nHilbert matrix inverted");
    Hinv.print (15, System.out);

    System.out.println ("\nHilbert matrix condition number = " + H.norm () * Hinv.norm ());

    // Invert the inverse.
    final InvertibleMatrix HinvInv = Hinv.inverse ();
    System.out.println ("\nInverse matrix inverted");
    HinvInv.print (15, System.out);

    // Multiply P = H*Hinv.
    System.out.println ("\nHilbert matrix times its inverse " + "(should be identity)");
    final SquareMatrix P = H.multiply (Hinv);
    P.print (15, System.out);

    // Average norm of P's rows.
    float normSum = 0;
    for (int r = 0; r < rank; ++r)
    {
      normSum += P.getRow (r).norm ();
    }
    System.out.println ("\nAverage row norm = " + normSum / rank + " (should be 1)");
  }

  /**
   * Main.
   *
   * @param args
   *        the array of arguments
   */
  public static void main (final String args[])
  {
    final MainHilbertMatrix test = new MainHilbertMatrix ();

    try
    {
      test.run (RANK);
    }
    catch (final MatrixException ex)
    {
      System.out.println ("*** ERROR: " + ex.getMessage ());
      ex.printStackTrace ();
    }
  }
}
/*
 * Output: Hilbert matrix of rank 4 Row 1: 1.0 0.5 0.33333334 0.25 Row 2: 0.5
 * 0.33333334 0.25 0.2 Row 3: 0.33333334 0.25 0.2 0.16666667 Row 4: 0.25 0.2
 * 0.16666667 0.14285715 Hilbert matrix inverted Row 1: 15.999718 -119.9972
 * 239.99367 -139.99605 Row 2: -119.9972 1199.9725 -2699.9382 1679.9615 Row 3:
 * 239.99367 -2699.9382 6479.862 -4199.914 Row 4: -139.99605 1679.9615 -4199.914
 * 2799.9465 Hilbert matrix condition number = 15613.463 Inverse matrix inverted
 * Row 1: 0.9999907 0.49999347 0.33332846 0.24999614 Row 2: 0.49999347
 * 0.33332878 0.2499966 0.19999732 Row 3: 0.33332846 0.2499966 0.19999747
 * 0.16666469 Row 4: 0.24999614 0.19999732 0.16666469 0.14285558 Hilbert matrix
 * times its inverse (should be identity) Row 1: 1.0 3.0517578E-5 1.2207031E-4
 * -6.1035156E-5 Row 2: -3.8146973E-6 1.0 0.0 -6.1035156E-5 Row 3: -3.8146973E-6
 * 0.0 1.0 -3.0517578E-5 Row 4: 0.0 1.5258789E-5 6.1035156E-5 0.99993896 Average
 * row norm = 0.99998474 (should be 1)
 */
