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
package com.helger.numbercruncher.matrix;

/**
 * A square matrix.
 */
public class SquareMatrix extends Matrix
{
  // --------------//
  // Constructors //
  // --------------//

  /**
   * Constructor.
   *
   * @param n
   *        the number of rows == the number of columns
   */
  public SquareMatrix (final int n)
  {
    super (n, n);
  }

  /**
   * Constructor.
   *
   * @param m
   *        the matrix (only the upper left square used)
   */
  private SquareMatrix (final Matrix m)
  {
    _set (m);
  }

  /**
   * Constructor.
   *
   * @param values
   *        the array of values
   */
  public SquareMatrix (final float values[][])
  {
    set (values);
  }

  // ---------//
  // Setters //
  // ---------//

  /**
   * Set this square matrix from another matrix. Note that this matrix will
   * reference the values of the argument matrix. If the values are not square,
   * only the upper left square is used.
   *
   * @param m
   *        the 2-d array of values
   */
  private void _set (final Matrix m)
  {
    this.m_nRows = this.m_nCols = Math.min (m.m_nRows, m.m_nCols);
    this.m_aValues = m.m_aValues;
  }

  /**
   * Set this square matrix from a 2-d array of values. If the values are not
   * square, only the upper left square is used.
   *
   * @param values
   *        the 2-d array of values
   */
  @Override
  protected void set (final float values[][])
  {
    super.set (values);
    m_nRows = m_nCols = Math.min (m_nRows, m_nCols);
  }

  // -------------------//
  // Matrix operations //
  // -------------------//

  /**
   * Add another square matrix to this matrix.
   *
   * @param sm
   *        the square matrix addend
   * @return the sum matrix
   * @throws MatrixException
   *         for invalid size
   */
  public SquareMatrix add (final SquareMatrix sm) throws MatrixException
  {
    return new SquareMatrix (super.add (sm));
  }

  /**
   * Subtract another square matrix from this matrix.
   *
   * @param sm
   *        the square matrix subrrahend
   * @return the difference matrix
   * @throws MatrixException
   *         for invalid size
   */
  public SquareMatrix subtract (final SquareMatrix sm) throws MatrixException
  {
    return new SquareMatrix (super.subtract (sm));
  }

  /**
   * Multiply this square matrix by another square matrix.
   *
   * @param sm
   *        the square matrix multiplier
   * @return the product matrix
   * @throws MatrixException
   *         for invalid size
   */
  public SquareMatrix multiply (final SquareMatrix sm) throws MatrixException
  {
    return new SquareMatrix (super.multiply (sm));
  }
}
