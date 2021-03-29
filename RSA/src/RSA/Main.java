package RSA;


import java.io.FileReader;
import java.io.FileWriter;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;


public class Main {

    public static class GDC {
        BigInteger c;
        BigInteger x;
        BigInteger y;

        GDC(BigInteger one, BigInteger two, BigInteger three) {
            c = one;
            x = two;
            y = three;
        }

        GDC() {
        }

    }


    public static BigInteger Exp(BigInteger a, BigInteger b) {
        if (b.compareTo(BigInteger.ZERO) == 0 && !(a.compareTo(BigInteger.ZERO) == 0)) {
            return BigInteger.ONE;
        }
        BigInteger tmp = a;
        for (BigInteger i = BigInteger.ONE; !(i.compareTo(b) == 0); i = i.add(BigInteger.ONE)) {
            tmp = tmp.multiply(a);
        }
        return tmp;
    }

    public static BigInteger Eu(BigInteger a, BigInteger b){
        BigInteger tmp;
        while (b.compareTo(BigInteger.ZERO)!=0){
            tmp = a.mod(b);
            a = b;
            b = tmp;
        }
        return a;
    }


    public static GDC ExEu(BigInteger a, BigInteger b) {
            GDC ans = new GDC(b, BigInteger.ZERO, BigInteger.ONE);
            GDC tmp;


            if (a == BigInteger.ZERO) {
                return ans;
            }
            tmp = ExEu(b.mod(a), a);
            ans = new GDC();
            ans.c = tmp.c;
            ans.x = (tmp.y).subtract(((b.divide(a)).multiply(tmp.x)));
            ans.y = tmp.x;
            return ans;
        }
    public static BigInteger ModPow(BigInteger a, BigInteger b, BigInteger n){
            BigInteger u = BigInteger.ONE;
            BigInteger v = a ;
            for(int i = 0; i < b.bitLength(); i++ ){
                if(b.testBit(i)){
                    u = (u.multiply(v)).mod(n);
                }
                v = (v.multiply(v)).mod(n);
            }

        return u;
    }
    public static boolean FermaTest(BigInteger a, BigInteger n){
        if(Eu(a,n).compareTo(BigInteger.ONE) != 0){
            return false;
        }
        if(MontPow(a,n.subtract(BigInteger.ONE),n).compareTo(BigInteger.ONE)!=0){
//        if(ModPow(a,n.subtract(BigInteger.ONE),n).compareTo(BigInteger.ONE)!=0){
            return false;
        }
        return true;
    }

    public static BigInteger Jakobi(BigInteger a, BigInteger n){
        BigInteger tmp;
        BigInteger tmp2;
        BigInteger j = BigInteger.ONE;
        if(a.compareTo(BigInteger.ZERO) < 0){
            a = a.negate();
            if(n.mod(BigInteger.valueOf(4)).compareTo(BigInteger.valueOf(3))==0){
                j = j.negate();
            }
        }
    while (a.compareTo(BigInteger.ZERO)!=0) {
        BigInteger t = BigInteger.ZERO;
        while (a.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0) {
          t = t.add(BigInteger.ONE);
          a = a.divide(BigInteger.valueOf(2));
        }

        if (t.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) != 0) {
            tmp = n.mod(BigInteger.valueOf(8));
            if (tmp.compareTo(BigInteger.valueOf(5)) == 0 || tmp.compareTo(BigInteger.valueOf(3)) == 0) {
                j = j.negate();
            }
        }


        tmp2 = a.mod(BigInteger.valueOf(4));
        if (tmp2.compareTo(n.mod(BigInteger.valueOf(4))) == 0 && tmp2.compareTo(BigInteger.valueOf(3)) == 0) {
            j = j.negate();
        }


        BigInteger tmp3 = a;
        a = n.mod(tmp3);
        n = tmp3;
        }
        return j;
        }

    public static boolean SST(BigInteger a,BigInteger n){
        if(Eu(a,n).compareTo(BigInteger.ONE) != 0){
            return false;
        }
//        BigInteger tmp = ModPow(a,n.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)),n);
        BigInteger tmp = MontPow(a,n.subtract(BigInteger.ONE).divide(BigInteger.valueOf(2)),n);
        BigInteger jacobi = Jakobi(a,n);
        if (tmp.compareTo(jacobi)==0){
            return true;
        }
        if(tmp.compareTo(jacobi.add(n)) == 0){
            return true;
        }
        return false;
    }
    public static boolean RM(BigInteger a, BigInteger n, BigInteger r,BigInteger s){
        if(Eu(a,n).compareTo(BigInteger.ONE) != 0){
            return false;
        }
//        BigInteger v = ModPow(a,r,n);
        BigInteger v = MontPow(a,r,n);
        if(v.compareTo(BigInteger.ONE)==0){
            return true;
        }
        for (BigInteger i = BigInteger.ZERO; i.compareTo(s) != 0; i = i.add(BigInteger.ONE)) {
            if (v.compareTo(n.subtract(BigInteger.ONE)) == 0) {
                return true;
            }
            v = MontPow(v, BigInteger.valueOf(2), a);
//            v = ModPow(v, BigInteger.valueOf(2), a);
        }
        return false;
    }

    public static boolean isPrime(BigInteger a, int steps) {
        if (a.compareTo(BigInteger.valueOf(2)) < 0) {
//            System.out.println("<2");
            return false;
        }
        if (a.compareTo(BigInteger.valueOf(2)) == 0) {
            return true;
        }
        if ((a.mod(BigInteger.valueOf(2))).compareTo(BigInteger.ZERO) == 0) {
//            System.out.println("mod2");
            return false;
        }
        Random random = new Random();
        BigInteger s = BigInteger.ZERO;
        BigInteger r = a.subtract(BigInteger.ONE);
        while (r.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0) {
            r = r.divide(BigInteger.valueOf(2));
            s = s.add(BigInteger.ONE);
        }

        for (int i = 1; i <= steps; i++) {
            BigInteger rng = new BigInteger(a.bitLength(), random);
//            Boolean c1 = true, c2 = true, c3 = true;
            if(!FermaTest(rng,a)){
//
                return false;
            }
//
            if(!RM(rng,a,r,s)){
//
                return false;
            }
// //           else {System.out.print("     +      ");}
            if(!SST(rng,a)){
//                System.out.print(" - |");
//                c3 = false;
                return false;
            }
//            else{System.out.print(" + |");}
//            System.out.println(a);
//            if((!c1|!c2|!c3)){
//                return false;
//            }
//
        }
        return true;
    }


    public static BigInteger RandomPrime(int l){
        BigInteger rng;
        BigInteger lLimit = (BigInteger.ONE.shiftLeft(l-1));
        BigInteger uLimit = (BigInteger.ONE.shiftLeft(l));
        Random random = new Random();
        System.out.println("=====================");
//        System.out.println("A: "+ a +"| Steps: " + steps);
        System.out.println("Ferma|RabinMiller|SST|A");
        int i = 0;
        while (true){
            rng = new BigInteger(l, random);
            rng = rng.mod(uLimit).add(lLimit);

            if (isPrime(rng, 100)){

                return rng;
            }
        }

    }
    public static BigInteger Euler(BigInteger p, BigInteger q){
        return (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
    }
    public static BigInteger Karl(BigInteger p, BigInteger q){
        BigInteger gcd = Eu(p.subtract(BigInteger.ONE), q.subtract(BigInteger.ONE));
        return (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)).divide(gcd));
    }
    public static BigInteger Ferma(int n){
        return (Exp(BigInteger.valueOf(2), Exp(BigInteger.valueOf(2),BigInteger.valueOf(n)))).add(BigInteger.ONE);
    }
    public static BigInteger Encr(BigInteger modulus, BigInteger e, BigInteger x){
//        BigInteger y = ModPow(x,e,modulus);
        BigInteger y = MontPow(x,e,modulus);
        return y;
    }
    public static BigInteger Decr(BigInteger modulus, BigInteger d, BigInteger y){
//        BigInteger x = ModPow(y,d,modulus);
        BigInteger x = MontPow(y,d,modulus);
        return x;
    }
    public static void Save(BigInteger p, BigInteger q, BigInteger e, BigInteger modulus,
                            BigInteger d, BigInteger fm, BigInteger fmm)throws Exception{
        FileWriter fw = new FileWriter("Keys.txt");
        fw.write(p.toString()+'\n' + q.toString() + '\n' + e.toString() + '\n' + modulus.toString() + '\n'
                    + d.toString() + '\n' + fm.toString() + '\n' + fmm.toString() );
        fw.close();
    }
    public static BigInteger[] Read()throws Exception{
        BigInteger[] arr = new BigInteger[7];
        FileReader fr = new FileReader("Keys.txt");
        Scanner scanner = new Scanner(fr);
        arr[0]=scanner.nextBigInteger();
        arr[1]=scanner.nextBigInteger();
        arr[2]=scanner.nextBigInteger();
        arr[3]=scanner.nextBigInteger();
        arr[4]=scanner.nextBigInteger();
        arr[5]=scanner.nextBigInteger();
        arr[6]=scanner.nextBigInteger();
        fr.close();
        return arr;
    }
    public static BigInteger[] KeyGen(int l, int lFerma){
        BigInteger[] arr = new BigInteger[7];

        arr[0] = RandomPrime(l);//p
        arr[1] = RandomPrime(l);//q
        while (arr[0].equals(arr[1])){
            arr[1] = RandomPrime(l);
        }
        arr[2] = Ferma(lFerma);//e
        arr[3] = arr[0].multiply(arr[1]);//modulus
        arr[4] = Karl(arr[0],arr[1]);//fm
        arr[5] = Euler(arr[0],arr[1]);//fmm
        arr[6] = ExEu(arr[2], arr[4]).x;//d
        return arr;
    }
//    public static BigInteger M(){;}

    public static BigInteger Mont(BigInteger a, BigInteger z, BigInteger r, int k, BigInteger n, BigInteger nn){
        BigInteger t = a.multiply(z);
        BigInteger u = (t.add(((t.multiply(nn)).and((r.subtract(BigInteger.ONE)))).multiply(n))).shiftRight(k);
        while (u.compareTo(n)>=0) {
            u = u.subtract(n);
        }
        return u;
    }

    public static BigInteger MontPow(BigInteger a, BigInteger e, BigInteger n){
        int k = n.bitLength();
        BigInteger r = (BigInteger.ONE).shiftLeft(k);
        GDC gdc = ExEu(r,n);
        BigInteger rn = gdc.x;
        BigInteger nn = gdc.y;
        nn = nn.negate();
        BigInteger z = r.subtract(n);
        BigInteger an = (a.multiply(r)).mod(n);
        while ((e.compareTo(BigInteger.ZERO))>0){
         //   z = Mont(z,z,r,k,n,nn);
            if ((e.and(BigInteger.ONE).compareTo(BigInteger.ZERO))>0){
                z = Mont(an,z,r,k,n,nn);
            }
            an = Mont(an,an,r,k,n,nn);
            e = e.shiftRight(1);
        }

        return (z.multiply(rn)).remainder(n);
    }



    public static void main(String[] args)throws Exception{
        Random ran = new Random();
        int l=1024;
        int lFerma=5;
        BigInteger n = BigInteger.valueOf(1533);
        GDC gdc;
             BigInteger p, q, e, modulus, fm, fmm, d, encrypt, decrypt, txt;
        BigInteger[] arr;
        arr = KeyGen(l, lFerma);

        p = arr[0];     q = arr[1];     e = arr[2];     modulus = arr[3];
        fm = arr[4];    fmm = arr[5];   d = arr[6];
       // Save(p,q,e,modulus,d,fm,fmm);
        System.out.println("=====================");
        System.out.println("Q:                 " +q);
        System.out.println("P:                 " +p);
        System.out.println("E:                 " +e);
        System.out.println("M:                 " +modulus);
        System.out.println("Функция Кармайкла: " + fm);
        System.out.println("Функция Эйлера:    " + fmm);
        System.out.println("D:                 " + d);
        System.out.println("=====================");

        txt = BigInteger.valueOf(8008135);
        encrypt = Encr(modulus, e, txt);
        decrypt = Decr(modulus, d, encrypt);

        System.out.println("TXT:       " + txt);
        System.out.println("Encrypted: " + encrypt);
        System.out.println("Decrypted: " + decrypt);
        System.out.println("=====================");


    }
}
