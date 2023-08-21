package com.bezkoder.springjwt.service.Impl;

import com.bezkoder.springjwt.models.*;
import com.bezkoder.springjwt.payload.request.*;
import com.bezkoder.springjwt.payload.response.*;
import com.bezkoder.springjwt.repository.*;
import com.bezkoder.springjwt.service.BackOfficeService;
import com.bezkoder.springjwt.service.SmsNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BackOfficeServiceImpl implements BackOfficeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private WithdrawalRepository withdrawalRepository;

    @Autowired
    private SettingsRepository settingsRepository;

    @Autowired
    private PreAuthServiceImpl preAuthService;

    @Autowired
    private ChoosePackageServiceImpl choosePackageService;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PackageItemsRepository packageItemsRepository;

    @Autowired
    private SmsNotification smsNotification;

    @Override
    public ResponseEntity getAppSettings(){
        Settings settings =  settingsRepository.getApplicationSetting();
        log.info("Setting here "+settings);
        return ResponseEntity.ok(new SettingsResponse(HttpServletResponse.SC_ACCEPTED, settings));
    }

    public Settings getSettings(){
        return settingsRepository.getApplicationSetting();
    }
    @Override
    public ResponseEntity updateSettings(SettingsRequest settingsRequest) {
        User user = userRepository.getUserDetailsByUsername(settingsRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + settingsRequest.getUsername()));
        Settings appSettings = settingsRepository.getApplicationSetting();
        appSettings.setCreditConversion(settingsRequest.getCreditConversion());
        appSettings.setWithdrawals(settingsRequest.getWithdrawals());
        appSettings.setUpdatedAt(LocalDate.now());
        appSettings.setUpdatedBy(settingsRequest.getUsername());
        settingsRepository.save(appSettings);
        choosePackageService.saveHistory("Settings was updated by: {}"+settingsRequest.getUsername(), user.getId(), "");

        return ResponseEntity.ok(new SettingsResponse(HttpServletResponse.SC_ACCEPTED, appSettings));
    }

    @Override
    public ResponseEntity adminUpdateUserDetails(UserDetailsResponse userDetailsResponse) {
        User user = userRepository.getUserDetailsByUsername(userDetailsResponse.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + userDetailsResponse.getUsername()));
        user.setAddress(userDetailsResponse.getAddress());
        user.setCity(userDetailsResponse.getCity());
        user.setState(userDetailsResponse.getState());
        user.setSubscriptionTenor(2);
        user.setFamilySum(userDetailsResponse.getFamily_sum());
        user.setSex(userDetailsResponse.getSex());
        user.setLastname(userDetailsResponse.getLastname());
        user.setFirstname(userDetailsResponse.getFirstname());
        user.setNextDeliveryDate(userDetailsResponse.getNextDeliveryDate());
        userRepository.save(user);
        choosePackageService.saveHistory("User Id Updated Successfully: {}", user.getId(), "");
        return ResponseEntity.ok(new GeneralResponse(HttpServletResponse.SC_ACCEPTED, user));
    }


    @Override
    public ResponseEntity bulkWithdraw(List<WithdrawalRequest> withdrawalRequest) {
        ArrayList<String> errorList = new ArrayList<>();
        ArrayList<String> successfulList = new ArrayList<>();
        try {
            withdrawalRequest.forEach((withdraw)-> {
                long timestamp = new Timestamp(System.currentTimeMillis()).getTime();
                User user = userRepository.findByUsername(withdraw.getUsername())
                        .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
            //  Check if user is subscribed to a package
                checkProfile(user, errorList, withdraw, getSettings().getWithdrawals());

               if(errorList.isEmpty()){
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
                    Long remainingBalance = (user.getSubscriptionTenor() * packagePrice.getPackage_price() - user.getCreditBalance()) / getSettings().getCreditConversion();

                    //  Auto subscribe
                    if (user.getWithdrawalLeft() <= 1 && user.getCreditBalance() >= (user.getSubscriptionTenor() * packagePrice.getPackage_price())) {
                        user.setWithdrawalLeft(user.getWithdrawalLeft() + user.getSubscriptionTenor());
                    }
                    else if(user.getWithdrawalLeft() <= 1 && user.getCreditBalance() < (user.getSubscriptionTenor() * packagePrice.getPackage_price())) {
                        errorList.add(user.getUsername() + ": Insufficient Fund for Auto-renewal");

                        log.info("Max withdrawal");
//                        String msgBody = "Dear " + user.getUsername() + ",<br><br> Your credit balance is low kindly recharge a minumum of " + remainingBalance
//                                + " Naira to enable continuous withdrawal.<br><br>Regards<br>Fooodbanking Team";
                        String msgBody ="Hi "+user.getUsername()+", your credit balance is low kindly recharge a minumum of "+remainingBalance
                                +" Naira to enable continuous withdrawal. Regards Fooodbanking Team";
                        String msgSubject = "FoodBanking Auto Renewal Failed";
                        try {
                            smsNotification.sendSMS(user.getPhone(), msgBody);
//                            preAuthService.sendEmail(user.getEmail(), msgBody, msgSubject);
                        } catch (Exception e) {
                            errorList.add(user.getUsername() + ": Error Occured "+e.getMessage());
                            e.printStackTrace();
                            return;
                        }
                    } else {

                        //  Ensure customer has enough fund
                        if (newBalance < packagePrice.getPackage_price()) {
                            errorList.add(user.getUsername() + ": Insufficient Funds, Kindly Fund your wallet with a minimum of " + remainingBalance);
                            return;
                        } else {
                            user.setNextDeliveryDate(user.getNextDeliveryDate().plusMonths(1));
                            log.info("  ==> " + user.getNextDeliveryDate().plusMonths(1) + " KL " + user.getNextDeliveryDate());
                            user.setNoOfMonthlyWithdrawal(user.getNoOfMonthlyWithdrawal() + 1);
                            user.setCreditBalance(newBalance);
                            user.setWithdrawalLeft(user.getWithdrawalLeft() - 1);
                            try{
                                userRepository.save(user);
                                Withdrawal withdrawalSave = withdrawalRepository.save(withdrawal);
                                successfulList.add(user.getUsername() + ": Withdrawal Successful");
                                choosePackageService.saveHistory("Package Withdrawal: " + packagePrice.getPackage_name(), user.getId(), packagePrice.getPackage_price().toString());
                            }catch(Exception e ){
                                errorList.add(user.getUsername() + ": Error Occured " + e.getMessage());
                            }
                        }
                    }
                }else{
                   log.info("User's Profile not updated");
               }
            });
        }catch(Exception e ){
            errorList.add(": Error Occured " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.ok(new WithdrawalResponse(HttpServletResponse.SC_UNAUTHORIZED, "Error Occured, check logs", errorList, successfulList));
        }
        return ResponseEntity.ok(new WithdrawalResponse(HttpServletResponse.SC_ACCEPTED, "Bulk Withdrawal Completed", errorList, successfulList));
    }

    @Override
    public ResponseEntity getNextWithdrawalList(LocalDate startDate, LocalDate endDtae) {
        List<User> user = userRepository.findAllByNextDeliveryDateBetween(startDate, endDtae);
        return ResponseEntity.ok(new UserListResponse(HttpServletResponse.SC_ACCEPTED, user));
    }

    @Override
    public ResponseEntity createPackage(CreatePackageRequest createPackageRequest) {
        User user = userRepository.findByUsername(createPackageRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
        Packages packages = new Packages();
        packages.setPackage_image(createPackageRequest.getPackage_image());
        packages.setPackage_price(createPackageRequest.getPackage_price());
        packages.setPackage_name(createPackageRequest.getPackage_name());
        packages.setTenor(createPackageRequest.getTenor());
        packages.setCreated_by(String.valueOf(user.getId()));
        packages.setUpdated_by(Math.toIntExact(user.getId()));
        packages.setUpdated_at(LocalDate.now());
        packages.setCreated_at(LocalDate.now());
        log.info("WE ARE package "+packages);
        packageRepository.save(packages);
        log.info("WE ARE HERE");
        List<Packages> allPackages = packageRepository.getAllPackages();
        log.info("WE ARE HERE 1 "+allPackages);

        choosePackageService.saveHistory("Package: "+packages.getPackage_name()+", Created  by: "+user.getUsername(), user.getId(), "");
        log.info("WE ARE HERE 2");
        return ResponseEntity.ok(new PackageResponse(HttpServletResponse.SC_ACCEPTED, allPackages));
    }

    @Override
    public ResponseEntity updatePackage(CreatePackageRequest updatePackageRequest) {
        User user = userRepository.findByUsername(updatePackageRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
        Packages packages = packageRepository.findById(updatePackageRequest.getPackageId()).orElseThrow(()-> new UsernameNotFoundException("Package doesn't Exist"));
        packages.setPackage_image(updatePackageRequest.getPackage_image());
        packages.setPackage_price(updatePackageRequest.getPackage_price());
        packages.setPackage_name(updatePackageRequest.getPackage_name());
        packages.setTenor(updatePackageRequest.getTenor());
        packages.setUpdated_by(Math.toIntExact(user.getId()));
        packages.setUpdated_at(LocalDate.now());
        packageRepository.save(packages);
        List<Packages> allPackages = packageRepository.getAllPackages();
        choosePackageService.saveHistory("Package: "+packages.getPackage_name()+", Updated  by: "+user.getUsername(), user.getId(), "");
        return ResponseEntity.ok(new PackageResponse(HttpServletResponse.SC_ACCEPTED, allPackages));
    }

    @Override
    public ResponseEntity deletePackage(DeletePackageRequest deletePackageRequest) {
        User user = userRepository.findByUsername(deletePackageRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
        packageRepository.deleteById(deletePackageRequest.getPackageId());
        List<Packages> allPackages = packageRepository.getAllPackages();
        choosePackageService.saveHistory("Package: "+deletePackageRequest.getPackageId()+", Deleted by: "+user.getUsername(), user.getId(), "");
        return ResponseEntity.ok(new PackageResponse(HttpServletResponse.SC_ACCEPTED, allPackages));
    }

    @Override
    public ResponseEntity sendMessageToUsers(SendMessageRequest sendMessageRequest) {
        User user = userRepository.findByUsername(sendMessageRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
        Message message = new Message();
        message.setSubject(sendMessageRequest.getSubject());
        message.setBody(sendMessageRequest.getBody());
        message.setCreated_by(user.getId());
        message.setCreated_at(LocalDateTime.now());
        message.setRecipient(sendMessageRequest.getRecipient());
       Message message1 =  messageRepository.save(message);
        choosePackageService.saveHistory("Message: "+message1.getId()+", was Sent by: "+user.getUsername()+" to "+sendMessageRequest.getRecipient(), user.getId(), "");
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_CREATED, "Message Sent Successfuly, MessageId: "+message1.getId()));
    }

    @Override
    public ResponseEntity deleteMessageToUsers(SendMessageRequest sendMessageRequest) {
        User user = userRepository.findByUsername(sendMessageRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
        messageRepository.deleteById(sendMessageRequest.getId());
        choosePackageService.saveHistory("Message "+sendMessageRequest.getId()+" was Deleted by: "+user.getUsername(), user.getId(), "");
        return ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "Message Deleted Successfully, MessageId: "+sendMessageRequest.getId()));
    }

    @Override
    public ResponseEntity createItem(ItemsRequest itemsRequest) {
        User user = userRepository.findByUsername(itemsRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
        Items items = new Items();
        items.setItem_image(itemsRequest.getItem_image());
        items.setCredit_price(itemsRequest.getCredit_price());
        items.setName(itemsRequest.getName());
        items.setCreated_by(Math.toIntExact(user.getId()));
        items.setUpdated_by(Math.toIntExact(user.getId()));
        items.setUpdated_at(LocalDate.now());
        items.setCreated_at(LocalDate.now());
        itemRepository.save(items);
        List<Items> getAllItems = itemRepository.findAll();
        choosePackageService.saveHistory("Item: "+itemsRequest.getId()+" "+itemsRequest.getName()+ ", was Created by: "+user.getUsername(), user.getId(), "");
        log.info("All Items "+ getAllItems);
        return ResponseEntity.ok(new ItemsResponse(HttpServletResponse.SC_ACCEPTED, getAllItems));
    }

    @Override
    public ResponseEntity updateItem(ItemsRequest itemsRequest) {
        User user = userRepository.findByUsername(itemsRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
        Items items = itemRepository.findById(itemsRequest.getId()).orElseThrow(()-> new UsernameNotFoundException("Item doesn't Exist"));

        items.setItem_image(itemsRequest.getItem_image());
        items.setCredit_price(itemsRequest.getCredit_price());
        items.setName(itemsRequest.getName());
        items.setCreated_by(Math.toIntExact(user.getId()));
        items.setUpdated_by(Math.toIntExact(user.getId()));
        items.setUpdated_at(LocalDate.now());
        itemRepository.save(items);
        List<Items> getAllItems = itemRepository.findAll();
        choosePackageService.saveHistory("Item: "+itemsRequest.getId()+", was Updated by: "+user.getUsername(), user.getId(), "");
        log.info("All Items "+ getAllItems);
        return ResponseEntity.ok(new ItemsResponse(HttpServletResponse.SC_ACCEPTED, getAllItems));
    }

    @Override
    public ResponseEntity addItemToPackage(ItemsRequest itemsRequest) {
        User user = userRepository.findByUsername(itemsRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
        int foundItems = packageItemsRepository.checkIfItemExist(itemsRequest.getId(), itemsRequest.getPackageId());
        if (foundItems>0)
            throw new IllegalArgumentException("Item Already Exists in this package");
        PackageItems packageItems = new PackageItems();
        packageItems.setItemId(itemsRequest.getId());
        packageItems.setPackageId(itemsRequest.getPackageId());
        packageItems.setAssignedBy(itemsRequest.getUsername());
        packageItems.setAssignedDate(LocalDate.now());
        packageItemsRepository.save(packageItems);
        List<Items> items = itemRepository.getItemsByPackage(itemsRequest.getPackageId());
        if(items.isEmpty()){
            return  ResponseEntity.ok(new MessageResponse(HttpServletResponse.SC_ACCEPTED, "No Item Assigned to this Package"));
        }
        choosePackageService.saveHistory("Item: "+itemsRequest.getId()+", was Added to package: "+itemsRequest.getPackageId()+" by: "+user.getUsername(), user.getId(), "");

        return ResponseEntity.ok(new ItemsResponse(HttpServletResponse.SC_ACCEPTED, items));
    }

    @Override
    public ResponseEntity deleteItem(ItemsRequest itemsRequest) {
        User user = userRepository.findByUsername(itemsRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
        itemRepository.deleteById(itemsRequest.getId());
        packageItemsRepository.deleteByItemId(itemsRequest.getId()).orElseThrow(()-> new UsernameNotFoundException("Item doesn't Exist"));
        List<Items> getAllItems =  itemRepository.findAll();
        choosePackageService.saveHistory("Item: "+itemsRequest.getId()+", was Deleted by: "+user.getUsername(), user.getId(), "");
        return ResponseEntity.ok(new ItemsResponse(HttpServletResponse.SC_ACCEPTED, getAllItems));
    }

    @Override
    public ResponseEntity removeItemFromPackage(ItemsRequest itemsRequest) {
        User user = userRepository.findByUsername(itemsRequest.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Doesnt Exist "));
        packageItemsRepository.deleteByItemIdAndPackageId(itemsRequest.getId(), itemsRequest.getPackageId());
        List<Items> getAllItems = itemRepository.getItemsByPackage(itemsRequest.getPackageId());
        choosePackageService.saveHistory("Item: "+itemsRequest.getId()+", was Removed by: "+user.getUsername(), user.getId(), "");
        return ResponseEntity.ok(new ItemsResponse(HttpServletResponse.SC_ACCEPTED, getAllItems));
    }

    public static void checkProfile(User user, ArrayList<String> errorList, WithdrawalRequest withdrawalRequest, long monthlyWithdrawal){
        if (user.getFirstname() == null || user.getFirstname().isEmpty()) {
            errorList.add(user.getUsername() + ": Profile Not Updated: fistName");
        } else if (user.getLastname() == null || user.getLastname().isEmpty()) {
            errorList.add(user.getUsername() + ": Profile Not Updated: lastName");
        } else if (user.getPhone() == null || user.getPhone().isEmpty()) {
            errorList.add(user.getUsername() + ": Profile Not Updated: Phone Number");
        } else if (user.getNextDeliveryDate() == null || user.getNextDeliveryDate().toString().isEmpty()) {
            errorList.add(user.getUsername() + ": Profile Not Updated: Delivery date");
        } else if (user.getAddress() == null || user.getAddress().isEmpty()) {
            errorList.add(user.getUsername() + ": Profile Not Updated: Address");
        } else if (user.getPackageId() == null || user.getPackageId().equals(null)) {
            errorList.add(user.getUsername() + ": User is not subscribed to any package: Package ID");
        } else if (user.getNoOfMonthlyWithdrawal() >= monthlyWithdrawal) {
            errorList.add(user.getUsername() + ": Monthly Withdrawal Exceeded");
        }
        log.info("Error List "+errorList);
    }

}
