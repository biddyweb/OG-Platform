/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.lowlevelapi.slatec.fnlib;

import static org.testng.Assert.assertTrue;

import org.testng.annotations.Test;

import com.opengamma.analytics.math.statistics.distribution.fnlib.D1MACH;
import com.opengamma.maths.commonapi.numbers.ComplexType;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGComplexMatrix;

/**
 * Tests complex square root
 */
@Test
public class ZACOSTest {
  private static double[][] realP = { {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 },
      {-10.00, -9.00, -8.00, -7.00, -6.00, -5.00, -4.00, -3.00, -2.00, -1.00, 0.00, 1.00, 2.00, 3.00, 4.00, 5.00, 6.00, 7.00, 8.00, 9.00, 10.00 } };

  private static double[][] imagP = {
      {-10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00, -10.00 },
      {-9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00, -9.00 },
      {-8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00, -8.00 },
      {-7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00, -7.00 },
      {-6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00, -6.00 },
      {-5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00, -5.00 },
      {-4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00, -4.00 },
      {-3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00, -3.00 },
      {-2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00, -2.00 },
      {-1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00, -1.00 },
      {0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
      {1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 },
      {2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00, 2.00 },
      {3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00, 3.00 },
      {4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00, 4.00 },
      {5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00, 5.00 },
      {6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00, 6.00 },
      {7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00, 7.00 },
      {8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00, 8.00 },
      {9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00, 9.00 },
      {10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00, 10.00 } };

  private static double[][] answerReal = {
      {2.3549444967026849, 2.3022384499390838, 2.2440515612478014, 2.1799485095682281, 2.1095980660119524, 2.0328496863345729, 1.9498233117356969, 1.8609978143781314, 1.7672734424245091,
          1.6699783706733893, 1.5707963267948966, 1.4716142829164038, 1.3743192111652842, 1.2805948392116620, 1.1917693418540964, 1.1087429672552203, 1.0319945875778407, 0.9616441440215654,
          0.8975410923419920, 0.8393542036507097, 0.7866481568871084 },
      {2.4074033782552986, 2.3546512925660785, 2.2957274744928142, 2.2299782545939473, 2.1568314187146038, 2.0758998199471423, 1.9871174725869842, 1.8908912590439122, 1.7882294733604964,
          1.6807902176833425, 1.5707963267948966, 1.4608024359064509, 1.3533631802292969, 1.2507013945458809, 1.1544751810028089, 1.0656928336426508, 0.9847612348751894, 0.9116143989958458,
          0.8458651790969788, 0.7869413610237145, 0.7341892753344944 },
      {2.4653630177474248, 2.4132370433090773, 2.3542413900268975, 2.2874354808048412, 2.2119025095979516, 2.1268800471555043, 2.0319579620472439, 1.9273324865836015, 1.8140615598670655,
          1.6942150364935811, 1.5707963267948966, 1.4473776170962123, 1.3275310937227278, 1.2142601670061917, 1.1096346915425495, 1.0147126064342891, 0.9296901439918419, 0.8541571727849522,
          0.7873512635628958, 0.7283556102807158, 0.6762296358423684 },
      {2.5292874754130619, 2.4786829499559091, 2.4205679446613617, 2.3536435251190877, 2.2765200224831852, 2.1878606234708902, 2.0866482359885832, 1.9725944896463568, 1.8466335921930224,
          1.7113132328670746, 1.5707963267948966, 1.4302794207227187, 1.2949590613967710, 1.1689981639434364, 1.0549444176012102, 0.9537320301189031, 0.8650726311066081, 0.7879491284707053,
          0.7210247089284315, 0.6629097036338841, 0.6123051781767311 },
      {2.5995469739091250, 2.5516128130070133, 2.4956865394259937, 2.4300559610436756, 2.3527224074982294, 2.2615125609651279, 2.1543858138333358, 2.0300437302573160, 1.8888523766658130,
          1.7337947996302656, 1.5707963267948966, 1.4077978539595277, 1.2527402769239804, 1.1115489233324773, 0.9872068397564572, 0.8800800926246655, 0.7888702460915641, 0.7115366925461174,
          0.6459061141637993, 0.5899798405827802, 0.5420456796806680 },
      {2.6763392752783917, 2.6324841678163486, 2.5804590965965000, 2.5181369711079453, 2.4428144632371356, 2.3511949067802806, 2.2396129021208240, 2.1047953963890653, 1.9454671316204235,
          1.7645894633498287, 1.5707963267948966, 1.3770031902399646, 1.1961255219693698, 1.0367972572007280, 0.9019797514689694, 0.7903977468095125, 0.6987781903526574, 0.6234556824818479,
          0.5611335569932935, 0.5091084857734447, 0.4652533783114015 },
      {2.7595929665334444, 2.7214453164305028, 2.6754309467378397, 2.6191135153665366, 2.5491277061594602, 2.4608795884460077, 2.3483835789165575, 2.2047801924340735, 2.0246665367580192,
          1.8091137886047628, 1.5707963267948966, 1.3324788649850305, 1.1169261168317741, 0.9368124611557198, 0.7932090746732356, 0.6807130651437854, 0.5924649474303331, 0.5224791382232566,
          0.4661617068519537, 0.4201473371592904, 0.3819996870563488 },
      {2.8488660468603713, 2.8181642542632210, 2.7805526108780820, 2.7335515735176816, 2.6734559591267417, 2.5946180733066795, 2.4884131801463751, 2.3423145189167447, 2.1414491111159961,
          1.8783999763256081, 1.5707963267948966, 1.2631926772641853, 1.0001435424737972, 0.7992781346730484, 0.6531794734434180, 0.5469745802831136, 0.4681366944630513, 0.4080410800721115,
          0.3610400427117111, 0.3234283993265722, 0.2927266067294218 },
      {2.9432663397549543, 2.9216679935640539, 2.8948668832841360, 2.8607707118296721, 2.8160350803180085, 2.7550280110699186, 2.6677178756250397, 2.5354548312024994, 2.3250454714929427,
          1.9978749131873728, 1.5707963267948966, 1.1437177404024204, 0.8165471820968506, 0.6061378223872937, 0.4738747779647538, 0.3865646425198747, 0.3255575732717849, 0.2808219417601211,
          0.2467257703056573, 0.2199246600257394, 0.1983263138348390 },
      {3.0414302567368403, 3.0302601572746872, 3.0162801952536040, 2.9982751205352254, 2.9742096986341546, 2.9403975738888830, 2.8894132448734400, 2.8038915443242409, 2.6342363503726487,
          2.2370357592874122, 1.5707963267948966, 0.9045568943023813, 0.5073563032171445, 0.3377011092655525, 0.2521794087163534, 0.2011950797009102, 0.1673829549556387, 0.1433175330545678,
          0.1253124583361894, 0.1113324963151061, 0.1001623968529529 },
      {3.1415926535897931, 3.1415926535897931, 3.1415926535897931, 3.1415926535897931, 3.1415926535897931, 3.1415926535897931, 3.1415926535897931, 3.1415926535897931, 3.1415926535897931,
          3.1415926535897931, 1.5707963267948966, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 0.0000000000000000,
          0.0000000000000000, 0.0000000000000000, 0.0000000000000000 },
      {3.0414302567368403, 3.0302601572746872, 3.0162801952536040, 2.9982751205352254, 2.9742096986341546, 2.9403975738888830, 2.8894132448734400, 2.8038915443242409, 2.6342363503726487,
          2.2370357592874122, 1.5707963267948966, 0.9045568943023813, 0.5073563032171445, 0.3377011092655525, 0.2521794087163534, 0.2011950797009102, 0.1673829549556387, 0.1433175330545678,
          0.1253124583361894, 0.1113324963151061, 0.1001623968529529 },
      {2.9432663397549543, 2.9216679935640539, 2.8948668832841360, 2.8607707118296721, 2.8160350803180085, 2.7550280110699186, 2.6677178756250397, 2.5354548312024994, 2.3250454714929427,
          1.9978749131873728, 1.5707963267948966, 1.1437177404024204, 0.8165471820968506, 0.6061378223872937, 0.4738747779647538, 0.3865646425198747, 0.3255575732717849, 0.2808219417601211,
          0.2467257703056573, 0.2199246600257394, 0.1983263138348390 },
      {2.8488660468603713, 2.8181642542632210, 2.7805526108780820, 2.7335515735176816, 2.6734559591267417, 2.5946180733066795, 2.4884131801463751, 2.3423145189167447, 2.1414491111159961,
          1.8783999763256081, 1.5707963267948966, 1.2631926772641853, 1.0001435424737972, 0.7992781346730484, 0.6531794734434180, 0.5469745802831136, 0.4681366944630513, 0.4080410800721115,
          0.3610400427117111, 0.3234283993265722, 0.2927266067294218 },
      {2.7595929665334444, 2.7214453164305028, 2.6754309467378397, 2.6191135153665366, 2.5491277061594602, 2.4608795884460077, 2.3483835789165575, 2.2047801924340735, 2.0246665367580192,
          1.8091137886047628, 1.5707963267948966, 1.3324788649850305, 1.1169261168317741, 0.9368124611557198, 0.7932090746732356, 0.6807130651437854, 0.5924649474303331, 0.5224791382232566,
          0.4661617068519537, 0.4201473371592904, 0.3819996870563488 },
      {2.6763392752783917, 2.6324841678163486, 2.5804590965965000, 2.5181369711079453, 2.4428144632371356, 2.3511949067802806, 2.2396129021208240, 2.1047953963890653, 1.9454671316204235,
          1.7645894633498287, 1.5707963267948966, 1.3770031902399646, 1.1961255219693698, 1.0367972572007280, 0.9019797514689694, 0.7903977468095125, 0.6987781903526574, 0.6234556824818479,
          0.5611335569932935, 0.5091084857734447, 0.4652533783114015 },
      {2.5995469739091250, 2.5516128130070133, 2.4956865394259937, 2.4300559610436756, 2.3527224074982294, 2.2615125609651279, 2.1543858138333358, 2.0300437302573160, 1.8888523766658130,
          1.7337947996302656, 1.5707963267948966, 1.4077978539595277, 1.2527402769239804, 1.1115489233324773, 0.9872068397564572, 0.8800800926246655, 0.7888702460915641, 0.7115366925461174,
          0.6459061141637993, 0.5899798405827802, 0.5420456796806680 },
      {2.5292874754130619, 2.4786829499559091, 2.4205679446613617, 2.3536435251190877, 2.2765200224831852, 2.1878606234708902, 2.0866482359885832, 1.9725944896463568, 1.8466335921930224,
          1.7113132328670746, 1.5707963267948966, 1.4302794207227187, 1.2949590613967710, 1.1689981639434364, 1.0549444176012102, 0.9537320301189031, 0.8650726311066081, 0.7879491284707053,
          0.7210247089284315, 0.6629097036338841, 0.6123051781767311 },
      {2.4653630177474248, 2.4132370433090773, 2.3542413900268975, 2.2874354808048412, 2.2119025095979516, 2.1268800471555043, 2.0319579620472439, 1.9273324865836015, 1.8140615598670655,
          1.6942150364935811, 1.5707963267948966, 1.4473776170962123, 1.3275310937227278, 1.2142601670061917, 1.1096346915425495, 1.0147126064342891, 0.9296901439918419, 0.8541571727849522,
          0.7873512635628958, 0.7283556102807158, 0.6762296358423684 },
      {2.4074033782552986, 2.3546512925660785, 2.2957274744928142, 2.2299782545939473, 2.1568314187146038, 2.0758998199471423, 1.9871174725869842, 1.8908912590439122, 1.7882294733604964,
          1.6807902176833425, 1.5707963267948966, 1.4608024359064509, 1.3533631802292969, 1.2507013945458809, 1.1544751810028089, 1.0656928336426508, 0.9847612348751894, 0.9116143989958458,
          0.8458651790969788, 0.7869413610237145, 0.7341892753344944 },
      {2.3549444967026849, 2.3022384499390838, 2.2440515612478014, 2.1799485095682281, 2.1095980660119524, 2.0328496863345729, 1.9498233117356969, 1.8609978143781314, 1.7672734424245091,
          1.6699783706733893, 1.5707963267948966, 1.4716142829164038, 1.3743192111652842, 1.2805948392116620, 1.1917693418540964, 1.1087429672552203, 1.0319945875778407, 0.9616441440215654,
          0.8975410923419920, 0.8393542036507097, 0.7866481568871084 } };

  private static double[][] answerImag = {
      {3.3423082075626018, 3.2925434814760544, 3.2434181591469957, 3.1956978516606149, 3.1503424784884602, 3.1085057043690791, 3.0715025569270047, 3.0407328278260111, 3.0175554791917567,
          3.0031252437131291, 2.9982229502979698, 3.0031252437131291, 3.0175554791917567, 3.0407328278260111, 3.0715025569270047, 3.1085057043690791, 3.1503424784884602, 3.1956978516606149,
          3.2434181591469957, 3.2925434814760544, 3.3423082075626018 },
      {3.2922535079883675, 3.2369489203715971, 3.1817205225071623, 3.1273926375040930, 3.0750607679468684, 3.0261163654875274, 2.9822307095329315, 2.9452709724930921, 2.9171288698209166,
          2.8994686991707197, 2.8934439858858716, 2.8994686991707197, 2.9171288698209166, 2.9452709724930921, 2.9822307095329315, 3.0261163654875274, 3.0750607679468684, 3.1273926375040930,
          3.1817205225071623, 3.2369489203715971, 3.2922535079883675 },
      {3.2427489292018197, 3.1813162536860236, 3.1191680344383270, 3.0571418389619964, 2.9964401392355255, 2.9387034887363708, 2.8860395049475405, 2.8409546699274197, 2.8061337001907227,
          2.7840492640208856, 2.7764722807237177, 2.7840492640208856, 2.8061337001907227, 2.8409546699274197, 2.8860395049475405, 2.9387034887363708, 2.9964401392355255, 3.0571418389619964,
          3.1191680344383270, 3.1813162536860236, 3.2427489292018197 },
      {3.1945492820340240, 3.1264459241248415, 3.0565545070216835, 2.9856406810823723, 2.9149349663103048, 2.8462888282083862, 2.7823040403441328, 2.7263424970102745, 2.6822833445007306,
          2.6539273355384174, 2.6441207610586290, 2.6539273355384174, 2.6822833445007306, 2.7263424970102745, 2.7823040403441328, 2.8462888282083862, 2.9149349663103048, 2.9856406810823723,
          3.0565545070216835, 3.1264459241248415, 3.1945492820340240 },
      {3.1486124158814870, 3.0734171726978485, 2.9950402175839939, 2.9140353877209031, 2.8314983232869921, 2.7493465969740996, 2.6706422224804807, 2.5998241937784723, 2.5425702259303136,
          2.5049441482332582, 2.4917798526449122, 2.5049441482332582, 2.5425702259303136, 2.5998241937784723, 2.6706422224804807, 2.7493465969740996, 2.8314983232869921, 2.9140353877209031,
          2.9950402175839939, 3.0734171726978485, 3.1486124158814870 },
      {3.1061057542892012, 3.0236244624862745, 2.9362418228665041, 2.8440976626506527, 2.7478687371476949, 2.6491961778064712, 2.5513216254756985, 2.4598315216234345, 2.3830308809003258,
          2.3309746530493123, 2.3124383412727525, 2.3309746530493123, 2.3830308809003258, 2.4598315216234345, 2.5513216254756985, 2.6491961778064712, 2.7478687371476949, 2.8440976626506527,
          2.9362418228665041, 3.0236244624862745, 3.1061057542892012 },
      {3.0683813163569509, 2.9787766619541034, 2.8822896953783657, 2.7783990940505472, 2.6669446837131048, 2.5486455784678337, 2.4261066521153900, 2.3055090312434769, 2.1985730279209359,
          2.1225501238100715, 2.0947125472611012, 2.1225501238100715, 2.1985730279209359, 2.3055090312434769, 2.4261066521153900, 2.5486455784678337, 2.6669446837131048, 2.7783990940505472,
          2.8822896953783657, 2.9787766619541034, 3.0683813163569509 },
      {3.0369031980974488, 2.9408265783541698, 2.8357943742419178, 2.7203975979400625, 2.5931585970908038, 2.4529137425028118, 2.2999140408792695, 2.1386220863162211, 1.9833870299165355,
          1.8641615441578825, 1.8184464592320668, 1.8641615441578825, 1.9833870299165355, 2.1386220863162211, 2.2999140408792695, 2.4529137425028118, 2.5931585970908038, 2.7203975979400625,
          2.8357943742419178, 2.9408265783541698, 3.0369031980974488 },
      {3.0131175744705696, 2.9118001066982249, 2.7996457775654280, 2.6742734458074566, 2.5325707993262854, 2.3705485373179198, 2.1835852165645639, 1.9686379257930964, 1.7343245214879666,
          1.5285709194809982, 1.4436354751788103, 1.5285709194809982, 1.7343245214879666, 1.9686379257930964, 2.1835852165645639, 2.3705485373179198, 2.5325707993262854, 2.6742734458074566,
          2.7996457775654280, 2.9118001066982249, 3.0131175744705696 },
      {2.9982726947086702, 2.8935197103979768, 2.7765933641736349, 2.6443267863946120, 2.4921599681338353, 2.3132209417695293, 2.0965964572888911, 1.8241987021938828, 1.4693517443681852,
          1.0612750619050357, 0.8813735870195430, 1.0612750619050357, 1.4693517443681852, 1.8241987021938828, 2.0965964572888911, 2.3132209417695293, 2.4921599681338353, 2.6443267863946120,
          2.7765933641736349, 2.8935197103979768, 2.9982726947086702 },
      {-2.9932228461263808, -2.8872709503576206, -2.7686593833135738, -2.6339157938496336, -2.4778887302884751, -2.2924316695611777, -2.0634370688955608, -1.7627471740390861, -1.3169578969248166,
          0.0000000000000000, 0.0000000000000000, 0.0000000000000000, 1.3169578969248166, 1.7627471740390861, 2.0634370688955608, 2.2924316695611777, 2.4778887302884751, 2.6339157938496336,
          2.7686593833135738, 2.8872709503576206, 2.9932228461263808 },
      {-2.9982726947086702, -2.8935197103979768, -2.7765933641736349, -2.6443267863946120, -2.4921599681338353, -2.3132209417695293, -2.0965964572888911, -1.8241987021938828, -1.4693517443681852,
          -1.0612750619050357, -0.8813735870195430, -1.0612750619050357, -1.4693517443681852, -1.8241987021938828, -2.0965964572888911, -2.3132209417695293, -2.4921599681338353, -2.6443267863946120,
          -2.7765933641736349, -2.8935197103979768, -2.9982726947086702 },
      {-3.0131175744705696, -2.9118001066982249, -2.7996457775654280, -2.6742734458074566, -2.5325707993262854, -2.3705485373179198, -2.1835852165645639, -1.9686379257930964, -1.7343245214879666,
          -1.5285709194809982, -1.4436354751788103, -1.5285709194809982, -1.7343245214879666, -1.9686379257930964, -2.1835852165645639, -2.3705485373179198, -2.5325707993262854, -2.6742734458074566,
          -2.7996457775654280, -2.9118001066982249, -3.0131175744705696 },
      {-3.0369031980974488, -2.9408265783541698, -2.8357943742419178, -2.7203975979400625, -2.5931585970908038, -2.4529137425028118, -2.2999140408792695, -2.1386220863162211, -1.9833870299165355,
          -1.8641615441578825, -1.8184464592320668, -1.8641615441578825, -1.9833870299165355, -2.1386220863162211, -2.2999140408792695, -2.4529137425028118, -2.5931585970908038, -2.7203975979400625,
          -2.8357943742419178, -2.9408265783541698, -3.0369031980974488 },
      {-3.0683813163569509, -2.9787766619541034, -2.8822896953783657, -2.7783990940505472, -2.6669446837131048, -2.5486455784678337, -2.4261066521153900, -2.3055090312434769, -2.1985730279209359,
          -2.1225501238100715, -2.0947125472611012, -2.1225501238100715, -2.1985730279209359, -2.3055090312434769, -2.4261066521153900, -2.5486455784678337, -2.6669446837131048, -2.7783990940505472,
          -2.8822896953783657, -2.9787766619541034, -3.0683813163569509 },
      {-3.1061057542892012, -3.0236244624862745, -2.9362418228665041, -2.8440976626506527, -2.7478687371476949, -2.6491961778064712, -2.5513216254756985, -2.4598315216234345, -2.3830308809003258,
          -2.3309746530493123, -2.3124383412727525, -2.3309746530493123, -2.3830308809003258, -2.4598315216234345, -2.5513216254756985, -2.6491961778064712, -2.7478687371476949, -2.8440976626506527,
          -2.9362418228665041, -3.0236244624862745, -3.1061057542892012 },
      {-3.1486124158814870, -3.0734171726978485, -2.9950402175839939, -2.9140353877209031, -2.8314983232869921, -2.7493465969740996, -2.6706422224804807, -2.5998241937784723, -2.5425702259303136,
          -2.5049441482332582, -2.4917798526449122, -2.5049441482332582, -2.5425702259303136, -2.5998241937784723, -2.6706422224804807, -2.7493465969740996, -2.8314983232869921, -2.9140353877209031,
          -2.9950402175839939, -3.0734171726978485, -3.1486124158814870 },
      {-3.1945492820340240, -3.1264459241248415, -3.0565545070216835, -2.9856406810823723, -2.9149349663103048, -2.8462888282083862, -2.7823040403441328, -2.7263424970102745, -2.6822833445007306,
          -2.6539273355384174, -2.6441207610586290, -2.6539273355384174, -2.6822833445007306, -2.7263424970102745, -2.7823040403441328, -2.8462888282083862, -2.9149349663103048, -2.9856406810823723,
          -3.0565545070216835, -3.1264459241248415, -3.1945492820340240 },
      {-3.2427489292018197, -3.1813162536860236, -3.1191680344383270, -3.0571418389619964, -2.9964401392355255, -2.9387034887363708, -2.8860395049475405, -2.8409546699274197, -2.8061337001907227,
          -2.7840492640208856, -2.7764722807237177, -2.7840492640208856, -2.8061337001907227, -2.8409546699274197, -2.8860395049475405, -2.9387034887363708, -2.9964401392355255, -3.0571418389619964,
          -3.1191680344383270, -3.1813162536860236, -3.2427489292018197 },
      {-3.2922535079883675, -3.2369489203715971, -3.1817205225071623, -3.1273926375040930, -3.0750607679468684, -3.0261163654875274, -2.9822307095329315, -2.9452709724930921, -2.9171288698209166,
          -2.8994686991707197, -2.8934439858858716, -2.8994686991707197, -2.9171288698209166, -2.9452709724930921, -2.9822307095329315, -3.0261163654875274, -3.0750607679468684, -3.1273926375040930,
          -3.1817205225071623, -3.2369489203715971, -3.2922535079883675 },
      {-3.3423082075626018, -3.2925434814760544, -3.2434181591469957, -3.1956978516606149, -3.1503424784884602, -3.1085057043690791, -3.0715025569270047, -3.0407328278260111, -3.0175554791917567,
          -3.0031252437131291, -2.9982229502979698, -3.0031252437131291, -3.0175554791917567, -3.0407328278260111, -3.0715025569270047, -3.1085057043690791, -3.1503424784884602, -3.1956978516606149,
          -3.2434181591469957, -3.2925434814760544, -3.3423082075626018 } };

  @Test
  public void correctnessTest() {
    OGComplexMatrix c = new OGComplexMatrix(realP, imagP);
    ComplexType ans, tabans;
    final int rows = c.getNumberOfRows();
    final int cols = c.getNumberOfColumns();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < cols; j++) {
        ans = new ComplexType(ZACOS.zacos(new double[] {c.getEntry(i, j).getReal(), c.getEntry(i, j).getImag() }));
        tabans = new ComplexType(answerReal[i][j], answerImag[i][j]);
        assertTrue(Math.abs(ans.getReal() - tabans.getReal()) < 1000 * D1MACH.four());
        assertTrue(Math.abs(ans.getImag() - tabans.getImag()) < 1000 * D1MACH.four());
      }
    }
  }

}
