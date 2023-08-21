package com.bezkoder.springjwt.service.Impl;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.UserRequest;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.service.PreAuthService;
import com.bezkoder.springjwt.service.SmsNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Slf4j
@Service
public class PreAuthServiceImpl implements PreAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    @Qualifier("gmail")
    private JavaMailSender mailSender;

    @Autowired
    private SmsNotification smsNotification;

    @Override
    public void sendMail(String toEmail, String body) {

    }

    @Override
    public ResponseEntity validateUsernameOrEmail(String username) {
        Random r = new Random();
        String token = String.format("%04d", r.nextInt(1001));
        User user = null;
        try {
            if (userRepository.existsByEmail(username)) {
                System.out.println("EMAIL");
                user = userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            } else if (userRepository.existsByUsername(username)) {
                System.out.println("USERNAME");
                user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(400, username + " Does not exist"));
            }
            user.setToken(token);
            userRepository.save(user);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new MessageResponse(HttpServletResponse.SC_SERVICE_UNAVAILABLE, username + " Error Occured "+e.getMessage()));
        }
//        String msgBody ="Dear "+user.getUsername()+"<br><br>Kindly Complete your password reset with this token "+token+".<br><br>If you didnt initiate a reset, kindly ignore this message.<br><br>Regards<br>Fooodbanking Team";
        String msgBody ="Hi "+user.getUsername()+", your FOODBANKING  verification code is "+token;
        String msgSubject="FoodBanking Reset Password Request Verification";
        try{
            smsNotification.sendSMS(user.getPhone(), msgBody);
//            sendEmail(user.getEmail(), msgBody, msgSubject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_CREATED, "Kindly verify your email with the token sent to "+user.getEmail()));
    }

    @Override
    public ResponseEntity validateEmailAndToken(String email, String password, String token) {
        User user = userRepository.findByEmailAndToken(email, token);

        if (null != user) {
            System.out.println("EMAIL "+user);
            user.setPasswordHash(encoder.encode(password));
            user.setToken("null");
            userRepository.save(user);
        } else{
            return ResponseEntity.badRequest().body(new MessageResponse(HttpServletResponse.SC_BAD_REQUEST, "Incorrect token Entered"));
        }
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Password Successfully Changed"));
    }

    @Override
    public ResponseEntity enableAccount(String email, String token) {
        User user = userRepository.findByEmailAndToken(email, token);

        if (null != user) {
            user.setToken("null");
            user.setEnabled(true);
            userRepository.save(user);
        } else{
            return ResponseEntity.badRequest().body(new MessageResponse(HttpServletResponse.SC_BAD_REQUEST, "Incorrect token Entered"));
        }
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Account Successfully Activated"));
    }

    @Override
    public ResponseEntity resendToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + email));
        if(!user.getToken().contains("null")){
//        if (userRepository.existsByEmail(email)) {
//            String msgBody ="Dear "+user.getUsername()+",<br><br>This is your the token to complete your account verification "+user.getToken()+".<br><br>If you didnt initiate a reset, kindly ignore this message.<br><br>Regards<br>Fooodbanking Team";
            String msgBody ="Hi "+user.getUsername()+", your FOODBANKING  verification code is "+user.getToken();
            String msgSubject="FoodBanking Resend Token";
            try{
                smsNotification.sendSMS(user.getPhone(), msgBody);
//                sendEmail(user.getEmail(), msgBody, msgSubject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else{
            return ResponseEntity.badRequest().body(new MessageResponse(403, "You have not initiated a password reset on this account"));
        }
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Token sent to "+ email));
    }


    @Override
    public ResponseEntity signUp(UserRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(400, "Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(400, "Error: Email is already in use!"));
        }

        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(400, "Error: Phone number is already in use!"));
        }
        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPasswordHash(encoder.encode(signUpRequest.getPassword()));
        user.setPhone(signUpRequest.getPhone());
        user.setUsername(signUpRequest.getUsername());
        user.setProfilePicSource("");
        user.setSubscriptionTenor(0 );

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }
        Random r = new Random();
        String token = String.format("%04d", r.nextInt(1001));
        user.setRoles(roles);
        user.setToken(token);
        userRepository.save(user);
//        String msgBody ="Dear "+user.getUsername()+",<br><br>Welcome onboard to foodbanking, Kindly Verify your account with this token <b>"+token+"</b>.";
        String msgBody ="Hi "+user.getUsername()+", your FOODBANKING  verification code is "+user.getToken();
        String msgSubject="FoodBanking Registration Verification";
        try{
            smsNotification.sendSMS(user.getPhone(), msgBody);
//            sendEmail(user.getEmail(), msgBody, msgSubject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(new MessageResponse(200, "Registration Successful!"));
    }


    public void sendEmail(String email, String msgBody, String subject) {
        String body="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body style=\"margin:0;padding:0;\">\n" +
                "    <div style=\"background:#fff\">\n" +
                "        <div style=\"max-width:100%;margin:0px auto;\">\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%; background-color:#fff;\">\n" +
                "                <tbody>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"max-width:100%;box-sizing:border-box; background:#999191\">\n" +
                "                                <div style=\"width:100%;max-width:575px;min-width:300px;margin:auto;text-align:center;padding:15px\">\n" +
                "                                    <img src=\"https://foodbanking.ng/wp-content/uploads/2020/10/food-banking-logo.png\" style=\"width: 40%;\">\n" +
                "                                </div>\n" +
                "                                <div style=\"width:100%; max-width:575px; min-width:300px; background:#fff; margin:auto; box-sizing:border-box; border-radius:4px; border-bottom-left-radius:0; border-bottom-right-radius:0; padding:50px 30px 10px;\">\n" +
                "                                    <h1 style=\"box-sizing:border-box;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji','Segoe UI Symbol';color:#3d4852;font-size:18px;font-weight:bold;margin-top:0;text-align:left\">\n" +
                "                                       Food Banking \n" +
                "                                    </h1><hr>\n" +
                "                                    <b style=\"text-align:center; font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji','Segoe UI Symbol';font-size:16px;line-height:1.5em;margin-top:0;text-align:left\">\n" +subject+
                "                                    </b><hr>\n" +
                "                                    <p style=\"box-sizing:border-box;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji','Segoe UI Symbol';font-size:16px;line-height:1.5em;margin-top:0;text-align:left\">\n" + msgBody+
                "                                     </p>             \n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"background-color: black;width:100%;max-width:575px;min-width:300px;margin-left:auto;margin-right:auto; box-sizing:border-box;border-radius:4px;border-top-left-radius:0;border-top-right-radius:0;padding:10px 30px 50px; box-shadow: 5px 5px 5px #dadada;\">                          \n" +
                "\n"+"                           <p style=\"color: white; box-sizing:border-box;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji','Segoe UI Symbol';font-size:16px;line-height:1.5em;margin-top:0;text-align:left\">\n" +
                "                                    Regards<br>\n" +
                "\t\t\t\t\t\t\t\t\tFoodbanking Team\n" +
                "                                </p>\n" +
                "                                <img width=\"180\" height=\"70\" src=\"https://foodbanking.ng/wp-content/uploads/2020/10/Google-play_img.png\"/>\n" +
                "                                <img width=\"180\" height=\"70\" src=\"https://foodbanking.ng/wp-content/uploads/2020/10/App-store.png\"/>\n" +
                "                                <!-- <img width=\"589\" height=\"585\" src=\"https://foodbanking.ng/wp-content/uploads/2020/10/food-banking-dashboard-and-signup-1.png\"/> -->\n" +
                "                            </div>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +"\n" +
                "        <div style=\"max-width:100%;margin:0px auto;\">\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\">\n" +
                "                <tbody>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"width:100%;max-width:575px;min-width:300px;margin:auto;box-sizing:border-box;padding-top:20px;padding-bottom:20px;padding-left:15px;padding-right:15px;\">\n" +
                "                                <p style=\"text-align:center; font-family:verdana;\">\n" +
                "                                    <a href=\"#\" style=\"text-align:center;font-size:13px;line-height:1.5;color:#999999; text-decoration: none; color: cornflowerblue;     display: flex; align-items: center; justify-content: center;\">\n" +
                "                                        <img src=\"https://foodbanking.ng/wp-content/uploads/2020/10/food-banking-logo.png\" style=\"height:20px; padding-right: 5px;\"> © 2021 Haymouz</a>\n" +
                "                                </p>\n" +
                "                            </div>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";

        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(email);
            message.setFrom("amoz4christ@gmail.com", "Food Banking");
            message.setSubject(subject);
            message.setText(body, true);
        };
        mailSender.send(preparator);
        System.out.println("Email sent successfully To with Subject");
    }


    @Override
    public void emailNotification(String email, String msgBody, String subject) {
        String body="<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body style=\"margin:0;padding:0;\">\n" +
                "    <div style=\"background:#fff\">\n" +
                "        <div style=\"max-width:100%;margin:0px auto;\">\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%; background-color:#fff;\">\n" +
                "                <tbody>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"max-width:100%;box-sizing:border-box; background:#999191\">\n" +
                "                                <div style=\"width:100%;max-width:575px;min-width:300px;margin:auto;text-align:center;padding:15px\">\n" +
                "                                    <img src=\"https://foodbanking.ng/wp-content/uploads/2020/10/food-banking-logo.png\" style=\"width: 40%;\">\n" +
                "                                </div>\n" +
                "                                <div style=\"width:100%; max-width:575px; min-width:300px; background:#fff; margin:auto; box-sizing:border-box; border-radius:4px; border-bottom-left-radius:0; border-bottom-right-radius:0; padding:50px 30px 10px;\">\n" +
                "                                    <h1 style=\"box-sizing:border-box;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji','Segoe UI Symbol';color:#3d4852;font-size:18px;font-weight:bold;margin-top:0;text-align:left\">\n" +
                "                                       Food Banking \n" +
                "                                    </h1><hr>\n" +
                "                                    <b style=\"text-align:center; font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji','Segoe UI Symbol';font-size:16px;line-height:1.5em;margin-top:0;text-align:left\">\n" +subject+
                "                                    </b><hr>\n" +
                "                                    <p style=\"box-sizing:border-box;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji','Segoe UI Symbol';font-size:16px;line-height:1.5em;margin-top:0;text-align:left\">\n" + msgBody+
                "                                     </p>             \n" +
                "                                </div>\n" +
                "                            </div>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"background-color: black;width:100%;max-width:575px;min-width:300px;margin-left:auto;margin-right:auto; box-sizing:border-box;border-radius:4px;border-top-left-radius:0;border-top-right-radius:0;padding:10px 30px 50px; box-shadow: 5px 5px 5px #dadada;\">                          \n" +
                "\n"+"                           <p style=\"color: white; box-sizing:border-box;font-family:-apple-system,BlinkMacSystemFont,'Segoe UI',Roboto,Helvetica,Arial,sans-serif,'Apple Color Emoji','Segoe UI Emoji','Segoe UI Symbol';font-size:16px;line-height:1.5em;margin-top:0;text-align:left\">\n" +
                "                                    @lang('Thanks,')<br>\n" +
                "\t\t\t\t\t\t\t\t\tFoodbanking Team\n" +
                "                                </p>\n" +
                "                                <img width=\"180\" height=\"70\" src=\"https://foodbanking.ng/wp-content/uploads/2020/10/Google-play_img.png\"/>\n" +
                "                                <img width=\"180\" height=\"70\" src=\"https://foodbanking.ng/wp-content/uploads/2020/10/App-store.png\"/>\n" +
                "                                <!-- <img width=\"589\" height=\"585\" src=\"https://foodbanking.ng/wp-content/uploads/2020/10/food-banking-dashboard-and-signup-1.png\"/> -->\n" +
                "                            </div>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +"\n" +
                "        <div style=\"max-width:100%;margin:0px auto;\">\n" +
                "            <table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:100%\">\n" +
                "                <tbody>\n" +
                "                    <tr>\n" +
                "                        <td>\n" +
                "                            <div style=\"width:100%;max-width:575px;min-width:300px;margin:auto;box-sizing:border-box;padding-top:20px;padding-bottom:20px;padding-left:15px;padding-right:15px;\">\n" +
                "                                <p style=\"text-align:center; font-family:verdana;\">\n" +
                "                                    <a href=\"#\" style=\"text-align:center;font-size:13px;line-height:1.5;color:#999999; text-decoration: none; color: cornflowerblue;     display: flex; align-items: center; justify-content: center;\">\n" +
                "                                        <img src=\"https://foodbanking.ng/wp-content/uploads/2020/10/food-banking-logo.png\" style=\"height:20px; padding-right: 5px;\"> © 2021 Haymouz</a>\n" +
                "                                </p>\n" +
                "                            </div>\n" +
                "                        </td>\n" +
                "                    </tr>\n" +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo("amosokomayin@gmail.com");
            message.setFrom("amoz4christ@gmail.com", "Food Banking");
            message.setSubject(subject);
            message.setText(body, true);
        };
        mailSender.send(preparator);
        System.out.println("Email sent successfully To with Subject");

    }

}