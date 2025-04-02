import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWTUtil;
import org.junit.jupiter.api.Test;

public class test {
    @Test
    public void test() throws InterruptedException {
        JSONObject payloads = JWTUtil.parseToken("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE3NDM2MDIwNDQsImlhdCI6MTc0MzYwMjA0NCwiZXhwIjoxNzQzNjA1NjQ0fQ.A93K0fGxQaCvwkRr-zmv3kTNXOF3FqsfQg2ZKbPpOW4").getPayloads();
        System.out.println(payloads);
    }
}
