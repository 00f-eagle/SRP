import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Server {


    private List<String> Users = new ArrayList<>();
    private Map<String, BigInteger> salt = new HashMap<>();
    private Map<String, BigInteger> pv = new HashMap<>();
    private BigInteger s;
    private String name;

    private BigInteger A;

    private BigInteger B;
    private BigInteger b;
    private BigInteger U;
    private BigInteger M;
    private BigInteger R;

    private BigInteger K;

    private BigInteger N;
    private BigInteger g;
    private BigInteger k;

    Random random = new Random();

    public BigInteger getB() {
        return B;
    }

    public BigInteger getN() {
        return N;
    }

    public BigInteger getG() {
        return g;
    }

    public BigInteger getK() {
        return k;
    }

    public BigInteger getR() {
        return R;
    }

    BigInteger getS() {
        return s;
    }

    void reg(String name, BigInteger salt, BigInteger pv){

        Users.add(name);
        this.salt.put(name, salt);
        this.pv.put(name, pv);

    }

    boolean us(String name){
        if(name.equals("")){
            System.out.println("Данное имя пользователя использовать нельзя!");
            return false;
        }
        for (int i = 0; i < Users.size(); i++) {
            if(Users.get(i).equals(name)){
                System.out.println("Имя пользователя занято!");
                return false;
            }
        }
        return true;
    }

    boolean log_1(String name, BigInteger A){

        if(A.equals(BigInteger.ZERO)){
            return false;
        }

        boolean bool = false;

        for (int i = 0; i < Users.size(); i++) {
            if(Users.get(i).equals(name)) {
                bool = true;
                break;
            }
        }

        if(!bool){
            return false;
        }

        this.A = A;
        this.s = salt.get(name);
        this.name = name;

        b = new BigInteger(256, random);

        B = (k.multiply(pv.get(name)).add(g.modPow(b, N))).mod(N);

        if(!scr()){
            return false;
        }

        return true;

    }

    boolean scr(){

        try {
            if(B.equals(BigInteger.ZERO) || A.equals(BigInteger.ZERO)){
                return false;
            }

            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(A.toString().getBytes());
            sha.update(B.toString().getBytes());
            U = new BigInteger(1, sha.digest());
            System.out.println("Server 'U': " + U);

            if(U.equals(BigInteger.ZERO)){
                return false;
            }
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return true;
    }

    void log_2(){

        try {
            BigInteger S = (A.multiply(pv.get(name).modPow(U,N))).modPow(b, N);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(S.toString().getBytes());
            K = new BigInteger(1, sha.digest());
            System.out.println("Server 'K': " + S);
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    boolean log_3(BigInteger M_us){

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
            sha.update(s.toString().getBytes());
            sha.update(A.toString().getBytes());
            sha.update(B.toString().getBytes());
            sha.update(K.toString().getBytes());
            M = new BigInteger(1, sha.digest());
            System.out.println("Server 'M': " + M);
            if(!M.equals(M_us)){
                return false;
            }
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return true;
    }

    void log_4(){

        try {

            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(A.toString().getBytes());
            sha.update(M.toString().getBytes());
            sha.update(K.toString().getBytes());
            R = new BigInteger(1, sha.digest());
            System.out.println("Server 'R': " + R);
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }



    void SecurityField(){

        boolean bool;

        do{
            BigInteger q = RandomGenerate(63);
            N = BigInteger.TWO.multiply(q).add(BigInteger.ONE);
            bool = N.isProbablePrime((int) Math.log(N.longValue()));
        }while (!bool);

        g = BigInteger.valueOf(3);
        //g = new BigInteger(8, random);

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            sha.update(N.toString().getBytes());
            sha.update(g.toString().getBytes());
            k = new BigInteger(1, sha.digest());
        }catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    private BigInteger RandomGenerate(int bitLength){

        boolean bool;

        BigInteger bigInteger;

        do {

            bigInteger = new BigInteger(bitLength, random);
            bool = bigInteger.isProbablePrime((int) Math.log(bigInteger.longValue()));

        }while(!bool);

        return bigInteger;
    }


}
