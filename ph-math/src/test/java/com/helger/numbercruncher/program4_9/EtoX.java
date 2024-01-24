/*
 * Copyright (C) 2014-2024 Philip Helger (www.helger.com)
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
package com.helger.numbercruncher.program4_9;

import com.helger.commons.equals.EqualsHelper;
import com.helger.numbercruncher.mathutils.SystemOutAlignRight;

/**
 * PROGRAM 4-9: e to x Compute e^x using the Taylor series with x = -19.5 The
 * final value should be approximately 3.4e-9
 */
public final class EtoX
{
  private static final double X = -19.5;

  public static void main (final String args[])
  {
    final SystemOutAlignRight ar = new SystemOutAlignRight (System.out);

    int k = 0;
    double numerator = 1;
    double denominator = 1;
    double sum = 1; // running sum
    double prevSum = 0; // previous value of running sum

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
      numerator *= X; // x^k
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
    } while (!EqualsHelper.equals (prevSum, sum));

    final double correct = Math.exp (X);
    System.out.println ("\ne^" + X + " = " + sum);
    System.out.println ("% error = " + 100 * Math.abs (sum - correct) / correct);
  }
}
/*
 * Output: k Numerator Denominator Fraction Running sum
 * ------------------------------------------------------------------------- 1
 * -19.5 1.0 -19.5 -18.5 2 380.25 2.0 190.125 171.625 3 -7414.875 6.0 -1235.8125
 * -1064.1875 4 144590.0625 24.0 6024.5859375 4960.3984375 5 -2819506.21875
 * 120.0 -23495.88515625 -18535.48671875 6 5.4980371265625E7 720.0
 * 76361.6267578125 57826.1400390625 7 -1.0721172396796875E9 5040.0
 * -212721.67453962052 -154895.53450055802 8 2.0906286173753906E10 40320.0
 * 518509.08169032505 363613.547189767 9 -4.076725803882012E11 362880.0
 * -1123436.343662371 -759822.7964726039 10 7.949615317569923E12 3628800.0
 * 2190700.8701416235 1430878.0736690196 11 -1.550174986926135E14 3.99168E7
 * -3883515.178887423 -2452637.1052184035 12 3.022841224505963E15 4.790016E8
 * 6310712.165692062 3858075.0604736586 13 -5.894540387786628E16 6.2270208E9
 * -9466068.248538094 -5607993.1880644355 14 1.14943537561839245E18
 * 8.71782912E10 1.3184880774749488E7 7576887.586685052 15
 * -2.2413989824558653E19 1.307674368E12 -1.7140345007174335E7
 * -9563457.420489283 16 4.370728015788938E20 2.0922789888E13
 * 2.0889795477493722E7 1.1326338057004439E7 17 -8.522919630788428E21
 * 3.55687428096E14 -2.3961824224183973E7 -1.2635486167179534E7 18
 * 1.6619693280037435E23 6.402373705728E15 2.595864290953264E7
 * 1.3323156742353106E7 19 -3.2408401896073E24 1.21645100408832E17
 * -2.6641765091362443E7 -1.3318608349009337E7 20 6.319638369734235E25
 * 2.43290200817664E18 2.5975720964078385E7 1.2657112615069048E7 21
 * -1.2323294820981758E27 5.109094217170944E19 -2.412031232378707E7
 * -1.1463199708718022E7 22 2.4030424900914428E28 1.1240007277776077E21
 * 2.137936774153854E7 9916168.032820517 23 -4.6859328556783134E29
 * 2.585201673888498E22 -1.812598569391311E7 -8209817.661092592 24
 * 9.137569068572711E30 6.204484017332394E23 1.4727363376304403E7
 * 6517545.715211811 25 -1.7818259683716787E32 1.5511210043330986E25
 * -1.1487343433517434E7 -4969797.718305623 26 3.4745606383247736E33
 * 4.0329146112660565E26 8615507.575138075 3645709.856832452 27
 * -6.775393244733308E34 1.0888869450418352E28 -6222311.026488611
 * -2576601.1696561584 28 1.3212016827229952E36 3.0488834461171384E29
 * 4333395.179161711 1756794.0095055522 29 -2.5763432813098405E37
 * 8.841761993739701E30 -2913834.689436323 -1157040.6799307708 30
 * 5.023869398554189E38 2.6525285981219103E32 1893992.54813361 736951.8682028393
 * 31 -9.796545327180669E39 8.222838654177922E33 -1191382.4093098515
 * -454430.5411070122 32 1.9103263388002306E41 2.631308369336935E35
 * 725998.6556731907 271568.1145661785 33 -3.7251363606604494E42
 * 8.683317618811886E36 -428999.20562506723 -157431.0910588887 34
 * 7.2640159032878765E43 2.9523279903960412E38 246043.6620496709
 * 88612.5709907822 35 -1.4164831011411359E45 1.0333147966386144E40
 * -137081.46885624522 -48468.89786546302 36 2.762142047225215E46
 * 3.719933267899012E41 74252.46229713284 25783.564431669816 37
 * -5.386176992089169E47 1.3763753091226343E43 -39133.054453894336
 * -13349.49002222452 38 1.050304513457388E49 5.23022617466601E44
 * 20081.435838182617 6731.945815958097 39 -2.0480938012419064E50
 * 2.0397882081197442E46 -10040.717919091307 -3308.7721031332094 40
 * 3.9937829124217177E51 8.159152832478977E47 4894.849985557013
 * 1586.0778824238032 41 -7.787876679222349E52 3.3452526613163803E49
 * -2328.0384077649205 -741.9605253411173 42 1.5186359524483581E54
 * 1.4050061177528798E51 1080.874975033713 338.9144496925958 43
 * -2.961340107274298E55 6.041526306337383E52 -490.16423286412567
 * -151.24978317152988 44 5.774613209184882E56 2.6582715747884485E54
 * 217.23187592841936 65.98209275688947 45 -1.126049575791052E58
 * 1.1962222086548019E56 -94.13381290231506 -28.151720145425585 46
 * 2.1957966727925513E59 5.5026221598120885E57 39.90455112163355
 * 11.752830976207967 47 -4.281803511945475E60 2.5862324151116818E59
 * -16.556143550464984 -4.803312574257017 48 8.349516848293676E61
 * 1.2413915592536073E61 6.7259333173764 1.9226207431193831 49
 * -1.6281557854172669E63 6.082818640342675E62 -2.676646932425302
 * -0.754026189305919 50 3.1749037815636704E64 3.0414093201713376E64
 * 1.0438923036458678 0.2898661143399488 51 -6.191062374049157E65
 * 1.5511187532873822E66 -0.39913529257047886 -0.10926917823053006 52
 * 1.2072571629395856E67 8.065817517094388E67 0.14967573471392956
 * 0.0404065564833995 53 -2.354151467732192E68 4.2748832840600255E69
 * -0.055069374092860876 -0.014662817609461379 54 4.590595362077774E69
 * 2.308436973392414E71 0.019886162866866428 0.005223345257405049 55
 * -8.95166095605166E70 1.2696403353658276E73 -0.007050548652798097
 * -0.0018272033953930476 56 1.7455738864300736E72 7.109985878048635E74
 * 0.0024551017630279085 6.278983676348609E-4 57 -3.4038690785386436E73
 * 4.052691950487722E76 -8.399032347200739E-4 -2.12004867085213E-4 58
 * 6.637544703150355E74 2.350561331282879E78 2.823812599489904E-4
 * 7.03763928637774E-5 59 -1.2943212171143192E76 1.3868311854568986E80
 * -9.33293994746663E-5 -2.29530066108889E-5 60 2.5239263733729226E77
 * 8.320987112741392E81 3.033205482926655E-5 7.37904821837765E-6 61
 * -4.921656428077199E78 5.075802138772248E83 -9.696312609355702E-6
 * -2.3172643909780525E-6 62 9.597230034750539E79 3.146997326038794E85
 * 3.049646707781229E-6 7.323823168031767E-7 63 -1.871459856776355E81
 * 1.98260831540444E87 -9.4393826669419E-7 -2.1155594989101322E-7 64
 * 3.649346720713892E82 1.2688693218588417E89 2.87606190633386E-7
 * 7.605024074237278E-8 65 -7.116226105392089E83 8.247650592082472E90
 * -8.628185719001579E-8 -1.0231616447643008E-8 66 1.3876640905514574E85
 * 5.443449390774431E92 2.5492366897050122E-8 1.5260750449407114E-8 67
 * -2.705944976575342E86 3.647111091818868E94 -7.419420216305633E-9
 * 7.841330233101482E-9 68 5.276592704321917E87 2.4800355424368305E96
 * 2.1276278561464683E-9 9.96895808924795E-9 69 -1.0289355773427739E89
 * 1.711224524281413E98 -6.012861332587845E-10 9.367671955989165E-9 70
 * 2.0064243758184091E90 1.197857166996989E100 1.6750113712209E-10
 * 9.535173093111256E-9 71 -3.9125275328458976E91 8.504785885678622E101
 * -4.600383343494021E-11 9.489169259676316E-9 72 7.629428689049501E92
 * 6.123445837688608E103 1.2459371555296307E-11 9.501628631231612E-9 73
 * -1.4877385943646527E94 4.4701154615126834E105 -3.3281882921681918E-12
 * 9.498300442939443E-9 74 2.901090259011073E95 3.3078854415193856E107
 * 8.770225905037804E-13 9.499177465529948E-9 75 -5.657126005071592E96
 * 2.480914081139539E109 -2.280258735309829E-13 9.498949439656417E-9 76
 * 1.1031395709889603E98 1.8854947016660498E111 5.850663860334429E-14
 * 9.49900794629502E-9 77 -2.1511221634284726E99 1.4518309202828584E113
 * -1.48166162696781E-14 9.49899312967875E-9 78 4.194688218685522E100
 * 1.1324281178206295E115 3.704154067419525E-15 9.498996833832817E-9 79
 * -8.179642026436768E101 8.946182130782973E116 -9.143165103124144E-16
 * 9.498995919516307E-9 80 1.5950301951551696E103 7.156945704626378E118
 * 2.2286464938865103E-16 9.498996142380956E-9 81 -3.110308880552581E104
 * 5.797126020747366E120 -5.365260077874933E-17 9.498996088728356E-9 82
 * 6.065102317077533E105 4.75364333701284E122 1.2758850185190387E-17
 * 9.498996101487206E-9 83 -1.1826949518301188E107 3.945523969720657E124
 * -2.9975611880868984E-18 9.498996098489644E-9 84 2.3062551560687317E108
 * 3.314240134565352E126 6.9586241866303E-19 9.498996099185507E-9 85
 * -4.497197554334027E109 2.8171041143805494E128 -1.596390254579892E-19
 * 9.498996099025867E-9 86 8.769535230951352E110 2.4227095383672724E130
 * 3.619722088873011E-20 9.498996099062065E-9 87 -1.7100593700355137E112
 * 2.107757298379527E132 -8.113170199198129E-21 9.498996099053952E-9 88
 * 3.3346157715692517E113 1.8548264225739836E134 1.7978047600495854E-21
 * 9.49899609905575E-9 89 -6.50250075456004E114 1.6507955160908452E136
 * -3.9390104293221256E-22 9.498996099055356E-9 90 1.2679876471392079E116
 * 1.4857159644817607E138 8.534522596864606E-23 9.498996099055442E-9 91
 * -2.4725759119214553E117 1.3520015276784023E140 -1.828826270756701E-23
 * 9.498996099055424E-9 92 4.821523028246838E118 1.24384140546413E142
 * 3.876316552147356E-24 9.498996099055428E-9 93 -9.401969905081333E119
 * 1.1567725070816409E144 -8.127760512567037E-25 9.498996099055428E-9 e^-19.5 =
 * 9.498996099055428E-9 % error = 179.52464619068274
 */
