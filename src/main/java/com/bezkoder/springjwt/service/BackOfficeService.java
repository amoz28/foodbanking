package com.bezkoder.springjwt.service;

import com.bezkoder.springjwt.payload.request.*;
import com.bezkoder.springjwt.payload.response.UserDetailsResponse;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;


public interface BackOfficeService {

    ResponseEntity getAppSettings();

    ResponseEntity updateSettings(SettingsRequest settingsRequest);

    ResponseEntity adminUpdateUserDetails(UserDetailsResponse userDetailsResponse);

    ResponseEntity bulkWithdraw(List<WithdrawalRequest> withdrawalRequest);

    ResponseEntity getNextWithdrawalList(LocalDate startDate, LocalDate endDtae);

    ResponseEntity createPackage(CreatePackageRequest createPackageRequest);

    ResponseEntity updatePackage(CreatePackageRequest updatePackageRequest);

    ResponseEntity deletePackage(DeletePackageRequest updatePackageRequest);

    ResponseEntity sendMessageToUsers(SendMessageRequest sendMessageRequest);

    ResponseEntity deleteMessageToUsers(SendMessageRequest sendMessageRequest);

    ResponseEntity createItem(ItemsRequest itemsRequest);

    ResponseEntity removeItemFromPackage(ItemsRequest itemsRequest);

    ResponseEntity deleteItem(ItemsRequest itemsRequest);

    ResponseEntity addItemToPackage(ItemsRequest itemsRequest);

    ResponseEntity updateItem(ItemsRequest itemsRequest);
}
