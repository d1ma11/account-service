//package ru.mts.account.security;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.function.Function;
//
//@Service
//public class JwtService {
//
//    private final String SECRET_KEY;
//
//    public JwtService(
//            @Value("${spring.security.secret.key}") String secretKey
//    ) {
//        this.SECRET_KEY = secretKey;
//    }
//
//    /**
//     * Извлекает номер телефона пользователя из данного токена JWT
//     *
//     * @param token - токен JWT, из которого можно извлечь номер телефона пользователя
//     * @return извлеченный номер телефона из токена
//     */
//    public String extractUserPhone(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    /**
//     * Извлекает утверждение из данного токена JWT, используя предоставленную функцию распознавания утверждений
//     *
//     * @param token          - Токен JWT, из которого извлекается утверждение
//     * @param claimsResolver - Функция, которая определяет тип утверждения для извлечения из токена
//     * @param <T>            Общий тип извлекаемого утверждения
//     * @return значение извлеченного утверждения
//     */
//    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//
//    /**
//     * Проверяет, является ли данный токен JWT действительным для предоставленных данных пользователя
//     *
//     * @param token       - Токен JWT для проверки
//     * @param userDetails - Данные пользователя, по которым можно проверить токен
//     * @return значение True, если токен действителен для данных пользователя, в противном случае значение false
//     */
//    public boolean isTokenValid(String token, UserDetails userDetails) {
//        final String username = extractUserPhone(token);
//        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
//    }
//
//    /**
//     * Проверяет, истек ли срок действия данного токена JWT.
//     *
//     * @param token - токен JWT для проверки.
//     * @return значение True, если срок действия токена истек, и значение false в противном случае.
//     */
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    /**
//     * Извлекает дату истечения срока действия из данного токена JWT.
//     *
//     * @param token - токен JWT, из которого можно извлечь дату истечения срока действия.
//     * @return дату истечения срока действия токена.
//     */
//    private Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//
//    /**
//     * Проводит парсинг всего токена JWT и извлекает все утверждения
//     *
//     * @param token - Токен JWT для синтаксического анализа
//     * @return все утверждения, содержащиеся в токене
//     */
//    private Claims extractAllClaims(String token) {
//        return Jwts
//                .parserBuilder()
//                .setSigningKey(getSignInKey())
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    /**
//     * Генерирует ключ HMAC SHA для подписи JWT
//     *
//     * @return ключ HMAC SHA для подписи JWT
//     */
//    private Key getSignInKey() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        return Keys.hmacShaKeyFor(keyBytes);
//    }
//}
