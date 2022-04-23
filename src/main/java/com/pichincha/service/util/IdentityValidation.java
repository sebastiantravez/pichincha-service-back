package com.pichincha.service.util;

public class IdentityValidation {

    /**
     * @param dni
     * @return true if is a valid document
     * @throws Exception
     */
    public static boolean dniIdentificationCardValidation(String dni) throws NumberFormatException {

        String number = dni;
        if (number.length() == 10) {
            boolean res = true;
            int sum = 0;
            int residue = 0;
            boolean pri = false;

            boolean nat = false;

            int module = 11;

            int d1 = Integer.parseInt(number.substring(0, 1));
            int d2 = Integer.parseInt(number.substring(1, 2));
            int d3 = Integer.parseInt(number.substring(2, 3));
            int d4 = Integer.parseInt(number.substring(3, 4));
            int d5 = Integer.parseInt(number.substring(4, 5));
            int d6 = Integer.parseInt(number.substring(5, 6));
            int d7 = Integer.parseInt(number.substring(6, 7));
            int d8 = Integer.parseInt(number.substring(7, 8));
            int d9 = Integer.parseInt(number.substring(8, 9));
            int d10 = Integer.parseInt(number.substring(9, 10));

            int p1 = 0;
            int p2 = d2;
            int p3 = 0;
            int p4 = d4;
            int p5 = 0;
            int p6 = d6;
            int p7 = 0;
            int p8 = d8;
            int p9 = 0;

            if (d3 == 7 || d3 == 8 || d3 == 6) {
                res = false;
            }

            if (d3 < 6) {
                nat = true;

                p1 = d1 * 2;
                if (p1 >= 10) {
                    p1 -= 9;
                }

                p3 = d3 * 2;
                if (p3 >= 10) {
                    p3 -= 9;
                }

                p5 = d5 * 2;
                if (p5 >= 10) {
                    p5 -= 9;
                }

                p7 = d7 * 2;
                if (p7 >= 10) {
                    p7 -= 9;
                }

                p9 = d9 * 2;
                if (p9 >= 10) {
                    p9 -= 9;
                }

                module = 10;
            } else if (d3 == 9) {
                pri = true;
                p1 = d1 * 4;
                p2 = d2 * 3;
                p3 = d3 * 2;
                p4 = d4 * 7;
                p5 = d5 * 6;
                p6 = d6 * 5;
                p7 = d7 * 4;
                p8 = d8 * 3;
                p9 = d9 * 2;
            }

            sum = p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
            residue = sum % module;

            int checkDigit = residue == 0 ? 0 : module - residue;

            if (pri) {
                if (checkDigit != d10) {
                    res = false;
                }
            } else if (nat) {
                if (checkDigit != d10) {
                    res = false;
                }
            }

            return res;
        } else {
            return false;
        }
    }

    public static boolean rucCardValidation(String ruc) {

        String number = ruc;
        if (number.length() == 13) {
            boolean res = true;
            int sum = 0;
            int residue = 0;
            boolean pri = false;
            boolean pub = false;
            boolean nat = false;

            int module = 11;

            int d1 = Integer.parseInt(number.substring(0, 1));
            int d2 = Integer.parseInt(number.substring(1, 2));
            int d3 = Integer.parseInt(number.substring(2, 3));
            int d4 = Integer.parseInt(number.substring(3, 4));
            int d5 = Integer.parseInt(number.substring(4, 5));
            int d6 = Integer.parseInt(number.substring(5, 6));
            int d7 = Integer.parseInt(number.substring(6, 7));
            int d8 = Integer.parseInt(number.substring(7, 8));
            int d9 = Integer.parseInt(number.substring(8, 9));
            int d10 = Integer.parseInt(number.substring(9, 10));

            int p1 = 0;
            int p2 = d2;
            int p3 = 0;
            int p4 = d4;
            int p5 = 0;
            int p6 = d6;
            int p7 = 0;
            int p8 = d8;
            int p9 = 0;

            if (d3 == 7 || d3 == 8) {
                res = false;
            }

            if (d2 == 9 && d3 == 6 && number.substring(10, 13).equals("001")) {
                return true;

            }

            if (d3 < 6) {

                nat = true;
                p1 = d1 * 2;
                if (p1 >= 10) {
                    p1 -= 9;
                }

                p3 = d3 * 2;
                if (p3 >= 10) {
                    p3 -= 9;
                }

                p5 = d5 * 2;
                if (p5 >= 10) {
                    p5 -= 9;
                }

                p7 = d7 * 2;
                if (p7 >= 10) {
                    p7 -= 9;
                }

                p9 = d9 * 2;
                if (p9 >= 10) {
                    p9 -= 9;
                }
                module = 10;
            } else if (d3 == 6) {
                pub = true;
                p1 = d1 * 3;
                p2 = d2 * 2;
                p3 = d3 * 7;
                p4 = d4 * 6;
                p5 = d5 * 5;
                p6 = d6 * 4;
                p7 = d7 * 3;
                p8 = d8 * 2;
                p9 = 0;
            } else if (d3 == 9) {
                pri = true;
                p1 = d1 * 4;
                p2 = d2 * 3;
                p3 = d3 * 2;
                p4 = d4 * 7;
                p5 = d5 * 6;
                p6 = d6 * 5;
                p7 = d7 * 4;
                p8 = d8 * 3;
                p9 = d9 * 2;
            }

            sum = p1 + p2 + p3 + p4 + p5 + p6 + p7 + p8 + p9;
            residue = sum % module;

            int checkDigit = residue == 0 ? 0 : module - residue;

            if (pub) {
                if (checkDigit != d9) {
                    res = false;
                }
                if (!number.substring(9, 13).equals("0001")) {
                    res = false;
                }
            } else if (pri) {
                if (checkDigit != d10) {
                    res = false;
                }
                if (!number.substring(10, 13).equals("001")) {
                    res = false;
                }
            } else if (nat) {
                if (checkDigit != d10) {
                    res = false;
                }
                if (number.length() > 10 && !number.substring(10, 13).equals("001")) {
                    res = false;
                }
            }

            return res;
        } else {
            return false;
        }
    }

}
