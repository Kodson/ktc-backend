package com.kodsonApp.resource;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kodsonApp.constant.SecurityConstant;
import com.kodsonApp.domain.Kodson;
import com.kodsonApp.exception.domain.*;
import com.kodsonApp.domain.HttpResponse;
import com.kodsonApp.domain.KodsonPrincipal;
import com.kodsonApp.exception.ExceptionHandling;
import com.kodsonApp.service.KodsonService;
import com.kodsonApp.service.impl.OtpService;
import com.kodsonApp.utility.JWTTokenProvider;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping(path = { "/","api/user"})
public class KodsonResource extends ExceptionHandling {
    private KodsonService kodsonService;
    private OtpService otpService;
    public static final String EMAIL_SENT = "An email with a new password was sent to: ";
    public static final String USER_DELETED_SUCCESSFULLY = "User deleted successfully";
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    private LoadingCache<String, Kodson> restaurantCache;

    private Logger LOGGER = LoggerFactory.getLogger(getClass());



    @Autowired
    public KodsonResource(KodsonService restaurantService, OtpService otpService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.kodsonService = restaurantService;
        this.otpService = otpService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;

        restaurantCache = CacheBuilder.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES) // Set expiration time
                .build(new CacheLoader<String, Kodson>() {
                    @Override
                    public Kodson load(String key) throws Exception {
                        return null; // Placeholder, actual loading logic will be implemented in login method
                    }
                });

    }

    @PostMapping("/login")
    public ResponseEntity<Kodson> login(@RequestBody Kodson restaurant) {
        authenticate(restaurant.getUsername(), restaurant.getPassword());
        //Restaurant loginUser = restaurantService.findUserByUsername(restaurant.getUsername());
        restaurantCache.put("loginU", restaurant);
        //RestaurantPrincipal restaurantPrincipal = new RestaurantPrincipal(loginUser);
        //HttpHeaders jwtHeader = getJwtHeader(restaurantPrincipal);
        return new ResponseEntity(OK);  //loginUser, jwtHeader,
    }

    @PostMapping("/signUp")
    public ResponseEntity<Kodson> register(@RequestBody Kodson kodson) throws UserNotFoundException, UsernameExistException, EmailExistException, MessagingException, javax.mail.MessagingException, IOException {
        Kodson newUser = kodsonService.register(kodson.getPhone(),
                kodson.getUsername(), kodson.getEmail(),kodson.getPassword());

        if (newUser.isMfaEnabled()) {
            return ResponseEntity.ok(newUser);
        }
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Kodson> addNewUser(
                                             @RequestParam("username") String username,
                                             @RequestParam("email") String email,
                                             @RequestParam("role") String role,
                                             @RequestParam("isActive") String isActive,
                                             @RequestParam("isNonLocked") String isNonLocked,
                                             @RequestParam("phone") String phone,
                                             @RequestParam("password") String password,
                                             @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        Kodson newUser = kodsonService.addNewUser(password,phone, username,email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
        return new ResponseEntity<>(newUser, OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Kodson> update(@RequestParam("currentUsername") String currentUsername,
                                         @RequestParam("userBranch") String branch,
                                         @RequestParam("username") String username,
                                         @RequestParam("email") String email,
                                         @RequestParam("role") String role,
                                         @RequestParam("isActive") String isActive,
                                         @RequestParam("isNonLocked") String isNonLocked,
                                         @RequestParam("phone") String phone,
                                         @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        Kodson updatedUser = kodsonService.updateUser(phone,currentUsername,  branch, username,email, role, Boolean.parseBoolean(isNonLocked), Boolean.parseBoolean(isActive), profileImage);
        return new ResponseEntity<>(updatedUser, OK);
    }

    @PostMapping("/linkSender")
    public ResponseEntity<HttpResponse> linkSender(@Valid @RequestBody Kodson restaurant) throws EmailNotFoundException {
        kodsonService.sendLink(restaurant.getEmail());
        restaurantCache.put("linkMail", restaurant);
        return new ResponseEntity<>(OK);
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody Kodson restaurant)
            throws MessagingException, EmailNotFoundException, javax.mail.MessagingException, IOException, ExecutionException {

        // Check if email and password are present
        if (restaurant.getEmail() == null || restaurant.getPassword() == null) {
            return new ResponseEntity<>("Email or password is missing", HttpStatus.BAD_REQUEST);
        }

        // Perform password reset logic
        kodsonService.resetPassword(restaurant.getEmail(), restaurant.getPassword());

        // For debugging purposes, you can log the email and password
        System.out.println("Email: " + restaurant.getEmail() + ", Password: " + restaurant.getPassword());

        // Return success response
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/find/{username}")
    public ResponseEntity<Kodson> getUser(@PathVariable("username") String username) {
        Kodson restaurant = kodsonService.findUserByUsername(username);
        return new ResponseEntity<>(restaurant, OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Kodson>> getAllUsers() {
        List<Kodson> users = kodsonService.getUsers();
        return new ResponseEntity<>(users, OK);
    }



    @DeleteMapping("/delete/{username}")
    @PreAuthorize("hasAnyAuthority('user:delete')")
    public ResponseEntity<HttpResponse> deleteUser(@PathVariable("username") String username) throws IOException {
        kodsonService.deleteUser(username);
        return response(OK, USER_DELETED_SUCCESSFULLY);
    }

    @PostMapping("/updateProfileImage")
    public ResponseEntity<Kodson> updateProfileImage(@RequestParam("username") String username, @RequestParam(value = "profileImage") MultipartFile profileImage) throws UserNotFoundException, UsernameExistException, EmailExistException, IOException, NotAnImageFileException {
        Kodson user = kodsonService.updateProfileImage(username, profileImage);
        return new ResponseEntity<>(user, OK);
    }

    @GetMapping(path = "/image/{username}/{fileName}", produces = IMAGE_JPEG_VALUE)
    public byte[] getProfileImage(@PathVariable("username") String username, @PathVariable("fileName") String fileName) throws IOException {
        return Files.readAllBytes(Paths.get(System.getProperty("user.home") + "/api/user/" + username + "/" + fileName));
    }

    @GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
    public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
        URL url = new URL("https://robohash.org/" + username);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (InputStream inputStream = url.openStream()) {
            int bytesRead;
            byte[] chunk = new byte[1024];
            while((bytesRead = inputStream.read(chunk)) > 0) {
                byteArrayOutputStream.write(chunk, 0, bytesRead);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }
    /*@GetMapping
    public ResponseEntity<HttpResponse> confirmUserAccount(@RequestParam("token") String token) {
        Boolean isSuccess = kodsonService.verifyToken(token);
        ResponseEntity<HttpResponse> body = ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp1(LocalDateTime.now().toString())
                        .data(Map.of("Success", isSuccess))
                        .message("Account Verified")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
        return body;
    }*/

    @GetMapping("/{token}")
    public ResponseEntity<HttpResponse> confirmUserAccount(@PathVariable("token") String token) {
        Boolean isSuccess = kodsonService.verifyToken(token);

        HttpStatus status = isSuccess ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
        String message = isSuccess ? "Account Verified" : "Invalid or expired token";

        HttpResponse response = HttpResponse.builder()
                .timeStamp1(LocalDateTime.now().toString())
                .data(Map.of("Success", isSuccess))
                .message(message)
                .status(status)
                .statusCode(status.value())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping(value = "verify")
    public ResponseEntity<Kodson> verifyOtp(@Valid @RequestBody Kodson verifyTokenRequest) throws ExecutionException {
        Kodson restaurantUser = restaurantCache.get("loginU");
        String username = restaurantUser.getUsername();
        Integer otp = verifyTokenRequest.getOtp();
        boolean isOtpValid = otpService.validateOTP(username, otp);
        if (!isOtpValid) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        LOGGER.info("otp verified for user: "+username);
        Kodson loginUser = kodsonService.findUserByUsername(username);
        KodsonPrincipal restaurantPrincipal = new KodsonPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(restaurantPrincipal);
        kodsonService.verifyOtp(username);
        restaurantCache.invalidate("loginU");
        return new ResponseEntity<>(loginUser,jwtHeader,HttpStatus.OK);
    }

    @PostMapping("/logOut")
    public ResponseEntity<HttpResponse> deVerifyOtp(@RequestParam("currentUsername") String currentUsername) {
        kodsonService.deVerifyOtp(currentUsername);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(),
                message), httpStatus);
    }

    private HttpHeaders getJwtHeader(KodsonPrincipal restaurantPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(restaurantPrincipal));
        return headers;
    }

   private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        otpService.generateOtp(username);
    }

    @PostMapping("/findByPhone")
    public ResponseEntity<String> findUserByPhone(@RequestBody Map<String, String> payload) {
        String phone = payload.get("phone");
        Kodson user = kodsonService.findUserByPhone(phone);

        if (user != null) {
            return new ResponseEntity<>("accountMail:" + user.getEmail(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }


}
