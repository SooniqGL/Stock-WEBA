/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.test;

import java.util.Random;

/**
 *
 * @author qz69042
 */
public class MathExercises {
    public static void main(String[] args) {
        MathExercises.generateMultiple(10, 44, 0);
    }

    // a x b - for a, b in {1 ... 9}
    public static void generateMultiple(int totalTests, int totalQ, int optionOne) {
        int firstInt = 0;
        int secondInt = 0;
        int prevInt = 0;
        boolean whichWay = true;
        Random rd = new Random(System.currentTimeMillis());

        for (int i = 0; i < totalTests; i ++) {
            if (i > 0) {
                System.out.println("\n\n\n\n\n");
            }


            for (int j = 0; j < totalQ; j ++) {
                // if optionOne > 0, then one of the item is optionOne;
                // else both items are randomized, but one is at least 5
                if (optionOne > 0) {
                    firstInt = optionOne;
                } else {
                    firstInt = 5 + rd.nextInt(5);
                }

                secondInt = 1 + rd.nextInt(9);
                if (prevInt == secondInt) {
                    if (prevInt < 9) {
                        secondInt = prevInt + 1;
                    } else {
                        secondInt = prevInt - 1;
                    }
                } else {
                    prevInt = secondInt;
                }

                if (j % 2 == 0) {
                    if (whichWay) {
                        System.out.print(firstInt + " X " + secondInt + " = ");
                    } else {
                        System.out.print(secondInt + " X " + firstInt + " = ");
                    }
                } else {
                    if (whichWay) {
                        whichWay = false;
                        System.out.print("\t\t\t\t" + secondInt + " X " + firstInt + " = \n");
                    } else {
                        whichWay = true;
                        System.out.print("\t\t\t\t" + firstInt + " X " + secondInt + " = \n");
                    }
                }
            }
        }
    }
}
