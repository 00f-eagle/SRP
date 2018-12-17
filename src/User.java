import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class User {

    private BigInteger N;
    private BigInteger g;
    private BigInteger k;


    private BigInteger salt;
    private String name;
    private BigInteger v;
    private BigInteger A;
    private BigInteger a;
    private BigInteger B;

    private BigInteger U;

    private BigInteger K;

    private BigInteger M;

    private BigInteger R;

    private Random random = new Random();


    BigInteger getSalt() {
        return salt;
    }

    String getName() {
        return name;
    }

    BigInteger getV() {
        return v;
    }

    BigInteger getA() {
        return A;
    }

    BigInteger getM() {
        return M;
    }

    void reg(String name, String password){

        try {
            this.name = name;

            salt = new BigInteger(256, random);
            System.out.println("salt:" + salt);

            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(password.getBytes());
            sha.update(salt.toString().getBytes());
            BigInteger x = new BigInteger(1, sha.digest());

            v = g.modPow(x, N);
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    void log_1(String name){

        a = new BigInteger(256, random);

        A = g.modPow(a, N);

        this.name = name;
    }

    boolean scr(BigInteger B){

        this.B = B;

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(A.toString().getBytes());
            sha.update(B.toString().getBytes());
            U = new BigInteger(1, sha.digest());
            System.out.println("User 'U': " + U);
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if(U.equals(BigInteger.ZERO)){
            System.out.println("Ошибка!");
            return false;
        }

        return true;

    }

    void log_2(String password, BigInteger salt){

        this.salt = salt;

        System.out.println("salt:" + salt);

        try {

            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(password.getBytes());
            sha.update(salt.toString().getBytes());
            BigInteger x = new BigInteger(1, sha.digest());

            BigInteger S = (B.subtract(k.multiply(g.modPow(x, N)))).modPow((a.add(U.multiply(x))), N);
            sha.update(S.toString().getBytes());
            K = new BigInteger(1, sha.digest());
            System.out.println("User 'K': " + S);
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }


    }

    void log_3(){

        BigInteger x;

        try {

            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(N.toString().getBytes());
            N = new BigInteger(1, sha.digest());
            sha.update(g.toString().getBytes());
            g = new BigInteger(1, sha.digest());
            sha.update(name.getBytes());
            BigInteger I = new BigInteger(1, sha.digest());
            x = N.xor(g);
            sha.update(x.toString().getBytes());
            sha.update(I.toString().getBytes());
            sha.update(salt.toString().getBytes());
            sha.update(A.toString().getBytes());
            sha.update(B.toString().getBytes());
            sha.update(K.toString().getBytes());
            M = new BigInteger(1, sha.digest());
            System.out.println("User 'M': " + M);
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    boolean log_4(BigInteger R_ser){

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(A.toString().getBytes());
            sha.update(M.toString().getBytes());
            sha.update(K.toString().getBytes());
            R = new BigInteger(1, sha.digest());
            System.out.println("User 'R': " + R);
            if(!R.equals(R_ser)){
                return false;
            }
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return true;
    }

    void SecurityField(BigInteger N, BigInteger g, BigInteger k){

        this.N = N;
        this.g = g;
        this.k = k;

    }

}