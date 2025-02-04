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
package com.helger.numbercruncher.mathutils;

import java.io.PrintStream;

import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;

/**
 * Decompose a floating-point value into its parts according to the IEEE 754
 * standard.
 */
public class IEEE754
{
  /** sign bit as a string */
  private String m_sSignBit;
  /** exponent bits as a string */
  private String m_sExponentBits;
  /** fraction bits as a string */
  private String m_sFractionBits;
  /** implied bit as a string */
  private String m_sImpliedBit;

  /** biased exponent value */
  private int m_nBiased;
  /** fraction value */
  private long m_nFraction;

  /** exponent bias */
  private int m_nBias;

  /** float number value */
  private float m_fFloatValue;
  /** double number value */
  private double m_dDoubleValue;

  /**
   * true if number value is zero
   */
  private boolean m_bIsZero;
  /**
   * true if reserved exponent value
   */
  private boolean m_bIsReserved;
  /**
   * true if number type is double
   */
  private final boolean m_bIsDouble;
  /**
   * true if denormalized number value
   */
  private boolean m_bIsDenormalized;

  // --------------//
  // Constructors //
  // --------------//

  /**
   * Constructor. Decompose a float value.
   *
   * @param value
   *        the float value to decompose
   */
  public IEEE754 (final float value)
  {
    // Convert the value to a character array of '0' and '1'.
    final char bits[] = _toCharBitArray (Float.floatToIntBits (value), 32);

    m_fFloatValue = value;
    m_bIsDouble = false;

    _decompose (bits,
                IEEE754Constants.FLOAT_EXPONENT_BIAS,
                IEEE754Constants.FLOAT_EXPONENT_RESERVED,
                IEEE754Constants.FLOAT_SIGN_INDEX,
                IEEE754Constants.FLOAT_SIGN_SIZE,
                IEEE754Constants.FLOAT_EXPONENT_INDEX,
                IEEE754Constants.FLOAT_EXPONENT_SIZE,
                IEEE754Constants.FLOAT_FRACTION_INDEX,
                IEEE754Constants.FLOAT_FRACTION_SIZE);
  }

  /**
   * Constructor. Decompose a double value.
   *
   * @param value
   *        the double-precision value to decompose
   */
  public IEEE754 (final double value)
  {
    // Convert the value to a character array of '0' and '1'.
    final char bits[] = _toCharBitArray (Double.doubleToLongBits (value), 64);

    m_dDoubleValue = value;
    m_bIsDouble = true;

    _decompose (bits,
                IEEE754Constants.DOUBLE_EXPONENT_BIAS,
                IEEE754Constants.DOUBLE_EXPONENT_RESERVED,
                IEEE754Constants.DOUBLE_SIGN_INDEX,
                IEEE754Constants.DOUBLE_SIGN_SIZE,
                IEEE754Constants.DOUBLE_EXPONENT_INDEX,
                IEEE754Constants.DOUBLE_EXPONENT_SIZE,
                IEEE754Constants.DOUBLE_FRACTION_INDEX,
                IEEE754Constants.DOUBLE_FRACTION_SIZE);
  }

  /**
   * Constructor. Reconstitute a float value.
   *
   * @param sign
   *        the sign bit value, 0 or 1
   * @param biasedExponent
   *        the biased exponent value, 0..255
   * @param fraction
   *        the fraction bits
   * @throws IEEE754Exception
   *         in case of misconfiguration
   */
  public IEEE754 (final int sign, final int biasedExponent, final FloatFraction fraction) throws IEEE754Exception
  {
    // Check the sign.
    if ((sign != 0) && (sign != 1))
    {
      throw new IEEE754Exception ("Invalid sign bit.");
    }

    validateFloatBiasedExponent (biasedExponent);

    // Consolidate the parts. First the sign ...
    int intBits = sign;

    // ... then the biased exponent value ...
    intBits <<= IEEE754Constants.FLOAT_EXPONENT_SIZE;
    intBits += biasedExponent;

    // ... and finally the fraction value.
    intBits <<= IEEE754Constants.FLOAT_FRACTION_SIZE;
    intBits += fraction.toInt ();

    // Convert to the float value.
    m_fFloatValue = Float.intBitsToFloat (intBits);
    m_bIsDouble = false;

    // Convert the value to a character array of '0' and '1'.
    final char bits[] = _toCharBitArray (intBits, 32);

    _decompose (bits,
                IEEE754Constants.FLOAT_EXPONENT_BIAS,
                IEEE754Constants.FLOAT_EXPONENT_RESERVED,
                IEEE754Constants.FLOAT_SIGN_INDEX,
                IEEE754Constants.FLOAT_SIGN_SIZE,
                IEEE754Constants.FLOAT_EXPONENT_INDEX,
                IEEE754Constants.FLOAT_EXPONENT_SIZE,
                IEEE754Constants.FLOAT_FRACTION_INDEX,
                IEEE754Constants.FLOAT_FRACTION_SIZE);
  }

  /**
   * Constructor. Reconstitute a double value.
   *
   * @param sign
   *        the sign bit value, 0 or 1
   * @param biasedExponent
   *        the biased exponent value, 0..2047
   * @param fraction
   *        the fraction bits
   * @throws IEEE754Exception
   *         in case of misconfiguration
   */
  public IEEE754 (final int sign, final int biasedExponent, final DoubleFraction fraction) throws IEEE754Exception
  {
    // Check the sign.
    if ((sign != 0) && (sign != 1))
    {
      throw new IEEE754Exception ("Invalid sign bit.");
    }

    validateDoubleBiasedExponent (biasedExponent);

    // Consolidate the parts. First the sign ...
    long longBits = sign;

    // ... then the biased exponent value ...
    longBits <<= IEEE754Constants.DOUBLE_EXPONENT_SIZE;
    longBits += biasedExponent;

    // ... and finally the fraction value.
    longBits <<= IEEE754Constants.DOUBLE_FRACTION_SIZE;
    longBits += fraction.toLong ();

    // Convert to the double value.
    m_dDoubleValue = Double.longBitsToDouble (longBits);
    m_bIsDouble = true;

    // Convert the value to a character array of '0' and '1'.
    final char bits[] = _toCharBitArray (longBits, 64);

    _decompose (bits,
                IEEE754Constants.DOUBLE_EXPONENT_BIAS,
                IEEE754Constants.DOUBLE_EXPONENT_RESERVED,
                IEEE754Constants.DOUBLE_SIGN_INDEX,
                IEEE754Constants.DOUBLE_SIGN_SIZE,
                IEEE754Constants.DOUBLE_EXPONENT_INDEX,
                IEEE754Constants.DOUBLE_EXPONENT_SIZE,
                IEEE754Constants.DOUBLE_FRACTION_INDEX,
                IEEE754Constants.DOUBLE_FRACTION_SIZE);
  }

  // -------------------------//
  // Methods to return parts //
  // -------------------------//

  /**
   * Return the float value.
   *
   * @return the float value
   */
  public float floatValue ()
  {
    return m_fFloatValue;
  }

  /**
   * Return the double value.
   *
   * @return the double value
   */
  public double doubleValue ()
  {
    return m_dDoubleValue;
  }

  /**
   * Return the biased value of the exponent.
   *
   * @return the unbiased exponent value
   */
  public int biasedExponent ()
  {
    return m_nBiased;
  }

  /**
   * Return the unbiased value of the exponent.
   *
   * @return the unbiased exponent value
   */
  public int unbiasedExponent ()
  {
    return m_bIsDenormalized ? -m_nBias + 1 : m_nBiased - m_nBias;
  }

  /**
   * Return the sign as a string of '0' and '1'.
   *
   * @return the string
   */
  public String signBit ()
  {
    return m_sSignBit;
  }

  /**
   * Return the exponent as a string of '0' and '1'.
   *
   * @return the string
   */
  public String exponentBits ()
  {
    return m_sExponentBits;
  }

  /**
   * Return the fraction as a string of '0' and '1'.
   *
   * @return the string
   */
  public String fractionBits ()
  {
    return m_sFractionBits;
  }

  /**
   * Return the significand as a string of '0', '1' and '.'.
   *
   * @return the string
   */
  public String significandBits ()
  {
    return m_sImpliedBit + "." + m_sFractionBits;
  }

  /**
   * Return whether or not the value is zero.
   *
   * @return true if zero, else false
   */
  public boolean isZero ()
  {
    return m_bIsZero;
  }

  /**
   * Return whether or not the value is a double.
   *
   * @return true if a double, else false
   */
  public boolean isDouble ()
  {
    return m_bIsDouble;
  }

  /**
   * Return whether or not the value is denormalized.
   *
   * @return true if denormalized, else false
   */
  public boolean isDenormalized ()
  {
    return m_bIsDenormalized;
  }

  /**
   * Return whether or not the exponent value is reserved.
   *
   * @return true if reserved, else false
   */
  public boolean isExponentReserved ()
  {
    return m_bIsReserved;
  }

  // -----------------------//
  // Decomposition methods //
  // -----------------------//

  /**
   * Convert a long value into a character array of '0' and '1' that represents
   * the value in base 2.
   *
   * @param pvalue
   *        the long value
   * @param size
   *        the array size
   * @return the character array
   */
  private static char [] _toCharBitArray (final long pvalue, final int size)
  {
    long value = pvalue;
    final char bits[] = new char [size];

    // Convert each bit from right to left.
    for (int i = size - 1; i >= 0; --i)
    {
      bits[i] = (value & 1) == 0 ? '0' : '1';
      value >>>= 1;
    }

    return bits;
  }

  /**
   * Decompose a floating-point value into its parts.
   *
   * @param bits
   *        the character array of '0' and '1' that represents the value in base
   *        2
   * @param bias
   *        the exponent bias value
   * @param reserved
   *        the reserved exponent value (other than 0)
   * @param signIndex
   *        the index of the sign bit
   * @param signSize
   *        the size of the sign bit
   * @param exponentIndex
   *        the index of the exponent
   * @param exponentSize
   *        the size of the exponent
   * @param fractionIndex
   *        the index of the fraction
   * @param fractionSize
   *        the size of the fraction
   */
  private void _decompose (final char [] bits,
                           final int bias,
                           final int reserved,
                           final int signIndex,
                           final int signSize,
                           final int exponentIndex,
                           final int exponentSize,
                           final int fractionIndex,
                           final int fractionSize)
  {
    this.m_nBias = bias;

    // Extract the individual parts as strings of '0' and '1'.
    m_sSignBit = new String (bits, signIndex, signSize);
    m_sExponentBits = new String (bits, exponentIndex, exponentSize);
    m_sFractionBits = new String (bits, fractionIndex, fractionSize);

    try
    {
      m_nBiased = Integer.parseInt (m_sExponentBits, 2);
      m_nFraction = Long.parseLong (m_sFractionBits, 2);
    }
    catch (final NumberFormatException ex)
    {}

    m_bIsZero = (m_nBiased == 0) && (m_nFraction == 0);
    m_bIsDenormalized = (m_nBiased == 0) && (m_nFraction != 0);
    m_bIsReserved = (m_nBiased == reserved);

    m_sImpliedBit = m_bIsDenormalized || m_bIsZero || m_bIsReserved ? "0" : "1";
  }

  /**
   * Print the decomposed parts of the value.
   *
   * @param aPW
   *        the PrintStream to write on
   */
  public void print (@Nonnull @WillNotClose final PrintStream aPW)
  {
    aPW.println ("------------------------------");

    // Print the value.
    if (isDouble ())
      aPW.println ("double value = " + doubleValue ());
    else
      aPW.println ("float value = " + floatValue ());

    // Print the sign.
    aPW.print ("sign=" + signBit ());

    // Print the bit representation of the exponent and its
    // biased and unbiased values. Indicate whether the value
    // is denormalized, or whether the exponent is reserved.
    aPW.print (", exponent=" + exponentBits () + " (biased=" + biasedExponent ());

    if (isZero ())
      aPW.println (", zero)");
    else
      if (isExponentReserved ())
        aPW.println (", reserved)");
      else
        if (isDenormalized ())
          aPW.println (", denormalized, use " + unbiasedExponent () + ")");
        else
          aPW.println (", normalized, unbiased=" + unbiasedExponent () + ")");

    // Print the significand.
    aPW.println ("significand=" + significandBits ());
  }

  // --------------------------------//
  // Compute and validate exponents //
  // --------------------------------//

  /**
   * Compute the value of the float biased exponent given the unbiased value.
   *
   * @param unbiased
   *        the unbiased exponent value
   * @return the biased exponent value
   */
  public static int toFloatBiasedExponent (final int unbiased)
  {
    return unbiased + IEEE754Constants.FLOAT_EXPONENT_BIAS;
  }

  /**
   * Compute the value of the float unbiased exponent given the biased value.
   *
   * @param biased
   *        the biased exponent value
   * @return the unbiased exponent value
   */
  public static int toFloatUnbiasedExponent (final int biased)
  {
    return biased == 0 ? -IEEE754Constants.FLOAT_EXPONENT_BIAS + 1 : biased - IEEE754Constants.FLOAT_EXPONENT_BIAS;
  }

  /**
   * Compute the value of the double biased exponent given the unbiased value.
   *
   * @param unbiased
   *        the unbiased exponent value
   * @return the biased exponent value
   */
  public static int toDoubleBiasedExponent (final int unbiased)
  {
    return unbiased + IEEE754Constants.DOUBLE_EXPONENT_BIAS;
  }

  /**
   * Compute the value of the double unbiased exponent given the biased value.
   *
   * @param biased
   *        the biased exponent value
   * @return the unbiased exponent value
   */
  public static int toDoubleUnbiasedExponent (final int biased)
  {
    return biased == 0 ? -IEEE754Constants.DOUBLE_EXPONENT_BIAS + 1 : biased - IEEE754Constants.DOUBLE_EXPONENT_BIAS;
  }

  /**
   * Validate the value of the float biased exponent value.
   *
   * @param biased
   *        the biased exponent value
   * @throws IEEE754Exception
   *         in case of misconfiguration
   */
  public static void validateFloatBiasedExponent (final int biased) throws IEEE754Exception
  {
    if ((biased < 0) || (biased > IEEE754Constants.FLOAT_EXPONENT_RESERVED))
    {
      throw new IEEE754Exception ("The biased exponent value should be " + "0 through " + IEEE754Constants.FLOAT_EXPONENT_RESERVED + ".");
    }
  }

  /**
   * Validate the value of the float unbiased exponent value.
   *
   * @param unbiased
   *        the unbiased exponent value
   * @throws IEEE754Exception
   *         in case of misconfiguration
   */
  public static void validateFloatUnbiasedExponent (final int unbiased) throws IEEE754Exception
  {
    if ((unbiased < -IEEE754Constants.FLOAT_EXPONENT_BIAS + 1) || (unbiased > IEEE754Constants.FLOAT_EXPONENT_BIAS))
    {
      throw new IEEE754Exception ("The unbiased exponent value should be " +
                                  -(IEEE754Constants.FLOAT_EXPONENT_BIAS - 1) +
                                  " through " +
                                  IEEE754Constants.FLOAT_EXPONENT_BIAS +
                                  ".");
    }
  }

  /**
   * Validate the value of the double biased exponent value.
   *
   * @param biased
   *        the biased exponent value
   * @throws IEEE754Exception
   *         in case of misconfiguration
   */
  public static void validateDoubleBiasedExponent (final int biased) throws IEEE754Exception
  {
    if ((biased < 0) || (biased > IEEE754Constants.DOUBLE_EXPONENT_RESERVED))
    {
      throw new IEEE754Exception ("The biased exponent value should be " + "0 through " + IEEE754Constants.DOUBLE_EXPONENT_RESERVED + ".");
    }
  }

  /**
   * Validate the value of the double unbiased exponent value.
   *
   * @param unbiased
   *        the unbiased exponent value
   * @throws IEEE754Exception
   *         in case of misconfiguration
   */
  public static void validateDoubleUnbiasedExponent (final int unbiased) throws IEEE754Exception
  {
    if ((unbiased < -IEEE754Constants.DOUBLE_EXPONENT_BIAS + 1) || (unbiased > IEEE754Constants.DOUBLE_EXPONENT_BIAS))
    {
      throw new IEEE754Exception ("The unbiased exponent value should be " +
                                  -(IEEE754Constants.DOUBLE_EXPONENT_BIAS - 1) +
                                  " through " +
                                  IEEE754Constants.DOUBLE_EXPONENT_BIAS +
                                  ".");
    }
  }

  // ------------------------------//
  // Nested decomposition classes //
  // ------------------------------//

  /**
   * IEEE 754 exception.
   */
  public static class IEEE754Exception extends Exception
  {
    public IEEE754Exception (final String message)
    {
      super (message);
    }
  }

  /**
   * Abstract base class for the IEEE 754 part classes.
   */
  private static abstract class AbstractPart
  {
    /** the part buffer */
    private final StringBuilder m_aPart;

    /**
     * Constructor.
     *
     * @param size
     *        the bit size of the part
     * @param bits
     *        the string of character bits '0' and '1'
     * @throws IEEE754Exception
     *         in case of misconfiguration
     */
    protected AbstractPart (final int size, final String bits) throws IEEE754Exception
    {
      if (size <= 0)
        throw new IEEE754Exception ("Invalid part size: " + size);

      final int length = bits.length ();
      m_aPart = new StringBuilder (size);

      // String length matches part size.
      if (length == size)
      {
        m_aPart.append (bits);
        _validate ();
      }

      // String length < part size: Pad with '0'.
      else
        if (length < size)
        {
          m_aPart.append (bits);
          _validate ();
          for (int i = length; i < size; ++i)
            m_aPart.append ('0');
        }

        // String length > part size: Truncate at the right end.
        else
        {
          m_aPart.append (bits.substring (0, size));
          _validate ();
        }
    }

    /**
     * Convert the part to an integer value.
     *
     * @return the integer value
     * @throws IEEE754Exception
     *         if the binary number format is invalid
     */
    protected int toInt () throws IEEE754Exception
    {
      try
      {
        return Integer.parseInt (m_aPart.toString (), 2);
      }
      catch (final NumberFormatException ex)
      {
        throw new IEEE754Exception ("Invalid binary number format: " + m_aPart.toString ());
      }
    }

    /**
     * Convert the part to an long value.
     *
     * @return the long value
     * @throws IEEE754Exception
     *         if the binary number format is invalid
     */
    protected long toLong () throws IEEE754Exception
    {
      try
      {
        return Long.parseLong (m_aPart.toString (), 2);
      }
      catch (final NumberFormatException ex)
      {
        throw new IEEE754Exception ("Invalid binary number format: " + m_aPart.toString ());
      }
    }

    /**
     * Return the part as a string of characters '0' and '1'.
     */
    @Override
    public String toString ()
    {
      return m_aPart.toString ();
    }

    /**
     * Validate that the part consists only of '0' and '1'.
     *
     * @throws IEEE754Exception
     */
    private void _validate () throws IEEE754Exception
    {
      final int length = m_aPart.length ();

      for (int i = 0; i < length; ++i)
      {
        final char bit = m_aPart.charAt (i);
        if ((bit != '0') && (bit != '1'))
        {
          throw new IEEE754Exception ("Invalid fraction bit string.");
        }
      }
    }
  }

  /**
   * The IEEE 754 fraction part for a float.
   */
  public static class FloatFraction extends AbstractPart
  {
    /**
     * Constructor.
     *
     * @param bits
     *        the string of character bits '0' and '1'
     * @throws IEEE754Exception
     *         in case of misconfiguration
     */
    public FloatFraction (final String bits) throws IEEE754Exception
    {
      super (IEEE754Constants.FLOAT_FRACTION_SIZE, bits);
    }
  }

  /**
   * The IEEE 754 fraction part for a double.
   */
  public static class DoubleFraction extends AbstractPart
  {
    /**
     * Constructor.
     *
     * @param bits
     *        the string of character bits '0' and '1'
     * @throws IEEE754Exception
     *         in case of misconfiguration
     */
    public DoubleFraction (final String bits) throws IEEE754Exception
    {
      super (IEEE754Constants.DOUBLE_FRACTION_SIZE, bits);
    }
  }
}
