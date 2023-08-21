package com.bezkoder.springjwt.service;


import com.bezkoder.springjwt.models.Items;
import com.bezkoder.springjwt.models.MarkReadMessage;
import com.bezkoder.springjwt.models.Message;
import com.bezkoder.springjwt.payload.request.DepositRequest;
import com.bezkoder.springjwt.payload.request.PackageRequest;
import com.bezkoder.springjwt.payload.request.UserRequest;
import com.bezkoder.springjwt.payload.request.WithdrawalRequest;
import com.bezkoder.springjwt.payload.response.UserDetailsResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ChoosePackageService {

    ResponseEntity getAllPackages(String username);

    ResponseEntity withdraw(WithdrawalRequest withdrawalRequest);

    ResponseEntity bulkWithdraw(List<WithdrawalRequest> withdrawalRequest);

    ResponseEntity allItems(String username);

    ResponseEntity fundWallet(DepositRequest depositRequest);

    ResponseEntity getUserDetails(String email);

    ResponseEntity updateUserDetails(UserDetailsResponse userDetailsResponse);

    ResponseEntity selectPackage(PackageRequest packageRequest);

//    ResponseEntity getAllPackages(PackageRequest packageRequest);

    ResponseEntity changePassword(String username, String password);

    ResponseEntity getAllMessages(String username);

    ResponseEntity markMessageAsRead(MarkReadMessage markReadMessage);

    ResponseEntity getTransactions(String userName);

    ResponseEntity getItemsByPackageId(String username, Long packageId);
}
