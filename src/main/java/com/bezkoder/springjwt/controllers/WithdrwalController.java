package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.models.MarkReadMessage;
import com.bezkoder.springjwt.payload.request.*;
import com.bezkoder.springjwt.payload.response.UserDetailsResponse;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.service.BackOfficeService;
import com.bezkoder.springjwt.service.Impl.ChoosePackageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v2")
public class WithdrwalController {
    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    @Autowired
    private ChoosePackageServiceImpl choosePackageService;

    @Autowired
    private BackOfficeService backOfficeService;

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}

    @GetMapping("/getUserDetails")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity getUserDetails(@Valid @RequestParam("username") String username) {

		return choosePackageService.getUserDetails(username);
	}

    @PostMapping("/changePassword")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity changePassword(@Valid @RequestBody UserRequest userRequest) {

        return choosePackageService.changePassword(userRequest.getUsername(), userRequest.getPassword());
    }

    @PostMapping("/updateUserDetails")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity updateUserDetails(@Valid @RequestBody UserDetailsResponse userDetailsResponse) {

        return choosePackageService.updateUserDetails(userDetailsResponse);
    }

    @PostMapping("/fundWallet")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity fundWallet(@Valid @RequestBody DepositRequest depositRequest) {
        return choosePackageService.fundWallet(depositRequest);
    }

    @PostMapping("/withdrawCredit")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity withdrawCredit(@Valid @RequestBody WithdrawalRequest withdrawalRequest) {
//        Check if user's profile is updated
//        Read Project doc for more
        return choosePackageService.withdraw(withdrawalRequest);
    }

    @GetMapping("/getAllPackages")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity getAllPackages(HttpServletRequest request) {
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwt = req.getHeader("Authorization").split(" ")[1];
        String userName = jwtUtils.getUserNameFromJwtToken(jwt);
//        Check if user's profile is updated
//        Read Project doc for more
        return choosePackageService.getAllPackages(userName);
    }

    @GetMapping("/getAllItems")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity getAllItems(@Valid @RequestParam("username") String username) {
        return choosePackageService.allItems(username);
    }

    @GetMapping("/getItemsByPackagesId")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity getItemsByPackageId(@Valid @RequestParam("username") String username, @Valid @RequestParam("packageId") Long packageId) {
//        Check if user's profile is updated
//        Read Project doc for more
        return choosePackageService.getItemsByPackageId(username, packageId);
    }

    @PostMapping("/selectPackage")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity selectPackage(@Valid @RequestBody PackageRequest packageRequest) {

        return choosePackageService.selectPackage(packageRequest);
    }


    @GetMapping("/getMyMessages")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity allMessage(@Valid @RequestParam("username") String username) {
        return choosePackageService.getAllMessages(username);
    }

    @PostMapping("/markMessageAsRead")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity markMessageAsRead(@Valid @RequestBody MarkReadMessage markReadMessage) {
        return choosePackageService.markMessageAsRead(markReadMessage);
    }

    @GetMapping("/getUserTransactions")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity getUserTransaction(@Valid @RequestParam("username") String username) {
        return choosePackageService.getTransactions(username);
    }

    @PostMapping("/bulkWithdraw")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity bulkWithdraw(@Valid @RequestBody List <WithdrawalRequest> withdrawalRequest) {
//        Check if user's profile is updated
//        Read Project doc for more
        return backOfficeService.bulkWithdraw(withdrawalRequest);
    }

    @GetMapping("/getNextWithdrawalList")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity getNextWithdrawalList(@Valid @RequestParam("startDate") String startDate, @Valid @RequestParam("endDate") String endDate) {
//        Check if user's profile is updated
//        Read Project doc for more
        return backOfficeService.getNextWithdrawalList(LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    @PostMapping("/adminUpdateUserDetails")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity adminUpdateUserDetails(@Valid @RequestBody UserDetailsResponse userDetailsResponse) {

        return backOfficeService.adminUpdateUserDetails(userDetailsResponse);
    }

    @GetMapping("/getSettings")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity getSettings() {
        return backOfficeService.getAppSettings();
    }

    @PostMapping("/updateSettings")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity updateSettings(@Valid @RequestBody SettingsRequest settingsRequest) {
        return backOfficeService.updateSettings(settingsRequest);
    }


    @PostMapping("/createPackage")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity createPackage(@Valid @RequestBody CreatePackageRequest createPackageRequest) {
        return backOfficeService.createPackage(createPackageRequest);
    }


    @PostMapping("/updatePackage")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity updatePackage(@Valid @RequestBody CreatePackageRequest updatePackageRequest) {
        return backOfficeService.updatePackage(updatePackageRequest);
    }

    @PostMapping("/deletePackage")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity deletePackage(@Valid @RequestBody DeletePackageRequest deletePackageRequest) {
        return backOfficeService.deletePackage(deletePackageRequest);
    }

    @PostMapping("/sendMessageToUsers")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity sendMessageToUsers(@Valid @RequestBody  SendMessageRequest sendMessageRequest) {
        return backOfficeService.sendMessageToUsers(sendMessageRequest);
    }


    @PostMapping("/deleteMessageToUsers")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity deleteMessageToUsers(@Valid @RequestBody SendMessageRequest sendMessageRequest) {
        return backOfficeService.deleteMessageToUsers(sendMessageRequest);
    }

    @PostMapping("/createItem")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity createItem(@Valid @RequestBody ItemsRequest itemsRequest) {
        return backOfficeService.createItem(itemsRequest);
    }

    @PostMapping("/addItemToPackage")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity addItemToPackage(@Valid @RequestBody ItemsRequest itemsRequest) {
        return backOfficeService.addItemToPackage(itemsRequest);
    }

    @PostMapping("/updateItem")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity updateItem(@Valid @RequestBody ItemsRequest itemsRequest) {
        return backOfficeService.updateItem(itemsRequest);
    }

    @PostMapping("/removeItemFromPackage")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity removeItemFromPackage(@Valid @RequestBody ItemsRequest itemsRequest) {
        return backOfficeService.removeItemFromPackage(itemsRequest);
    }

    @PostMapping("/deleteItem")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity deleteItem(@Valid @RequestBody ItemsRequest itemsRequest) {
        return backOfficeService.deleteItem(itemsRequest);
    }


    @GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}

	private String getUsername(){
        return "";
    }
}
