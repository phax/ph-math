/**
 * Copyright (C) 2014-2015 Philip Helger (www.helger.com)
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
package numbercruncher.program4_10;

import numbercruncher.mathutils.SystemOutAlignRight;

/**
 * PROGRAM 4-10: e to x Inverse Compute e^x at x = -19.5 by using the Taylor
 * series with x = 19.5 and then taking the inverse of the result. The final
 * value should be approximately 3.4e-9
 */
public class EtoXInverse
{
  private static final double x = -19.5;

  public static void main (final String args[])
  {
    final SystemOutAlignRight ar = new SystemOutAlignRight ();

    int k = 0;
    double numerator = 1;
    double denominator = 1;
    double sum = 1; // running sum
    double prevSum = 0; // previous value of running sum
    final double xInverse = -x;

    ar.print ("k", 2);
    ar.print ("Numerator", 24);
    ar.println ();
    ar.print ("Denominator", 26);
    ar.print ("Fraction", 24);
    ar.print ("Running sum", 23);
    ar.underline ();

    // Loop to compute and sum the terms of the Taylor series.
    do
    {
      numerator *= xInverse; // xInverse^k
      denominator *= ++k; // k!

      final double fraction = numerator / denominator;
      prevSum = sum;
      sum += fraction;

      ar.print (k, 2);
      ar.print (numerator, 24);
      ar.println ();
      ar.print (denominator, 26);
      ar.print (fraction, 24);
      ar.print (sum, 23);
      ar.println ();
    } while (prevSum != sum);

    final double result = 1 / sum;
    final double correct = Math.exp (x);
    System.out.println ("\ne^" + x + " = " + result);
    System.out.println ("% error = " + 100 * Math.abs (result - correct) / correct);
  }
}
/*
 * Output: k Numerator Denominator Fraction Running sum
 * ------------------------------------------------------------------------- 1
 * 19.5 1.0 19.5 20.5 2 380.25 2.0 190.125 210.625 3 7414.875 6.0 1235.8125
 * 1446.4375 4 144590.0625 24.0 6024.5859375 7471.0234375 5 2819506.21875 120.0
 * 23495.88515625 30966.90859375 6 5.4980371265625E7 720.0 76361.6267578125
 * 107328.53535156249 7 1.0721172396796875E9 5040.0 212721.67453962052
 * 320050.209891183 8 2.0906286173753906E10 40320.0 518509.08169032505
 * 838559.291581508 9 4.076725803882012E11 362880.0 1123436.343662371
 * 1961995.635243879 10 7.949615317569923E12 3628800.0 2190700.8701416235
 * 4152696.5053855022 11 1.550174986926135E14 3.99168E7 3883515.178887423
 * 8036211.684272925 12 3.022841224505963E15 4.790016E8 6310712.165692062
 * 1.4346923849964987E7 13 5.894540387786628E16 6.2270208E9 9466068.248538094
 * 2.3812992098503083E7 14 1.14943537561839245E18 8.71782912E10
 * 1.3184880774749488E7 3.699787287325257E7 15 2.2413989824558653E19
 * 1.307674368E12 1.7140345007174335E7 5.4138217880426906E7 16
 * 4.370728015788938E20 2.0922789888E13 2.0889795477493722E7 7.502801335792063E7
 * 17 8.522919630788428E21 3.55687428096E14 2.3961824224183973E7
 * 9.898983758210461E7 18 1.6619693280037435E23 6.402373705728E15
 * 2.595864290953264E7 1.2494848049163724E8 19 3.2408401896073E24
 * 1.21645100408832E17 2.6641765091362443E7 1.5159024558299968E8 20
 * 6.319638369734235E25 2.43290200817664E18 2.5975720964078385E7
 * 1.7756596654707807E8 21 1.2323294820981758E27 5.109094217170944E19
 * 2.412031232378707E7 2.0168627887086514E8 22 2.4030424900914428E28
 * 1.1240007277776077E21 2.137936774153854E7 2.230656466124037E8 23
 * 4.6859328556783134E29 2.585201673888498E22 1.812598569391311E7
 * 2.411916323063168E8 24 9.137569068572711E30 6.204484017332394E23
 * 1.4727363376304403E7 2.5591899568262118E8 25 1.7818259683716787E32
 * 1.5511210043330986E25 1.1487343433517434E7 2.674063391161386E8 26
 * 3.4745606383247736E33 4.0329146112660565E26 8615507.575138075
 * 2.7602184669127667E8 27 6.775393244733308E34 1.0888869450418352E28
 * 6222311.026488611 2.822441577177653E8 28 1.3212016827229952E36
 * 3.0488834461171384E29 4333395.179161711 2.86577552896927E8 29
 * 2.5763432813098405E37 8.841761993739701E30 2913834.689436323
 * 2.894913875863633E8 30 5.023869398554189E38 2.6525285981219103E32
 * 1893992.54813361 2.913853801344969E8 31 9.796545327180669E39
 * 8.222838654177922E33 1191382.4093098515 2.925767625438068E8 32
 * 1.9103263388002306E41 2.631308369336935E35 725998.6556731907
 * 2.9330276119948E8 33 3.7251363606604494E42 8.683317618811886E36
 * 428999.20562506723 2.9373176040510505E8 34 7.2640159032878765E43
 * 2.9523279903960412E38 246043.6620496709 2.939778040671547E8 35
 * 1.4164831011411359E45 1.0333147966386144E40 137081.46885624522
 * 2.941148855360109E8 36 2.762142047225215E46 3.719933267899012E41
 * 74252.46229713284 2.9418913799830806E8 37 5.386176992089169E47
 * 1.3763753091226343E43 39133.054453894336 2.94228271052762E8 38
 * 1.050304513457388E49 5.23022617466601E44 20081.435838182617
 * 2.9424835248860013E8 39 2.0480938012419064E50 2.0397882081197442E46
 * 10040.717919091307 2.9425839320651925E8 40 3.9937829124217177E51
 * 8.159152832478977E47 4894.849985557013 2.942632880565048E8 41
 * 7.787876679222349E52 3.3452526613163803E49 2328.0384077649205
 * 2.942656160949125E8 42 1.5186359524483581E54 1.4050061177528798E51
 * 1080.874975033713 2.9426669696988755E8 43 2.961340107274298E55
 * 6.041526306337383E52 490.16423286412567 2.942671871341204E8 44
 * 5.774613209184882E56 2.6582715747884485E54 217.23187592841936
 * 2.9426740436599636E8 45 1.126049575791052E58 1.1962222086548019E56
 * 94.13381290231506 2.9426749849980927E8 46 2.1957966727925513E59
 * 5.5026221598120885E57 39.90455112163355 2.942675384043604E8 47
 * 4.281803511945475E60 2.5862324151116818E59 16.556143550464984
 * 2.9426755496050394E8 48 8.349516848293676E61 1.2413915592536073E61
 * 6.7259333173764 2.9426756168643725E8 49 1.6281557854172669E63
 * 6.082818640342675E62 2.676646932425302 2.942675643630842E8 50
 * 3.1749037815636704E64 3.0414093201713376E64 1.0438923036458678
 * 2.942675654069765E8 51 6.191062374049157E65 1.5511187532873822E66
 * 0.39913529257047886 2.942675658061118E8 52 1.2072571629395856E67
 * 8.065817517094388E67 0.14967573471392956 2.9426756595578754E8 53
 * 2.354151467732192E68 4.2748832840600255E69 0.055069374092860876
 * 2.942675660108569E8 54 4.590595362077774E69 2.308436973392414E71
 * 0.019886162866866428 2.9426756603074306E8 55 8.95166095605166E70
 * 1.2696403353658276E73 0.007050548652798097 2.9426756603779364E8 56
 * 1.7455738864300736E72 7.109985878048635E74 0.0024551017630279085
 * 2.9426756604024875E8 57 3.4038690785386436E73 4.052691950487722E76
 * 8.399032347200739E-4 2.9426756604108864E8 58 6.637544703150355E74
 * 2.350561331282879E78 2.823812599489904E-4 2.9426756604137105E8 59
 * 1.2943212171143192E76 1.3868311854568986E80 9.33293994746663E-5
 * 2.942675660414644E8 60 2.5239263733729226E77 8.320987112741392E81
 * 3.033205482926655E-5 2.942675660414947E8 61 4.921656428077199E78
 * 5.075802138772248E83 9.696312609355702E-6 2.9426756604150444E8 62
 * 9.597230034750539E79 3.146997326038794E85 3.049646707781229E-6
 * 2.942675660415075E8 63 1.871459856776355E81 1.98260831540444E87
 * 9.4393826669419E-7 2.9426756604150844E8 64 3.649346720713892E82
 * 1.2688693218588417E89 2.87606190633386E-7 2.9426756604150873E8 65
 * 7.116226105392089E83 8.247650592082472E90 8.628185719001579E-8
 * 2.942675660415088E8 66 1.3876640905514574E85 5.443449390774431E92
 * 2.5492366897050122E-8 2.942675660415088E8 e^-19.5 = 3.3982678194950715E-9 %
 * error = 1.217062127663519E-14
 */
