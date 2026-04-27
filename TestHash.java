import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestHash {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println(encoder.matches("123456", "$2a$10$S2hcjCFJjRQaHF7Bw/XN3esUkhEJthUtVRirYvV3e0iBnt3JemJeu"));
        System.out.println(encoder.encode("123456"));
    }
}
