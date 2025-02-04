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
package com.helger.numbercruncher.program12_1;

import java.math.BigInteger;

import org.junit.Test;

/**
 * PROGRAM 12-1: Big Prime Number Demonstrate BigInteger by computing the
 * Mersenne prime 2^11213 - 1.
 */
public final class BigPrimeFuncTest
{
  private static final int EXPONENT = 11213;

  /** the prime number */
  private BigInteger m_aPrime;

  /**
   * Compute and print 2^EXPONENT - 1.
   */
  @Test
  public void testCompute () throws Exception
  {
    // Compute the value.
    m_aPrime = BigInteger.valueOf (1);
    for (int i = 1; i <= EXPONENT; ++i)
    {
      m_aPrime = m_aPrime.add (m_aPrime);
    }
    m_aPrime = m_aPrime.subtract (BigInteger.valueOf (1));

    // Print it.
    System.out.println ("2^" + EXPONENT + " - 1 = ");
    print (m_aPrime);
  }

  /**
   * Print the big prime number.
   *
   * @param prime
   *        the big prime number
   */
  private void print (final BigInteger prime)
  {
    final String primeString = prime.toString ();

    final int length = primeString.length ();
    final int groups = length / 3; // no. of groups of three digits
    final int exGroups = length % 3; // no. of extra-group digits
    @SuppressWarnings ("unused")
    final int lines = groups / 16; // no. of lines of 16 groups
    final int exLines = groups % 16; // no. of extra-line groups

    int index = 0; // substring index
    final int lineWidth = 4 * 16;

    // Print a right-justified partial line, if any.
    if (exLines > 0)
    {
      final int padding = lineWidth - 4 * exLines - exGroups - 1;
      for (int i = 0; i < padding; ++i)
        System.out.print (" ");

      // Print the extra-group digits.
      System.out.print (primeString.substring (0, exGroups) + ",");
      index = exGroups;

      // Print the extra-line groups.
      for (int i = 0; i < exLines; ++i)
      {
        index = printGroup (primeString, index, length);
      }
      System.out.println ();
    }

    int count = 0; // group counter

    // Loop to print whole lines.
    while (index < length)
    {
      index = printGroup (primeString, index, length);

      // End of line?
      if (++count == 16)
      {
        System.out.println ();
        count = 0;
      }
    }

    // Print statistics.
    System.out.println ();
    System.out.println ("Number of digits = " + length);
    System.out.println ("Number of bits   = " + prime.bitLength ());
  }

  /**
   * Print a group of digits followed by a comma.
   *
   * @param primeString
   *        the prime number as a string
   * @param pindex
   *        the substring index
   * @param length
   *        the string length
   * @return the new index value
   */
  private int printGroup (final String primeString, final int pindex, final int length)
  {
    int index = pindex;
    System.out.print (primeString.substring (index, index += 3));

    // Append a comma unless it's the last group.
    if (index < length)
      System.out.print (",");

    return index;
  }
}
/**
 * Output: 2^11213 - 1 =<br>
 * 2,814,112,013,697,373,
 * 133,393,152,975,842,584,191,818,662,382,013,600,787,892,419,349,
 * 345,515,176,682,276,313,810,715,094,745,633,257,074,198,789,308,
 * 535,071,537,342,445,016,418,881,801,789,390,548,709,414,391,857,
 * 257,571,565,758,706,478,418,356,747,070,674,633,497,188,053,050,
 * 875,416,821,624,325,680,555,826,071,110,691,946,607,460,873,056,
 * 965,360,830,571,590,242,774,934,226,866,183,966,309,185,433,462,
 * 514,537,484,258,655,982,386,235,046,029,227,507,801,410,907,163,
 * 348,439,547,781,093,397,260,096,909,677,091,843,944,555,754,221,
 * 115,477,343,760,206,979,650,067,087,884,993,478,012,977,277,878,
 * 532,807,432,236,554,020,931,571,802,310,429,923,167,588,432,457,
 * 036,104,110,850,960,439,769,038,450,365,514,022,349,625,383,665,
 * 751,207,169,661,697,352,732,236,111,926,846,454,751,701,734,527,
 * 011,379,148,175,107,820,821,297,628,946,795,631,098,960,767,492,
 * 250,494,834,254,073,334,414,121,627,833,939,461,539,212,528,932,
 * 010,726,136,689,293,688,815,665,491,671,395,174,710,452,663,709,
 * 175,753,603,774,156,855,766,515,313,827,613,727,281,696,692,633,
 * 529,666,363,787,286,539,769,941,609,107,777,183,593,336,002,680,
 * 124,517,633,451,490,439,598,324,823,836,457,251,219,406,391,432,
 * 635,639,225,604,556,042,396,004,307,799,361,927,379,900,586,400,
 * 420,763,092,320,813,392,262,492,942,076,312,933,268,033,818,471,
 * 555,255,820,639,308,889,948,665,570,202,403,815,856,313,578,949,
 * 779,767,046,261,845,327,956,725,767,289,205,262,311,752,014,786,
 * 247,813,331,834,015,084,475,386,760,526,612,217,340,579,721,237,
 * 414,485,803,725,355,463,022,009,536,301,008,145,867,524,704,604,
 * 618,862,039,093,555,206,195,328,240,951,895,107,040,793,284,825,
 * 095,462,530,151,872,823,997,171,764,140,663,315,804,309,008,611,
 * 942,578,380,931,064,748,991,594,407,476,328,437,785,848,825,423,
 * 921,170,614,938,294,029,483,257,162,979,299,388,940,695,877,375,
 * 448,948,081,108,345,293,394,327,808,452,729,789,834,135,140,193,
 * 912,419,661,799,488,795,210,328,238,112,742,218,700,634,541,149,
 * 743,657,287,232,843,426,369,348,804,878,993,471,962,403,393,967,
 * 857,676,150,371,600,196,650,252,168,250,117,793,178,488,012,000,
 * 505,422,821,362,550,520,509,209,724,459,895,852,366,827,477,851,
 * 619,190,503,254,853,115,029,403,132,178,989,005,195,751,194,301,
 * 340,277,282,730,390,683,651,120,587,895,060,198,753,121,882,187,
 * 788,657,024,007,291,784,186,518,589,977,788,510,306,743,945,896,
 * 108,645,258,766,415,692,825,664,174,470,616,153,305,144,852,273,
 * 884,549,635,059,255,410,606,458,427,323,864,109,506,687,636,314,
 * 447,514,269,094,932,953,219,924,212,594,695,157,655,009,158,521,
 * 173,420,923,275,882,063,327,625,408,617,963,032,962,033,572,563,
 * 553,604,056,097,832,111,547,535,908,988,433,816,919,747,615,817,
 * 161,606,620,557,307,000,377,194,730,013,431,815,560,750,159,027,
 * 842,164,901,422,544,571,224,546,936,793,234,970,894,954,668,425,
 * 436,412,347,785,376,194,310,030,139,080,568,383,420,772,628,618,
 * 722,646,109,707,506,566,928,102,800,033,961,704,343,991,962,002,
 * 059,794,565,527,774,913,883,237,756,792,720,065,543,768,640,792,
 * 177,441,559,278,272,350,823,092,843,683,534,396,679,150,229,676,
 * 101,834,243,787,820,420,087,274,028,617,212,684,576,388,733,605,
 * 769,491,224,109,866,592,577,360,666,241,467,280,158,988,605,523,
 * 486,345,880,882,227,855,505,706,309,276,349,415,034,547,677,180,
 * 618,296,352,866,263,005,509,222,254,318,459,768,194,126,727,603,
 * 047,460,344,175,581,029,298,320,171,226,355,234,439,676,816,309,
 * 919,127,574,206,334,807,719,021,875,413,891,580,871,529,049,187,
 * 829,308,412,133,400,910,419,756,313,021,540,478,436,604,178,446,
 * 757,738,998,632,083,586,207,992,234,085,162,634,375,406,771,169,
 * 707,323,213,988,284,943,779,122,171,985,953,605,897,902,291,781,
 * 768,286,548,287,878,180,415,060,635,460,047,164,104,095,483,777,
 * 201,737,468,873,324,068,550,430,695,826,210,304,316,336,385,311,
 * 384,093,490,021,332,372,463,463,373,977,427,405,896,673,827,544,
 * 203,128,574,874,581,960,335,232,005,637,229,319,592,369,288,171,
 * 375,276,702,260,450,911,735,069,504,025,016,667,755,214,932,073,
 * 643,654,199,488,477,010,363,909,372,005,757,899,989,580,775,775,
 * 126,621,113,057,905,717,449,417,222,016,070,530,243,916,116,705,
 * 990,451,304,256,206,318,289,297,738,303,095,152,430,549,772,239,
 * 514,964,821,601,838,628,861,446,301,936,017,710,546,777,503,109,
 * 263,030,994,747,397,618,576,207,373,447,725,441,427,135,362,428,
 * 360,863,669,327,157,635,983,045,447,971,816,718,801,639,869,547,
 * 525,146,305,655,571,843,717,916,875,669,140,320,724,978,568,586,
 * 718,527,586,602,439,602,335,283,513,944,980,064,327,030,278,104,
 * 224,144,971,883,680,541,689,784,796,267,391,476,087,696,392,191<br>
 * <br>
 * Number of digits = 3376<br>
 * Number of bits = 11213
 */
