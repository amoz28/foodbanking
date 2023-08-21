package com.bezkoder.springjwt.service.Impl;

import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.DepositRequest;
import com.bezkoder.springjwt.payload.request.PackageRequest;
import com.bezkoder.springjwt.payload.request.WithdrawalRequest;
import com.bezkoder.springjwt.payload.response.*;
import com.bezkoder.springjwt.repository.*;
import com.bezkoder.springjwt.service.ChoosePackageService;
import com.bezkoder.springjwt.service.SmsNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChoosePackageServiceImpl implements ChoosePackageService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private DepositRepository depositRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MarkReadMessageRepository markReadMessageRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @Autowired
    private PreAuthServiceImpl preAuthService;

    @Autowired
    private SmsNotification smsNotification;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    public ResponseEntity getAllPackages(String username) {
        User user = userRepository.getUserDetailsByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        List<Packages> packages = packageRepository.getAllPackages();
        return ResponseEntity.ok(new PackageResponse(HttpServletResponse.SC_ACCEPTED, packages));
    }

    @Override
    public ResponseEntity allItems(String username) {
        User user = userRepository.getUserDetailsByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        List<Items> allItems = itemRepository.findAll();
        return ResponseEntity.ok(new ItemsResponse(HttpServletResponse.SC_ACCEPTED, allItems));
    }



    @Override
    public ResponseEntity bulkWithdraw(List<WithdrawalRequest> withdrawalRequest){
        try {
            long timestamp = new Timestamp(System.currentTimeMillis()).getTime();

            if(withdrawalRequest.isEmpty() ){
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_BAD_REQUEST, "No User was selected"));
            }
            withdrawalRequest.forEach((withdrawal)->{
                withdraw(withdrawal);
            });

        }catch(Exception e ){
            return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Error Occured, please try again Later"));
        }
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Withdrawal was successful"));
    }
 
    @Override
    public ResponseEntity withdraw(WithdrawalRequest withdrawalRequest) {
        try {
            long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
            User user = userRepository.findByUsername(withdrawalRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));

//        Check if user is subscribed to a package
            if (user.getFirstname() == null || user.getFirstname().isEmpty()) {
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Kindly Update your profile with your Name, Phone Number and Address and Subscribe to a package"));
            } else if (user.getLastname() == null || user.getLastname().isEmpty()) {
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Kindly Update your profile with your Name, Phone Number and Address and Subscribe to a package"));
            } else if (user.getPhone() == null || user.getPhone().isEmpty()) {
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Kindly Update your profile with your Name, Phone Number and Address and Subscribe to a package"));
            }  else if (user.getAddress() == null || user.getAddress().isEmpty()) {
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Kindly Update your profile with your Name, Phone Number and Address and Subscribe to a package"));
            } else if (user.getNextDeliveryDate() == null || user.getNextDeliveryDate().toString().isEmpty()) {
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Please Set a delivery date"));
            }
            if (user.getPackageId() == null || user.getPackageId()==0) {
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Kindly Subscribe to a package"));
            }
            if (user.getNoOfMonthlyWithdrawal() >= getSettings().getWithdrawals()) {
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "You have Exhausted your Monthly withdrawal limit"));
            }

            Withdrawal withdrawal = new Withdrawal();
            withdrawal.setStatus("Pending");
            withdrawal.setCreated_by(user.getId());
            withdrawal.setUpdated_by(user.getId());
            withdrawal.setCreated_at(timestamp);
            withdrawal.setUpdated_at(timestamp);
            Packages packagePrice = packageRepository.findById(Long.valueOf(user.getPackageId()))
                    .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
            Long newBalance = user.getCreditBalance() - packagePrice.getPackage_price();
            log.info("COME OUT " + packagePrice);
            Long remainingBalance = (user.getSubscriptionTenor() * packagePrice.getPackage_price() - user.getCreditBalance())/getSettings().getCreditConversion();
            //  Auto subscribe
            if (user.getWithdrawalLeft() <= 1 && user.getCreditBalance() >= (user.getSubscriptionTenor() * packagePrice.getPackage_price())){
                user.setWithdrawalLeft(user.getWithdrawalLeft() + user.getSubscriptionTenor());
            }
            else if(user.getWithdrawalLeft() <= 1 && user.getCreditBalance() < (user.getSubscriptionTenor() * packagePrice.getPackage_price())){

                log.info("Max withdrawal");
                String msgBody ="Hi "+user.getUsername()+", your credit balance is low kindly recharge a minumum of "+remainingBalance
                        +" Naira to enable continuous withdrawal. Regards Fooodbanking Team";
//                String msgBody ="Dear "+user.getUsername()+",<br><br> Your credit balance is low kindly recharge a minumum of "+remainingBalance
//                        +" Naira to enable continuous withdrawal.<br><br>Regards<br>Fooodbanking Team";
                String msgSubject="FoodBanking Fund Wallet Notification";
                try{
                    smsNotification.sendSMS(user.getPhone(), msgBody);
//                    preAuthService.sendEmail(user.getEmail(), msgBody, msgSubject);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Insufficient Balance Please fund your wallet with a minimum "+remainingBalance+" to continue"));

                }
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Insufficient Balance Please fund your wallet with a minimum "+remainingBalance+" to continue"));

            }

            //  Ensure customer has enough fund
            if (newBalance < packagePrice.getPackage_price()) {
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_BAD_REQUEST, "Insufficient Funds, Kindly Fund your wallet with a minimum of N"+remainingBalance));
            }
            user.setNextDeliveryDate(user.getNextDeliveryDate().plusMonths(1));
            log.info("  ==> "+user.getNextDeliveryDate().plusMonths(1) +" KL "+user.getNextDeliveryDate());
            user.setNoOfMonthlyWithdrawal(user.getNoOfMonthlyWithdrawal() +1);
            user.setCreditBalance(newBalance);
            user.setWithdrawalLeft(user.getWithdrawalLeft()-1);
            userRepository.save(user);
            Withdrawal withdrawalSave = withdrawalRepository.save(withdrawal);
            saveHistory("Package Withdrawal: " + packagePrice.getPackage_name(), user.getId(), packagePrice.getPackage_price().toString());
        }catch(Exception e ){
            return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Error Occured, please try again Later "+ e.getMessage() ));
        }
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Withdrawal was successful"));
    }

    public Settings getSettings(){
        return settingsRepository.getApplicationSetting();
    }

    @Override
    public ResponseEntity fundWallet(DepositRequest depositRequest) {
        try {
            User user = userRepository.findByUsername(depositRequest.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
            Deposit deposit = new Deposit();
            deposit.setUser_id(user.getId());
            deposit.setType(depositRequest.getType());
            deposit.setDescription(depositRequest.getDescription());
            deposit.setAmount_paid(depositRequest.getAmount_paid());
//        Convert credit
            Double credit = depositRequest.getAmount_paid() * getSettings().getCreditConversion();
            deposit.setCredits_received(credit);
            deposit.setTranId(depositRequest.getTransactionId());
            user.setCreditBalance((long) (null == user.getCreditBalance() ? credit : (user.getCreditBalance() + credit)));
            depositRepository.save(deposit);
            userRepository.save(user);
            saveHistory("Wallet Funded: ", user.getId(), credit.toString());
        }catch (Exception e){
            e.printStackTrace();
             return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_UNAUTHORIZED, "Error Occured: "+e.getMessage()));
        }
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Deposit was successful and your wallet has been credited"));
    }

    @Override
    public ResponseEntity getUserDetails(String username) {
        User user = userRepository.getUserDetailsByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return ResponseEntity.ok(new GeneralResponse(HttpServletResponse.SC_ACCEPTED, user));
    }

    @Override
    public ResponseEntity updateUserDetails(UserDetailsResponse userDetailsResponse) {
        User user = userRepository.getUserDetailsByUsername(userDetailsResponse.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userDetailsResponse.getUsername()));
        user.setAddress(userDetailsResponse.getAddress());
//        user.setAge(userDetailsResponse.getAge());
        user.setCity(userDetailsResponse.getCity());
        user.setState(userDetailsResponse.getState());
        user.setFamilySum(userDetailsResponse.getFamily_sum());
        user.setSex(userDetailsResponse.getSex());
        if(userDetailsResponse.getProfilePicSource() !=null && !userDetailsResponse.getProfilePicSource().trim().isEmpty())
        user.setProfilePicSource(userDetailsResponse.getProfilePicSource());
//        user.setMaritalStatus(userDetailsResponse.getMaritalStatus());
        user.setLastname(userDetailsResponse.getLastname());
        user.setFirstname(userDetailsResponse.getFirstname());
//        user.setNextDeliveryDate(userDetailsResponse.getNextDeliveryDate());
        userRepository.save(user);
        saveHistory("User Id Updated Successfully: {}", user.getId(), "");
        return ResponseEntity.ok(new GeneralResponse(HttpServletResponse.SC_ACCEPTED, user));
    }

    @Override
    public ResponseEntity selectPackage(PackageRequest packageRequest) {
        log.info("Package Sent "+ packageRequest);
        User user = userRepository.getUserDetailsByUsername(packageRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + packageRequest.getUsername()));
        if(packageRequest.getPackageId()==null){
            return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_BAD_REQUEST, "Please Select a Package"));
        }
        Packages packages = packageRepository.findById(Long.valueOf(packageRequest.getPackageId()))
                .orElseThrow(() -> new UsernameNotFoundException("Package not found "));
        Long balance = user.getCreditBalance() - packages.getPackage_price();
        log.info("User's Balance After Deductions "+balance +" pack price: "+packages.getPackage_price()+" Original credit "+user.getCreditBalance());
            if(balance >= 0){
                user.setPackageId(Math.toIntExact(packages.getId()));
                user.setWithdrawalLeft(packages.getTenor());
                user.setSubscriptionTenor(packages.getTenor());
                user.setNextDeliveryDate(LocalDate.parse(packageRequest.getNextDeliveryDate()));
                userRepository.save(user);
                saveHistory(user.getUsername()+" -Subscribed to Package ID: " +packageRequest.getPackageId(), user.getId(), "");
            }
            else{
                return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_BAD_REQUEST, "Insufficient Fund, kindly Credit your wallet with a minimum of "+(-1*balance)));
            }
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Package updated Successfully to: "+packages.getPackage_name()));
    }

    public void saveHistory(String desc, Long userId, String credit){

        UserHistory userHistory = new UserHistory();
        userHistory.setDescription(desc);
        userHistory.setUserId(userId);
        userHistory.setCredit(credit);
        userHistoryRepository.save(userHistory);
    }

    public Date setExpiryDate(int months) {
        Date currentDate = new Date();
        log.info(dateFormat.format(currentDate));
        // convert date to calendar
        Calendar c = Calendar.getInstance();
        c.setTime(currentDate);
        // manipulate date
        c.add(Calendar.YEAR, 1);
        c.add(Calendar.MONTH, months);
        c.add(Calendar.DATE, 1);
        // convert calendar to date
        Date expiryDate = c.getTime();
        log.info(dateFormat.format(expiryDate));
        return expiryDate;
    }

    @Override
    public ResponseEntity changePassword(String username, String password) {
        User user = userRepository.getUserDetailsByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        user.setPasswordHash(encoder.encode(password));
        userRepository.save(user);
        saveHistory(user.getUsername()+" -Changed Password ", user.getId(), "");

        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Password Changed Successfully"));
    }

    @Override
    public ResponseEntity getAllMessages(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        List<Message> messageResp = messageRepository.findAllByRecipient(user.getId());
        if (null == messageResp || messageResp.isEmpty()){
            return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_NOT_FOUND, "Ooops, you dont have Any Message"));
        }
        return ResponseEntity.ok(new UserMessageResponse(HttpServletResponse.SC_ACCEPTED, messageResp));
    }

    @Override
    public ResponseEntity markMessageAsRead(MarkReadMessage markReadMessage) {
        Message message = messageRepository.findById(markReadMessage.getMessage_id())
                .orElseThrow(() -> new UsernameNotFoundException("Messsage not found"));
        MarkReadMessage markReadMessage1 = markReadMessageRepository.messageReadStatus(markReadMessage.getUser_id(), markReadMessage.getMessage_id());
        if(null == markReadMessage1){
            markReadMessageRepository.save(markReadMessage);
            return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Message Read"));
        }
        saveHistory("Read Message with id: " +markReadMessage.getMessage_id(), markReadMessage.getUser_id(), "");

        return  ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Message Already Read Previously"));
    }

    @Override
    public ResponseEntity getTransactions(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userName));

       List<UserHistory> userHistory = userHistoryRepository.findAllByUserId(user.getId());

        if (null == userHistory || userHistory.isEmpty()){
            return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_NOT_FOUND, "Transaction History is Empty"));
        }

        List<UserHistory> relatedHistory = userHistory
                .stream()
                .filter(c -> c.getDescription().contains("Wallet Funded")
                        || c.getDescription().contains("Package Withdrawal")
                        || c.getDescription().contains("Subscribed"))
                .collect(Collectors.toList());

        log.info(user.getId()+" -========= "+userHistory);

        return ResponseEntity.ok(new HistoryResponse(HttpServletResponse.SC_ACCEPTED, relatedHistory));

    }

    @Override
    public ResponseEntity getItemsByPackageId(String username, Long packageId) {
        List<Items> items = itemRepository.getItemsByPackage(packageId);
        if(items.isEmpty()){
            return  ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "No Item Assigned to this Package"));
        }
        return ResponseEntity.ok(new ItemsResponse(HttpServletResponse.SC_ACCEPTED, items));
    }
}